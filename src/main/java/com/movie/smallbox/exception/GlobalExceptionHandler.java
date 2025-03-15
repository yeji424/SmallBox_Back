package com.movie.smallbox.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;

// 전역 예외 처리
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 유효성 검사 실패 예외 처리
     * - @Valid, @Validated 사용한 요청에서 검증 실패 시 호출
     * - 입력 필드와 에러 메시지만 반환 (서버 내부 정보 보호)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
    	List<String> errorMessages = new ArrayList<>();
        
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
        	errorMessages.add(error.getDefaultMessage()); // 에러 메세지만 저장
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages);
    }
}

/**
 * 보안 관련 사항
 * - 불필요한 stack trace, timestamp 정보 노출 방지 
 * - Spring 기본 에러 응답 숨김 : 해커가 내부 구조 모르게 함
 */