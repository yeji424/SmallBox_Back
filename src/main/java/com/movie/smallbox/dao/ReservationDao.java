package com.movie.smallbox.dao;

import java.util.*;

import org.apache.ibatis.annotations.Mapper;

import com.movie.smallbox.dto.Reservation;

@Mapper
public interface ReservationDao {
    void insertReservation(List<Reservation> reservations) throws Exception;
    
    // 예약 취소를 위한 메서드 추가 -> 그룹화함 
    int cancelGroupReservation(Map<String, Object> params);
    
    // 예약 ID로 예약 정보 조회
    Reservation getReservationById(int reservationId) throws Exception;
    
    List<Reservation> getReservation(int userId) throws Exception;	
	
    // Mybatis는 boolean 제공 안함 service에서 boolean으로 처리 예정
	int isSeatBooked(Map<String, Object> bookingInfo) throws Exception;
	
	// 이선좌 표시 리스트
	List<String> getBookedSeats(Map<String, Object> bookingInfo) throws Exception;

}
