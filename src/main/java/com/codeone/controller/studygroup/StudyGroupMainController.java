package com.codeone.controller.studygroup;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codeone.command.studygroup.StudygroupDeleteCommand;
import com.codeone.command.studygroup.StudygroupInfoCommand;
import com.codeone.command.studygroup.StudygroupListCommand;
import com.codeone.command.studygroup.StudygroupUpdateCommand;
import com.codeone.dto.studygroup.NumberOfRecruitsDto;
import com.codeone.dto.studygroup.PositionDto;
import com.codeone.dto.studygroup.RecruitmentTypeDto;
import com.codeone.dto.studygroup.StudygroupDetailDto;
import com.codeone.dto.studygroup.StudygroupInfoDto;
import com.codeone.dto.studygroup.StudygroupLikeDto;
import com.codeone.dto.studygroup.StudygroupListDto;
import com.codeone.dto.studygroup.TechnologyStackDto;
import com.codeone.dto.studygroup.WayOfProceedingDto;
import com.codeone.dto.user.UserDto;
import com.codeone.etc.LoginUtil;
import com.codeone.etc.StaticVariable;
import com.codeone.exception.DeletedStudygroupException;
import com.codeone.exception.NotPermissionToModifyException;
import com.codeone.exception.StudygroupNotFoundException;
import com.codeone.service.studygroup.StudygroupDetailService;
import com.codeone.service.studygroup.StudygroupFilterService;
import com.codeone.service.studygroup.StudygroupInfoService;
import com.codeone.service.studygroup.StudygroupLikeService;
import com.codeone.service.studygroup.StudygroupMngService;
import com.codeone.validator.studygroup.DeleteStudygroupValidator;
import com.codeone.validator.studygroup.StudygroupListValidator;
import com.codeone.validator.studygroup.UpdateStudygroupValidator;
import com.codeone.validator.studygroup.WriteStudygroupValidator;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/studygroup")
public class StudyGroupMainController {
	@Autowired
	StudygroupInfoService studygroupInfoService;
	@Autowired
	StudygroupDetailService studygroupDetailService;
	@Autowired
	StudygroupLikeService StudygroupLikeService;
	@Autowired
	StudygroupMngService studygroupMngService;
	
	@Autowired
	StudygroupFilterService studygroupFilterService;
	
	@PostMapping()
	public ResponseEntity<Void> writeStudygroupRecruitment(StudygroupInfoCommand studygroupCommand, BindingResult errors, HttpSession session) {
		// 로그인 여부 확인 및 로그인한 사용자 번호 꺼내기
		if(!LoginUtil.isLogin(session)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		UserDto loginUserInfo = LoginUtil.getLoginUserInfo(session);
		int memberSeq = loginUserInfo.getSeq();
		// 로그인 여부 확인 및 로그인한 사용자 번호 꺼내기
		
		// 커맨드 객체 검증
		Validator validator = new WriteStudygroupValidator();
		validator.validate(studygroupCommand, errors);
		
		if(errors.hasErrors()) {
			// 클라이언트가 잘못된 값을 보냈다면
			return ResponseEntity.badRequest().build();
		}
		
		studygroupCommand.setMemberSeq(memberSeq);
		
		// 모집글 작성
		studygroupInfoService.writeStudygroupRecruitment(studygroupCommand);
		
		return ResponseEntity.ok().header("Location", "/studygroup").build();
	}
	
	@DeleteMapping()
	public ResponseEntity<Void> deleteStudygroupRecruitment(StudygroupDeleteCommand studygroupDeleteCommand, BindingResult errors, HttpSession session) {
		// 로그인 여부 확인 및 로그인한 사용자 번호 꺼내기
		if(!LoginUtil.isLogin(session)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		UserDto loginUserInfo = LoginUtil.getLoginUserInfo(session);
		int memberSeq = loginUserInfo.getSeq();
		// 로그인 여부 확인 및 로그인한 사용자 번호 꺼내기
		
		// 커맨드 객체 검증
		Validator validator = new DeleteStudygroupValidator();
		validator.validate(studygroupDeleteCommand, errors);
		
		if(errors.hasErrors()) {
			// 클라이언트가 잘못된 값을 보냈다면
			return ResponseEntity.badRequest().build();
		}
		
		// 삭제하기 위해 삭제를 요청한 사용자(로그인한 사용자)의 번호를 저장
		studygroupDeleteCommand.setMemberSeq(memberSeq);
		
		try {
			// 모집글 삭제
			boolean result = studygroupInfoService.deleteStudygroupRecruitment(studygroupDeleteCommand);
			
			if(result) {
				// 모집글을 삭제했을 경우
				return ResponseEntity.ok().build();
			} else {
				// 모집글을 삭제하지 못했을 경우
				return ResponseEntity.badRequest().build();
			}
		} catch(NotPermissionToModifyException e) {
			// 이미 스터디가 시작된 경우
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
	}
	
	@PutMapping()
	public ResponseEntity<Void> updateStudygroupRecruitment(StudygroupUpdateCommand studygroupUpdateCommand, BindingResult errors, HttpSession session) {
		// 로그인 여부 확인 및 로그인한 사용자 번호 꺼내기
		if(!LoginUtil.isLogin(session)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		UserDto loginUserInfo = LoginUtil.getLoginUserInfo(session);
		int memberSeq = loginUserInfo.getSeq();
		// 로그인 여부 확인 및 로그인한 사용자 번호 꺼내기
		
		// 커맨드 객체 검증
		Validator validator = new UpdateStudygroupValidator();
		validator.validate(studygroupUpdateCommand, errors);
		
		if(errors.hasErrors()) {
			// 클라이언트가 잘못된 값을 보냈다면
			return ResponseEntity.badRequest().build();
		}
		
		// 수정할 모집 글 번호
		studygroupUpdateCommand.setMemberSeq(memberSeq);
		
		try {
			// 모집글 수정
			boolean result = studygroupInfoService.updateStudygroupRecruitment(studygroupUpdateCommand);
			
			if(result) {
				// 모집글 수정 완료
				return ResponseEntity.ok().build();
			} else {
				// 모집글 수정 실패
				return ResponseEntity.badRequest().build();
			}
		} catch(NotPermissionToModifyException e) {
			// 모집글 수정 권한 없음
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
	}
	
	@GetMapping("/detail/{seq}")
	public ResponseEntity<StudygroupDetailDto> getStudygroupInfo(@PathVariable("seq") int seq) {
		// seq 검증
		if(seq <= 0) {
			return ResponseEntity.badRequest().build();
		}
		
		try {
			// 스터디 그룹 정보 조회
			StudygroupDetailDto studygroupDetail = studygroupDetailService.getStudygroupInfo(seq);
			
			return ResponseEntity.ok(studygroupDetail);
		} catch(StudygroupNotFoundException |  DeletedStudygroupException e) {
			// StudygroupNotFoundException -> 잘못된 seq를 보내 스터디 그룹을 찾지 못했거나
			// DeletedStudygroupException -> 삭제된 스터디 그룹의 상세 정보를 요청했을 경우
			return ResponseEntity.noContent().build();
		}
	}
	
	@GetMapping("/list")
	public ResponseEntity<List<StudygroupListDto>> getStudygroupList(StudygroupListCommand studygroupListCommand, BindingResult errors, HttpSession session) {
		Validator validator = new StudygroupListValidator();
		// 필터링 값을 전달 받았을 경우 전달 받은 필터링 값 검증
		validator.validate(studygroupListCommand, errors);
		
		if(errors.hasErrors()) {
			// 클라이언트가 잘못된 값을 보냈다면
			return ResponseEntity.badRequest().build();
		}
		
		// 로그인한 사용자 번호 꺼내기 (로그인은 선택 사항)
		int memberSeq = 0;
		if(LoginUtil.isLogin(session)) {
			UserDto loginUserInfo = LoginUtil.getLoginUserInfo(session);
			memberSeq = loginUserInfo.getSeq();
		}
		
		System.out.println("memberSeq = " + memberSeq);
		
		// 좋아요 여부를 확인하기 위해 로그인한 사용자 번호 저장
		studygroupListCommand.setMemberSeq(memberSeq);
		
		// 페이지 번호에 맞게 검색 범위 설정
		studygroupListCommand.setStart(studygroupListCommand.getDepth() * StaticVariable.DEPTH_PER_CONTENTS_AMOUNT);
		studygroupListCommand.setEnd((studygroupListCommand.getDepth() + 1) * StaticVariable.DEPTH_PER_CONTENTS_AMOUNT);
		
		List<StudygroupListDto> studygroupList = studygroupInfoService.getStudygroupList(studygroupListCommand);
		
		if(studygroupList.size() == 0 || studygroupList.get(0) == null) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.ok(studygroupList);
		}
	}
	
	@PostMapping("/toggleLike/{seq}")
	public ResponseEntity<Void> toggleLike(@PathVariable("seq") int seq, HttpSession session){
		if(seq <= 0) {
			// 클라이언트가 잘못된 값을 보냈다면
			return ResponseEntity.badRequest().build();
		}
		
		// 로그인 여부 확인 및 로그인한 사용자 번호 꺼내기
		if(!LoginUtil.isLogin(session)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		UserDto loginUserInfo = LoginUtil.getLoginUserInfo(session);
		int memberSeq = loginUserInfo.getSeq();
		// 로그인 여부 확인 및 로그인한 사용자 번호 꺼내기
		
		StudygroupLikeDto studygroupLike = new StudygroupLikeDto();
		studygroupLike.setStudygroupSeq(seq);
		studygroupLike.setMemberSeq(memberSeq);
		
		try {
			StudygroupLikeService.toggleLike(studygroupLike);
			return ResponseEntity.ok().build(); 
		} catch(DeletedStudygroupException e) {
			// 삭제된 스터디 그룹일 경우
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
	}
	
	
	@GetMapping("/filter/position")
	public ResponseEntity<List<PositionDto>> getPositionList() {
		List<PositionDto> positionList = studygroupFilterService.getPositionList();
		
		return ResponseEntity.ok(positionList);
	}
	
	@GetMapping("/filter/technology_stack")
	public ResponseEntity<List<TechnologyStackDto>> getTechnologyStackList() {
		List<TechnologyStackDto> technologyStackList = studygroupFilterService.getTechnologyStackList();
		
		return ResponseEntity.ok(technologyStackList);
	}
	
	@GetMapping("/filter/recruitment_type")
	public ResponseEntity<List<RecruitmentTypeDto>> getRecruitmentTypeList() {
		List<RecruitmentTypeDto> recruitmentTypeList = studygroupFilterService.getRecruitmentTypeList();
		
		return ResponseEntity.ok(recruitmentTypeList);
	}
	
	@GetMapping("/filter/way_of_proceeding")
	public ResponseEntity<List<WayOfProceedingDto>> getWayOfProceedingList() {
		List<WayOfProceedingDto> wayOfProceedingList = studygroupFilterService.getWayOfProceedingList();
		
		return ResponseEntity.ok(wayOfProceedingList);
	}
	
	@GetMapping("/filter/number_of_recruits")
	public ResponseEntity<List<NumberOfRecruitsDto>> getNumberOfRecruits() {
		List<NumberOfRecruitsDto> numberOfRecruitsList = studygroupFilterService.getNumberOfRecruitsList();
		
		return ResponseEntity.ok(numberOfRecruitsList);
	}
	
	@PostMapping("/{seq}/view")
	public void increseViewAmount(@PathVariable int seq) {
		if(seq > 0) {
			// 클라이언트가 제대로 된 seq를 보냈을 경우에만 조회수 증가
			studygroupMngService.increaseViewAmount(seq);
		}
	}
	
	// 좋아요한 모집글 캘린더에 불러오기
	@GetMapping("/getLikedInfo")
	public ResponseEntity<Map<String, Object>> getLikedInfo(@RequestParam("seq") int seq){
		System.out.println("studygroupController getLikedInfo() " + new Date());
	    System.out.println("//////// seq확인 "+seq);
	    
	    Map<String, Object> map = new HashMap<>();
	    
	    try {
	    	List<StudygroupInfoDto> list = studygroupInfoService.getLikedInfo(seq);
	    	map.put("list", list);
	    	return ResponseEntity.ok(map); 
	    	
	    } catch (Exception e) {
	    	return ResponseEntity.badRequest().build();
	    }
	}
}
