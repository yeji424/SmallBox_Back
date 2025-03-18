package com.movie.smallbox.dao;

import org.apache.ibatis.annotations.Mapper;

import com.movie.smallbox.dto.Member;

@Mapper
public interface MemberDao {
	public void insertMember(Member m) throws Exception;
	public void updateMember(Member m) throws Exception;
	public void deleteMember(int userId) throws Exception;
	
	public Member tokenLogin(Member m) throws Exception;

	public Integer getUserIdByEmail(String email) throws Exception;
}