package com.movie.smallbox.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.movie.smallbox.dto.Login;
import com.movie.smallbox.dto.Member;
import com.movie.smallbox.service.MemberService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@CrossOrigin("http://127.0.0.1:5500/")
public class MemberController {

	@Autowired
	MemberService memberService;
	
	@PostMapping("insertMember")
	public Map<String, String> insertMember(@Valid @RequestBody Member m){
		Map<String, String> responseData = new HashMap();
		
		try {
			memberService.insertMember(m);
			responseData.put("msg", "회원가입을 축하합니다.");
			
		} catch(Exception e) {
			e.printStackTrace();
			responseData.put("msg", "회원가입 중 오류가 발생했습니다.");
		}
		return responseData;
	}
	@PostMapping("tokenLogin")
	public Map<String, String> tokenLogin(@RequestBody Member m){
		Map<String, String> responseData = new HashMap<>();
		
		try {
			Login loginInfo = memberService.tokenLogin(m);
			
			if(loginInfo != null && loginInfo.getUserName() != null && loginInfo.getToken() != null) {
				responseData.put("username", loginInfo.getUserName());
				responseData.put("Authorization", loginInfo.getToken());
			} else {
				responseData.put("msg", "다시 로그인 해주세요.");
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			responseData.put("msg", "다시 로그인 해주세요.");
		}
		return responseData;
	}
	
	@PostMapping("logout")
	public Map<String, String> logout (HttpServletRequest request) {
		Map<String, String> responseData = new HashMap();
		try {
			int userId = (int) request.getAttribute("userId");
			memberService.logout(userId);
			responseData.put("msg", "로그아웃에 성공했습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			responseData.put("msg", "로그아웃에 실패했습니다.");
		}
		return responseData; 
	}
}
