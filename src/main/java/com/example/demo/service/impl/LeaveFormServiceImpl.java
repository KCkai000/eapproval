package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.enums.Action;
import com.example.demo.enums.Goto;
import com.example.demo.enums.State;
import com.example.demo.exception.LeaveFormException;
import com.example.demo.exception.UserException;
import com.example.demo.mapper.LeaveFormMapper;
import com.example.demo.model.dto.LeaveFormDto;
import com.example.demo.model.entity.Flow;
import com.example.demo.model.entity.FlowLog;
import com.example.demo.model.entity.LeaveForm;
import com.example.demo.model.entity.User;
import com.example.demo.repository.FlowLogRepository;
import com.example.demo.repository.FlowRepository;
import com.example.demo.repository.LeaveFormRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.LeaveFormService;
import com.example.demo.util.EnumUtil;

@Service
public class LeaveFormServiceImpl  implements LeaveFormService{
	
	@Autowired
	private LeaveFormRepository leaveFormRepository;	
	@Autowired
	private FlowRepository flowRepository;	
	@Autowired	
	private FlowLogRepository flowLogRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private LeaveFormMapper leaveFormMapper;
	@Autowired
	private EnumUtil enumUtil;
	
	@Transactional
	@Override
	public LeaveForm createLeaveForm(LeaveFormDto dto) {
		// 1.  將輸入轉enum
		State stateEnum = State.fromDisplayName(dto.getFlowState());
		Action actionEnum = Action.fromDisplayName(dto.getAction());
		if (actionEnum == null) {
		    actionEnum = Action.START; // fallback
		}
		
		// 2.查詢對應流程
		Optional<Flow> flowOpt = flowRepository.findByStateAndAction(stateEnum, actionEnum);
		if(flowOpt.isEmpty()) {
			
			throw new LeaveFormException("找不到對應的流程" + stateEnum + " 和 " + actionEnum);
		}
		Flow flow = flowOpt.get();
		// 3.建立假單
		LeaveForm leaveForm = new LeaveForm();
		leaveForm.setState(flow);
		leaveForm.setAction(flow);
		leaveForm.setStartDate(dto.getStartDate());
		leaveForm.setEndDate(dto.getEndDate());
		leaveForm.setReason(dto.getReason());
		
		//儲存假單
		LeaveForm savedForm = leaveFormRepository.save(leaveForm); //儲存假單
		leaveFormRepository.flush();
		
		//建立第一筆flowLog (紀錄請假單創建的流程)
		User currentUser = getCurrentUser(); // 假設從 dto 中獲取當前用戶
		
		FlowLog firstLog = new FlowLog();
		firstLog.setLeaveForm(savedForm);
	    firstLog.setFlow(flow); // 初始動作流程
	    System.out.println("第一筆流程紀錄的狀態: " + flow.getState());
	    firstLog.setUser(currentUser);
	    flowLogRepository.save(firstLog);
	    flowLogRepository.flush();
	    
	    // 拿到下一個節點(goto)
	    String nextStep = flow.getGoTo() != null ? flow.getGoTo().name() : null;
	    System.out.println(nextStep);
	    
	    // 將字串轉成 Goto enum
	    Goto nextStepEnum = flow.getGoTo();
	    System.out.println(nextStepEnum);
	    
	    // 決定下一個流程是哪個action
	    Action nextActiionEnum = flow.getAction() != null ? Action.SUBMITTED: flow.getAction();
	    System.out.println(nextActiionEnum);
	    // 根據nextStepEnum和nextActionEnum 再去找對應的 Flow
	    Flow nextFlow = flowRepository
	    		.findByStateAndActionAndCurrentgoTo(stateEnum, nextActiionEnum, nextStepEnum)	    	
	    		.filter(f -> f.getAction() == Action.SUBMITTED )
	    		.orElseThrow(() -> new LeaveFormException("找不到下一個流程 " + nextStepEnum + " 和 " + nextActiionEnum));
	    
	    // 將下一階段流程寫入 flowLog
	    FlowLog nextLog = new FlowLog();
	    nextLog.setLeaveForm(savedForm); // 確保 leaveForm 已經有 ID
	    nextLog.setFlow(nextFlow);
	    nextLog.setUser(currentUser);
	    flowLogRepository.save(nextLog);
	    System.out.println("下一個流程紀錄: " + nextLog);
	    flowLogRepository.flush();
	    
	    return savedForm;
		
	}

	@Override
	@Transactional(readOnly = true)	
	public List<LeaveFormDto> findLeaveFormDtos(Integer userId) {
		List<LeaveForm> forms = flowLogRepository.findLeaveFormByUserId(userId);
		System.out.println("查詢到的假單數量：" + forms.size());
		return forms.stream()
				.map(leaveFormMapper::toDto)
				.collect(Collectors.toList());
	}

	@Override
	public List<LeaveForm> getAllLeaveForms() {		
		return leaveFormRepository.findAll();
	}

	@Override
	public LeaveForm updateLeaveForm(LeaveForm updateForm) {
		LeaveForm existingForm = leaveFormRepository.findById(updateForm.getId())
			.orElseThrow(() -> new LeaveFormException("假單不存在"));
		
		existingForm.setState(updateForm.getState());
		existingForm.setAction(updateForm.getAction());
		existingForm.setStartDate(updateForm.getStartDate());
		existingForm.setEndDate(updateForm.getEndDate());
		existingForm.setReason(updateForm.getReason());
		return leaveFormRepository.save(existingForm);
	}

	@Override
	public void deactivateLeaveForm(Integer id) {
		LeaveForm form = leaveFormRepository.findById(id)
			.orElseThrow(() -> new LeaveFormException("假單不存在"));
		form.setActive(false); // 將 active 設為 false 以軟刪除
		leaveFormRepository.save(form);		
	}
	
	//獲取當前使用者
	private User getCurrentUser() {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String username = authentication.getName();
	    return userRepository.findByAccount(username)
	        .orElseThrow(() -> new UserException("找不到目前登入的使用者"));
	}
	
	//創立流程紀錄 (flowLog)
	private void createFlowLog(LeaveForm form, Flow flow, User user) {
		FlowLog log = new FlowLog();
		log.setLeaveForm(form);
		log.setFlow(flow);
		log.setUser(user);
		flowLogRepository.save(log);
	}

}
