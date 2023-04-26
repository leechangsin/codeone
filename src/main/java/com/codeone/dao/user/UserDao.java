package com.codeone.dao.user;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.codeone.dto.user.UserDto;

@Mapper
public interface UserDao {
	
	// 회원가입 전 이메일 중복체크
	int checkEmail(String email);
	
<<<<<<< HEAD
	// 이메일 중복체크
=======
	// 아이디 중복체크
>>>>>>> 28a478e5eab8bbdf80b7923e47285d2fa1a6f0a0
	int checkId(String id);
	
	int addUser(UserDto dto);
	
	int updateEmailKey(UserDto dto);
	

	int updateEmailAuth(UserDto dto);
	
	UserDto getMember(String email);
	
	
	UserDto selectOneBySeq(int seq);
<<<<<<< HEAD

=======
	
	UserDto checkEmailKey(String emailKey);
>>>>>>> 28a478e5eab8bbdf80b7923e47285d2fa1a6f0a0
}
