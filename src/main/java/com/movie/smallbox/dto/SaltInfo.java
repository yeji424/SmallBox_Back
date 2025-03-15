package com.movie.smallbox.dto;

public class SaltInfo {
	private int userId;
	private String salt;
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public SaltInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public SaltInfo(int userId, String salt) {
		super();
		this.userId = userId;
		this.salt = salt;
	}
	@Override
	public String toString() {
		return "SaltInfo [userId=" + userId + ", salt=" + salt + "]";
	}
}
