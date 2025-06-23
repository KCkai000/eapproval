package com.example.demo.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.enums.Action;
import com.example.demo.enums.Goto;
import com.example.demo.enums.State;
import com.example.demo.exception.FlowException;
import com.example.demo.exception.LeaveFormException;
import com.example.demo.exception.UserException;
import com.example.demo.mapper.LeaveFormMapper;
import com.example.demo.model.dto.LeaveFormDto;
import com.example.demo.model.entity.Flow;
import com.example.demo.model.entity.FlowLog;
import com.example.demo.model.entity.LeaveForm;
import com.example.demo.model.entity.User;
import com.example.demo.repository.FlowLogRepository;
import com.example.demo.repository.LeaveFormRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.FlowLogService;
import com.example.demo.service.FlowService;
import com.example.demo.service.LeaveFormService;
import com.example.demo.service.UserService;
import com.example.demo.util.AuthUtil;


@Service
public class LeaveFormServiceImpl  implements LeaveFormService{
	
	@Autowired
	private LeaveFormRepository	 leaveFormRepository;
	@Autowired
	private FlowLogRepository flowLogRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private FlowService flowService;
	@Autowired
	private FlowLogService flowLogService;
	@Autowired
	private LeaveFormMapper leaveFormMapper;
	@Autowired
	private UserRepository userRepository;
	
	
	@Override
	@Transactional
	public LeaveForm createLeaveForm(LeaveFormDto dto) {
		// 取得當前使用者
		User user = userService.getCurrentUser();
		if (user == null) {
			throw new UserException("User not found");
		}
		//取得當前假別
		State state = State.valueOf(dto.getFlowState());		
		
		//設定起始流程 = START
		Action action = Action.START;
		
		// 找到初始流程
		Flow initialFlow = flowService.findInitialFlow(state, action, user.getRole().getId());

		
		//建立請假單
		LeaveForm form = leaveFormMapper.toEntity(dto);
		form.setState(state);
		form.setCurrentFlow(initialFlow);
		form.setAction(initialFlow.getAction());
		LeaveForm saved = leaveFormRepository.save(form);
		
		//建立流程紀錄
		flowLogService.createFlowLog(saved, initialFlow, user);
		
		//取得下一個流程
		Goto nextCurrent = initialFlow.getGoTo();
		System.out.println("【流程跳轉】nextCurrentGoTo = " + nextCurrent + ", state = " + state + ", action = SUBMITTED");
		Flow submittedFlow = flowService.findNextFlowByCurrent(nextCurrent, state, Action.SUBMITTED); //找到下個流程action為submitted
		
		// ✅ 如果查不到就拋出錯誤，防止後面 NPE
		if(submittedFlow == null) {
			throw new FlowException("找不到初始流程，，請檢查 flow 表是否有 currentgoTo=USER, State=\" + state + \", Action=SUBMITTED 的設定");
		}
		
		//更新假單為下個流程
		saved.setCurrentFlow(submittedFlow);
		saved.setState(state);
		saved.setAction(submittedFlow.getAction());
		System.out.println("確認 action: " + saved.getAction());

		leaveFormRepository.save(saved);
		
		//建立第二筆流程紀錄(SUBMITTED)
		flowLogService.createFlowLog(saved, submittedFlow, user);
		
		return saved;
	}

	@Override
	@Transactional
	public List<LeaveForm> getAllLeaveForms() {
		return leaveFormRepository.findAll();
	}

	@Override
	@Transactional(readOnly =true)
	public List<LeaveFormDto> findLeaveFormDtos(Integer userId) {
		List<LeaveForm> forms = flowLogRepository.findLeaveFormByUserId(userId);
		return forms.stream()
				.map(leaveFormMapper::toDto)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public LeaveForm updateLeaveForm(LeaveFormDto dto) {
		LeaveForm form = leaveFormRepository.findById(dto.getId())
				.orElseThrow(() -> new LeaveFormException("Leave form not found with id: " + dto.getId()));
		form.setStartDate(dto.getStartDate());
		form.setEndDate(dto.getEndDate());
		form.setReason(dto.getReason());
		return leaveFormRepository.save(form);
	}

	@Override
	@Transactional
	public void deactivateLeaveForm(Integer id) {
		LeaveForm form = leaveFormRepository.findById(id)
				.orElseThrow(() -> new LeaveFormException("無法找到假單ID: " + id));
		form.setActive(false);
		leaveFormRepository.save(form);
	}

	@Override
	@Transactional
	public void processLeaveForm(Integer formId, Action userAction) {
		
		// 取得表單以及當前流程
		LeaveForm form = leaveFormRepository.findWithFlowLogsById(formId)
				.orElseThrow(() -> new LeaveFormException("無法找到請假單: " + formId));
		// 查到當前使用者
		User usernow = userService.getCurrentUser();
		// 取得最新流程紀錄
		FlowLog currentLog = flowLogService.findLatestSubmittedLog(formId);
		if(currentLog == null) {
			throw new FlowException("查無當前 submitted 流程紀錄");
		}
		
		Flow currentFlow = currentLog.getFlow();
		Goto currentGoto = currentFlow.getGoTo(); //當前流程的節點
		State currentState = currentFlow.getState();
		
		Action nextAction = null;
		
		if(currentGoto == Goto.REVIEW_MANAGER) {
			if(userAction == Action.APPROVED) {
				nextAction = Action.PENDING;
			}else if(userAction == Action.REJECTED) {
				nextAction = Action.REJECTED;
			}else{
				throw new FlowException("REVIEW_MANAGER僅支援APPROVED和REJECTED操作");
			}
		}else if(currentGoto == Goto.REVIEW_HR) {
			if(userAction == Action.APPROVED) {
				nextAction = Action.APPROVED;
			}else if(userAction == Action.REJECTED) {
				nextAction = Action.REJECTED;
			}else {
				throw new FlowException("REVIEW_HR 僅支援 APPROVED 或 REJECTED 操作");
			}
		}else {
			throw new FlowException("目前節點 [" + currentGoto + "] 無法處理遞移");
		}
		
		// 根據目前節點、狀態、與使用者動作 查詢下個流程
	    
	    Flow nextFlow = flowService.findNextFlowByGoTo(currentGoto, currentState, nextAction);
	    if(nextFlow == null) {
	    	System.out.println("查無符合條件的下個節點");
	    	throw new FlowException("❌ 無法找到下個流程，參數如下：" +
	                "previousGoTo=" + currentGoto +
	                ", state=" + currentState +
	                ", action=" + nextAction);
	    }
	    
	    // 更新假單目前流程狀態
	    form.setCurrentFlow(nextFlow);
	    form.setAction(userAction);
	    leaveFormRepository.save(form);
	    
	    //新增流程紀錄
	    flowLogService.createFlowLog(form, nextFlow, usernow);
	    System.out.println("成功推進流程至：" + nextFlow.getGoTo());
	}

	@Override
	public List<LeaveFormDto> findPenadingFormsForReview() {
		//取得當前使用者角色
		String roleName = AuthUtil.getCurrentRole();
		
		// 根據角色決定對應的審核動作
		Goto goTo;
		Action expectedAction;
				
				
		switch(roleName.toLowerCase()) {
			case "manager" -> {
				goTo = Goto.REVIEW_MANAGER;
				expectedAction = Action.PENDING;
			}
			case "hr" -> {
				goTo = Goto.REVIEW_HR;
				expectedAction = Action.APPROVED;
			}
			default -> {
				throw new FlowException("無法辨識角色對應之審核流程");
			}
		};
		
		
		// 呼叫 repository 查詢對應流程的假單(根據角色及動作)
		List<LeaveForm> forms = leaveFormRepository.findPendingByGoToAndAction(goTo, expectedAction);
		
		//轉乘 DTO 回傳
		return forms.stream()
				.map(leaveFormMapper::toDto)
				.collect(Collectors.toList());
	}
//	//表單送出才更新狀態
//	@Override
//	@Transactional
//	public void submitForm(Integer formId) {
//		User user = userService.getCurrentUser();
//		LeaveForm form = leaveFormRepository.findWithFlowLogsById(formId)
//				.orElseThrow(()-> new LeaveFormException("找不到假單"));
//		
//		Flow curreFlow = form.getCurrentFlow();
//		Goto nextGoto = curreFlow.getGoTo();
//		State state = curreFlow.getState();
//		
//		// 找 SUBMITTED 的下一個節點
//		Flow submittedFlow = flowService.findNextFlowWithRole(nextGoto, state, Action.SUBMITTED, user.getRole().getId());
//		
//		//更新 currentFlow 與 action
//		form.setCurrentFlow(submittedFlow);
//		form.setAction(submittedFlow.getAction());
//		leaveFormRepository.save(form);
//		
//		//更新 SUBMITTED 流程紀錄
//		flowLogService.createFlowLog(form, submittedFlow, user);
//	}

	
	
}
