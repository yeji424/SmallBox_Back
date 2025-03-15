package com.movie.smallbox.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class Member {
	private int userId;
	
	@Email(message = "올바른 이메일 형식이 아닙니다.")
	@NotBlank(message = "이메일은 필수 입력 항목입니다.")
	private String email;
	
    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    // @JsonIgnore // JSON 응답 시 비밀번호 숨기기 (보안 강화)
    private String pwd;
	
    @NotBlank(message = "사용자 이름은 필수 입력 항목입니다.")
	private String userName;
    
	private Date registDate;
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Date getRegistDate() {
		return registDate;
	}
	public void setRegistDate(Date registDate) {
		this.registDate = registDate;
	}
	public Member() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Member(int userId, String email, String pwd, String userName, Date registDate) {
		super();
		this.userId = userId;
		this.email = email;
		this.pwd = pwd;
		this.userName = userName;
		this.registDate = registDate;
	}
	@Override
	public String toString() {
		return "Member [userId=" + userId + ", email=" + email + ", pwd=" + pwd + ", userName=" + userName
				+ ", registDate=" + registDate + "]";
	}
}
