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
		LeaveForm saved = leaveFormRepository.save(form);
		
		//建立流程紀錄
		flowLogService.createFlowLog(saved, initialFlow, user);
		
		//取得下一個流程
		Goto next = initialFlow.getGoTo();
		Flow submittedFlow = flowService.findNextFlowWithRole(next, state, Action.SUBMITTED, user.getRole().getId()); //找到下個流程action為submitted
		
		//更新假單為下個流程
		saved.setCurrentFlow(submittedFlow);
		leaveFormRepository.save(saved);
		
		//建立第二筆流程紀錄(SUBMITTED)
		flowLogService.createFlowLog(form, submittedFlow, user);
		
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
	public void processLeaveForm(Integer formId, Action action) {
		
		// 取得表單以及當前流程
		LeaveForm form = leaveFormRepository.findWithFlowLogsById(formId)
				.orElseThrow(() -> new LeaveFormException("無法找到請假單: " + formId));
		User usernow = userService.getCurrentUser();
		Flow currentFlow = form.getCurrentFlow();
		Goto nextGoto = currentFlow.getGoTo();
		State state = currentFlow.getState();
		
		// 根據目前節點、 假別、與假單的狀態， 尋找下一個流程
		Flow nextFlow;
		try {
				
			nextFlow = flowService.findNextFlowWithRole(nextGoto, state, action, usernow.getRole().getId());
			System.out.println("處理流程跳轉 ➜ 使用者角色：" + usernow.getRole().getName());
		}catch(Exception e) {
			throw new FlowException("無法找到下個流程，請重新確認參數" + e.getMessage());
		}
			
		// ✅ 防呆：流程終點（找不到下一步）
		 if (nextFlow == null) {
		    // 你可以選擇設定 currentFlow = null 或直接 return
		    // form.setCurrentFlow(null); // 若你系統能處理 null
		    System.out.println("流程已結束，無下一階段流程");
		    return;
		 }
		// 更新假單狀態	
		form.setCurrentFlow(nextFlow);;
		leaveFormRepository.save(form);
		
		//建立流程紀錄
		User user= userService.getCurrentUser();
		flowLogService.createFlowLog(form, nextFlow, user);
	}

	@Override
	public List<LeaveFormDto> findPenadingFormsForReview() {
		//取得當前使用者角色
		String roleName = AuthUtil.getCurrentRole();
		
		// 根據角色決定對應的審核動作
		Goto goTo = switch(roleName) {
		case "manager" -> Goto.REVIEW_MANAGER;
		case "HR" -> Goto.REVIEW_HR;
		case "admin" -> null;
		default -> null;
		};
		
		if(goTo == null) {
			throw new FlowException("找不到對應審核動作，請檢察角色權限設定");
		}
		
		// 呼叫 repository 查詢對應流程的假單(根據角色及動作)
		List<LeaveForm> forms = leaveFormRepository.findPendingByGoToAndAction(goTo, Action.SUBMITTED);
		
		//轉乘 DTO 回傳
		return forms.stream()
				.map(leaveFormMapper::toDto)
				.collect(Collectors.toList());
	}

	
	
}
