package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;
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
import com.example.demo.util.FlowActionResolver;


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
	@Autowired
	private FlowActionResolver flowActionResolver;
	
	
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
		// 查詢假單與當前流程
		LeaveForm form = leaveFormRepository.findWithFlowLogsById(formId)
				.orElseThrow(() -> new LeaveFormException("找不到當前假單" + formId));
		// 找到當前流程節點
		Flow currentFlow = form.getCurrentFlow();
		if(currentFlow == null) {
			throw new FlowException("假單尚未啟動流程");
		}
		
		Goto currentGoto = currentFlow.getGoTo();
		State currentState = currentFlow.getState();
	
		// 驗證動作是否合邏輯
		Action nextAction = flowActionResolver.resolverNextAction(currentGoto, userAction);
		
		// 嘗試取得下個節點
		Flow nextFlow = flowService.findNextFlowByGoTo(currentGoto, currentState, nextAction);
	
		if(nextFlow == null) {
			System.out.println("⚠️ 無法查到下一步流程，改查 FinalStep");
			Optional<Flow> maybeFinal = flowService.findFinalStep(currentGoto, currentState, nextAction);
			if(maybeFinal.isPresent()) {
				nextFlow = maybeFinal.get();
				form.setCompleted(true);
				form.setActive(false);
				System.out.println("成功查到finalStep，流程完成");
			}else {
				throw new FlowException("找不到符合流程");
			}
			
		}
		//更新假單狀態
		form.setCurrentFlow(nextFlow);
		form.setAction(userAction);
		leaveFormRepository.save(form);
		
		//紀錄流程log
		User currentUser = userService.getCurrentUser();
		flowLogService.createFlowLog(form, nextFlow, currentUser);
		System.out.println("已建立流程紀錄，下一站：\" + nextFlow.getGoTo()");
		
	}

	@Override
	public List<LeaveFormDto> findPenadingFormsForReview() {
		//取得當前使用者角色
		String roleName = AuthUtil.getCurrentRole();
		
		// 根據角色決定對應的審核動作
		Goto currentGoTo;
		
		switch(roleName.toLowerCase()) {
			
		case "manager" -> currentGoTo = Goto.REVIEW_MANAGER;
		case "hr" -> currentGoTo = Goto.REVIEW_HR;
		default -> throw new FlowException("舞法取得角色對應之流程");
				
		}
		
		// 只要目前流程處於此節點，就是該角色的待審假單
		List<LeaveForm> forms = leaveFormRepository.findByCurrentFlow_GoTo(currentGoTo); 
		
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
