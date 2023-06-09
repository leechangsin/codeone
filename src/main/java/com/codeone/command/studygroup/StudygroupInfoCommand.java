package com.codeone.command.studygroup;

import java.time.LocalDate;

import com.codeone.dto.studygroup.StudygroupInfoDto;
import com.codeone.dto.studygroup.StudygroupManagementDto;

import lombok.Data;

@Data
public class StudygroupInfoCommand {
	private int memberSeq;						// 모집글 작성자 번호
	private String title;						// 모집글 제목
	private String contents;					// 모집글 내용
	private int recruitmentType;				// 모집 구분
	private int wayOfProceeding;				// 진행 방식
	private int numberOfRecruits;				// 모집 인원
	private LocalDate startDate;				// 진행 기간(시작 날짜)
	private LocalDate endDate;					// 진행 기간(종료 날짜)
	private int[] recruitmentPart;				// 모집 분야
	private int[] technologyStack;				// 기술 스택
	private LocalDate deadlineForRecruitment;	// 모집 마감 날짜
	private boolean isVisible;					// 공개, 비공개

	public boolean getIsVisible() {
		return isVisible;
	}
	
	public void setIsVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
	
	public StudygroupManagementDto toStudygroupManagementDto() {
		StudygroupManagementDto dto = new StudygroupManagementDto();
		dto.setMemberSeq(memberSeq);
		dto.setIsVisible(isVisible);
		
		return dto;
	}
	
	public StudygroupInfoDto toStudygroupInfoDto() {
		StudygroupInfoDto dto = new StudygroupInfoDto();
		dto.setTitle(title);
		dto.setContents(contents);
		dto.setRecruitmentType(recruitmentType);
		dto.setWayOfProceeding(wayOfProceeding);
		dto.setNumberOfRecruits(numberOfRecruits);
		dto.setStartDate(startDate);
		dto.setEndDate(endDate);
		dto.setRecruitmentPart(recruitmentPart);
		dto.setTechnologyStack(technologyStack);
		dto.setDeadlineForRecruitment(deadlineForRecruitment);
		
		return dto;
	}
}
