package com.movie.smallbox.dao;

import java.util.*;

import org.apache.ibatis.annotations.Mapper;

import com.movie.smallbox.dto.Reservation;

@Mapper
public interface ReservationDao {
	void insertReservation(Reservation reservation) throws Exception;
	List<Reservation> getReservation(int userId) throws Exception;
	
	// Mybatis는 boolean 제공 안함 service에서 boolean으로 처리 예정
	int isSeatBooked(Map<String, Object> bookingInfo) throws Exception;
}
