package com.codeone.dao.job;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.codeone.dto.job.ComPagingDto;
import com.codeone.dto.job.JobDto;
import com.codeone.dto.job.JobFilterDto;

@Mapper
public interface JobDao {
	
// ---------채용메인 -----------------//	
	//채용 글목록
	List<JobDto> joblist(JobFilterDto filter);
	//채용 seq조회
	JobDto getJob(int comseq);
	
// ---------기업회원 -----------------//	
	//기업회원 글목록
	List<JobDto> combbslist(ComPagingDto paging);
	//기업회원 글 총수(페이징 위해)
	int getAllComBbs(ComPagingDto paging);
	//기업회원 글작성
	int writeJob(JobDto job);
	//기업회원 글수정
	int updateJob(JobDto job);
	//기업회원 글삭제
	int deleteJob(int comseq);

}