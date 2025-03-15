package com.movie.smallbox.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.movie.smallbox.service.MemberService;

@Component
public class AuthFilter implements Filter {

	@Autowired
	MemberService memberService;
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
	        throws IOException, ServletException {

	    HttpServletRequest httpRequest = (HttpServletRequest) request;
	    HttpServletResponse httpResponse = (HttpServletResponse) response;

	    // 응답 JSON 한글 깨짐 방지
	    httpResponse.setCharacterEncoding(StandardCharsets.UTF_8.name());
	    httpResponse.setContentType("application/json");

	    // 요청 URL 가져오기
	    String requestURI = httpRequest.getRequestURI();
	    String token = httpRequest.getHeader("Authorization");

	    // 회원가입, 로그인 요청은 필터 제외
	    if (requestURI.startsWith("/insertMember") || requestURI.startsWith("/tokenLogin")) {
	        chain.doFilter(request, response);
	        return;
	    }

	    // 로그인 검증이 필요한 API만 필터 적용 (직접 설정하는 방법밖엔 없는건가...싶음)
	    if (requestURI.startsWith("/reservation") || requestURI.startsWith("/myReservation") || requestURI.startsWith("/logout")) {

	        // 토큰이 없으면 로그인 요청 유도
	        if (token == null || token.trim().isEmpty()) {
	            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	            httpResponse.getWriter().write("{\"msg\":\"로그인 먼저 해주세요.\"}");
	            return;
	        }

	        try {
	        	// 토큰으로 userId 조회 후 컨트롤러에서 사용 가능하도록 저장
	            // 이거도 보안 때문!! 지금까지 컨트롤러에서 auth 헤더에서 가져왔잖아요
	            // 근데 그렇게되면 헤커가 salt값 맞춰서 클라에서 auth헤더 조작하면 지금까지 한 보안이 무슨 소용이겠습니까
	            
	            int userId = memberService.getUserIdFromToken(token);

	            // 로그인 시간 가져오기
	            Date lastLoginTime = memberService.getLoginTimeByToken(token);
	            if (lastLoginTime == null) {
	            	memberService.logout(userId);
	                
	            	httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	                httpResponse.getWriter().write("{\"msg\":\"세션이 만료되었습니다. 다시 로그인해주세요.\"}");
	                return;
	            }

	            // 현재 시간과 비교하여 30분 이상 지났는지 확인
	            long timeDiff = (new Date().getTime() - lastLoginTime.getTime()) / 1000; // 초 단위
	            if (timeDiff > 1800) { // 30분 초과
	            	memberService.logout(userId);
	            	
	            	httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	                httpResponse.getWriter().write("{\"msg\":\"세션이 만료되었습니다. 다시 로그인해주세요.\"}");
	                return;
	            }

	            // 30분이 지나지 않았을 때만 loginTime 갱신
	            if (timeDiff <= 1800) { 
	                memberService.updateLoginTime(token);
	            }

	            // 여기서 userId 세팅해놓고 컨트롤러에서 갖다 쓸겁니당
	            httpRequest.setAttribute("userId", userId);

	        } catch (Exception e) {
	            e.printStackTrace();
	            httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	            httpResponse.getWriter().write("{\"msg\":\"서버 오류 발생.\"}");
	            return;
	        }
	    }

	    // 필터 통과 (정상 요청)
	    chain.doFilter(request, response);
	}
}