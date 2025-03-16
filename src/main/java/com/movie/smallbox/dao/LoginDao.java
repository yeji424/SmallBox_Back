package com.movie.smallbox.dao;

import java.util.Date;

import org.apache.ibatis.annotations.Mapper;

import com.movie.smallbox.dto.Login;

@Mapper
public interface LoginDao {
    public void insertToken(Login login) throws Exception; // userId 사용
    public void deleteToken(int userId) throws Exception; // userId 기반 삭제
    
    public int getUserIdByToken(String token) throws Exception; // userId 반환
    
    public Date getLoginTimeByToken(String token) throws Exception;
    public void updateLoginTime(String token) throws Exception;

    public void updateLoginAttempt(String email) throws Exception;
    public void resetLoginAttempt(String email) throws Exception;
    public Integer getLoginAttemptCount(String email) throws Exception;
    public void lockAccount(String email) throws Exception;
    public Integer isAccountLocked(String email) throws Exception; 
    public Date getLastAttempt(String email) throws Exception;
}