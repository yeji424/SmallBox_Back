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

        // CORS 응답 헤더 추가
        httpResponse.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:5500");
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        httpResponse.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
        httpResponse.setHeader("Access-Control-Expose-Headers", "Authorization");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");

        // CORS preflight (OPTIONS 요청) 처리
        if (httpRequest.getMethod().equalsIgnoreCase("OPTIONS")) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return;
        }

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

        // 로그인 검증이 필요한 API만 필터 적용
        // 여기에 체크토큰 추가했슴당
        if (requestURI.startsWith("/reservation") || requestURI.startsWith("/myReservation") || requestURI.startsWith("/logout") || requestURI.startsWith("/checkSession")) {

            // System.out.println("**** AuthFilter: 요청 URI : " + requestURI);
            // System.out.println("**** AuthFilter: 받은 토큰 : " + token);

            // 토큰이 없으면 로그인 요청 유도
            if (token == null || token.trim().isEmpty()) {
                // System.out.println("**** AuthFilter: 토큰 없음, 401 반환");
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpResponse.getWriter().write("{\"msg\":\"로그인 먼저 해주세요.\"}");
                return;
            }

            try {
                // 토큰으로 userId 조회 후 컨트롤러에서 사용 가능하도록 저장
                int userId = memberService.getUserIdFromToken(token);
                // System.out.println("**** AuthFilter: userId 조회 : " + userId);

                // 로그인 시간 가져오기
                Date lastLoginTime = memberService.getLoginTimeByToken(token);
                // System.out.println("**** AuthFilter: 로그인 시간 : " + lastLoginTime);

                
                // 로그인 기록 없을 때 (사실 분리되는게 맞는 거 같은데 없앴다가 망가질까 무서워서 놔둡니다)
                if (lastLoginTime == null) {
                    // System.out.println("**** AuthFilter: loginTIme 삭제됨, 세션 만료 처리");
                    memberService.logout(userId);
                    httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    httpResponse.getWriter().write("{\"msg\":\"세션이 만료되었습니다. 다시 로그인해주세요.\"}");
                    return;
                }

                // 로그인 기록 만료되었을 때
                // 현재 시간과 비교하여 30분 이상 지났는지 확인
                long timeDiff = (new Date().getTime() - lastLoginTime.getTime()) / 1000; // 초 단위
                if (timeDiff > 1800) { // 30분 초과
                    // System.out.println("**** AuthFilter: 세션 만료, 401 반환");
                    memberService.logout(userId);
                    httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    httpResponse.getWriter().write("{\"msg\":\"세션이 만료되었습니다. 다시 로그인해주세요.\"}");
                    return;
                }

                
                // 30분이 지나지 않았을 때만 loginTime 갱신
                memberService.updateLoginTime(token);
                // System.out.println("**** AuthFilter: LoginTime 업데이트 완료");

                // 컨트롤러에서 userId를 사용할 수 있도록 설정
                httpRequest.setAttribute("userId", userId);

            } catch (Exception e) {
                e.printStackTrace();
                // System.out.println("**** AuthFilter: 예외 발생, 401 반환");
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpResponse.getWriter().write("{\"msg\":\"서버 오류 발생.\"}");
                return;
            }
        }

        // 필터 통과 (정상 요청)
        chain.doFilter(request, response);
    }
}
