package com.movie.smallbox.dao;

import org.apache.ibatis.annotations.Mapper;

import com.movie.smallbox.dto.SaltInfo;

@Mapper
public interface SaltDao {
	public void insertSalt(SaltInfo saltInfo) throws Exception;
	public SaltInfo selectSalt(int userId) throws Exception;
}
