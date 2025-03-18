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
	public Map<String, String> insertMember(@Valid @RequestBody Member m) {
	    Map<String, String> responseData = new HashMap<>();

	    try {
	        memberService.insertMember(m);
	        responseData.put("msg", "회원가입을 축하합니다.");
	    } catch (Exception e) {
	        String errorMsg = e.getMessage();

	        if (errorMsg.contains("이미 가입된 이메일")) {
	            responseData.put("msg", "이미 가입된 이메일입니다. 로그인을 진행해 주세요.");
	        } else if (errorMsg.contains("유효하지 않은 이메일 형식")) {
	            responseData.put("msg", "이메일 형식이 올바르지 않습니다.");
	        } else if (errorMsg.contains("비밀번호는 8자리 이상")) {
	            responseData.put("msg", "비밀번호는 8자리 이상이며, 숫자와 특수문자를 포함해야 합니다.");
	        } else {
	            responseData.put("msg", "회원가입 중 오류가 발생했습니다.");
	        }
	    }

	    return responseData;
	}
	
	
	@PostMapping("tokenLogin")
	public Map<String, Object> tokenLogin(@RequestBody Member m) {
	    Map<String, Object> responseData = new HashMap<>();

	    try {
	        Login loginInfo = memberService.tokenLogin(m);

	        if (loginInfo != null && loginInfo.getUserName() != null && loginInfo.getToken() != null) {
	            responseData.put("username", loginInfo.getUserName());
	            responseData.put("Authorization", loginInfo.getToken());
	        } else {
	            responseData.put("msg", "다시 로그인 해주세요.");
	        }

	    } catch (Exception e) {
	        String errorMsg = e.getMessage();
	        
	        // 예외 메시지가 null이면 기본 메시지를 설정
	        if (errorMsg == null) {
	            errorMsg = "알 수 없는 오류가 발생했습니다.";
	        }

	        // 예외 메시지 가공 후 반환
	        if (errorMsg.contains("존재하지 않는 이메일")) {
	        	responseData.put("msg", "존재하지 않는 회원입니다. 회원가입을 먼저 해주세요.");
	            responseData.put("redirect", "register.html"); // 회원가입 페이지로 이동
	        } else if (errorMsg.contains("로그인 횟수 초과로 계정이 잠겼습니다")) {
	            responseData.put("msg", "로그인 시도 횟수를 초과하여 계정이 잠겼습니다. 잠시 후 다시 시도해주세요.");
	        } else if (errorMsg.contains("이메일 또는 비밀번호가 올바르지 않습니다")) {
	            responseData.put("msg", "이메일 또는 비밀번호가 올바르지 않습니다.");
	        } else {
	            responseData.put("msg", "로그인 중 오류가 발생했습니다.");
	        }
	    }

	    return responseData;
	}

	@PostMapping("logout")
	public Map<String, String> logout (HttpServletRequest request) {
		Map<String, String> responseData = new HashMap();
		try {
			Integer userId = (int) request.getAttribute("userId");
			
	        if (userId == null) {
	            responseData.put("msg", "세션이 만료되었습니다. 다시 로그인 해주세요.");
	            return responseData;
	        }
			
			memberService.logout(userId);
			responseData.put("msg", "로그아웃에 성공했습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			responseData.put("msg", "로그아웃에 실패했습니다.");
		}
		return responseData; 
	}
}
