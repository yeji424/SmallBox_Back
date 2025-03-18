package com.movie.smallbox;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 API에 CORS 적용
            .allowedOrigins("http://127.0.0.1:5500", "http://localhost:5500") // localhost:5500 추가
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 그먄 명시해둠
            .allowedHeaders("*") // 모든 헤더 허용
            .exposedHeaders("Authorization") // 클라이언트 Authorization 읽을 수 있도록 설정
            .allowCredentials(true); // 쿠키 및 인증정보 허용
        
        // 참고로, myConfig 여기서는 @RestController가 처리하는 요청에만 적용됨
        // Filter를 쓰는 우리는 AuthFilter에 직접 추가해줘야함
        // 왜? 로그아웃요청은 AuthFilter.java에서 먼저 가로채기 때문에 MyConfig.java의 CORS 설정이 적용되지 않음
    }
}