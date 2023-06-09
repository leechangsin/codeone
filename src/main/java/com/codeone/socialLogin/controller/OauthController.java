package com.codeone.socialLogin.controller;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Objects;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codeone.dto.user.UserDto;
import com.codeone.etc.TempKey;
import com.codeone.service.user.UserService;
import com.codeone.socialLogin.SocialLoginType;
import com.codeone.socialLogin.oauth.GoogleOauth;
import com.codeone.socialLogin.service.OauthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin(origins="http://localhost:3000/")
@RequiredArgsConstructor
@RequestMapping(value = "/auth")
@Slf4j
public class OauthController {
    private final OauthService oauthService;
    private final UserService userService;


    /**
     * 사용자가 소셜로그인 요청을 보냈을 때 처리하는 로직
     * @param socialLoginType (GOOGLE, NAVER, KAKAO)
     */
    @GetMapping(value = "/{socialLoginType}")
    public void socialLoginType(
            @PathVariable(name = "socialLoginType") SocialLoginType socialLoginType) {
		log.info(">> 사용자로부터 SNS 로그인 요청을 받음 :: {} Social Login", socialLoginType);
        oauthService.request(socialLoginType);
    }
    
	/**
     * Social Login API Server 요청에 의한 callback 을 처리
     * @param socialLoginType (GOOGLE, NAVER, KAKAO)
     * @param code API Server 로부터 넘어오는 code
     * @return ResponseEntity를 신규냐 기존회원인가에 따라 다르게 정보를 준다
	 * @throws Exception 
     */
    @GetMapping(value = "/{socialLoginType}/callback")
    public void  callback(
            @PathVariable(name = "socialLoginType") SocialLoginType socialLoginType,
            HttpServletResponse response,
            HttpServletRequest request) throws Exception {
        	String code = request.getParameter("code"); 
        	// 하나의 메소드로 퉁쳐버리기
        	HashMap<Integer, UserDto> result = oauthService.requestUserInfo(socialLoginType,code);
        	 for(Integer key : result.keySet()){
        		 UserDto user = result.get(key);
        		 // 기존인 경우
                 if(key == 0) {                	 
                	 String emailKey = new TempKey().getKey(10, false); // 랜덤키 길이 설정
                	 userService.sendLoginEmail(user.getEmail(), emailKey);
                	 response.sendRedirect("http://localhost:3000/loginAf");
                 } else {
                	 // 신규인경우
                	 response.sendRedirect("http://localhost:3000/signupinfo?email="+ user.getEmail()+ "&filename="+ user.getFilename() );
                 }
             }
    }
    
}