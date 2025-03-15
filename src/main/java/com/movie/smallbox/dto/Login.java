package com.movie.smallbox.dto;

import java.util.Date;

public class Login {
    private int userId;
    private String token;
    private String userName;
    private Date loginTime;
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Date getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}
	public Login() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Login(int userId, String token, String userName, Date loginTime) {
		super();
		this.userId = userId;
		this.token = token;
		this.userName = userName;
		this.loginTime = loginTime;
	}
	@Override
	public String toString() {
		return "Login [userId=" + userId + ", token=" + token + ", userName=" + userName + ", loginTime=" + loginTime
				+ "]";
	}
}
    
    
