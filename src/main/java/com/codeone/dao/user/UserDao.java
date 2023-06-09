package com.codeone.dao.user;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.codeone.dto.user.UserDto;

@Mapper
public interface UserDao {
	
	// 회원가입 전 이메일 중복체크
	UserDto checkEmail(String email);

	int checkId(String id);
	
	int addUser(UserDto dto);
	
	int updateEmailKey(UserDto dto);
	

	int updateEmailAuth(UserDto dto);
	
	UserDto getMember(String email);
	
	UserDto getUserById(String id);
	
	
	UserDto selectOneBySeq(int seq);

	UserDto checkEmailKey(String emailKey);
	
	int updateProfile(UserDto user);
	
	//기업회원 인증 시 업데이트
	int updateCompanyAuth(UserDto dto);
}
