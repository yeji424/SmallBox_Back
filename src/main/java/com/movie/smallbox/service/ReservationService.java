package com.movie.smallbox.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.movie.smallbox.dao.ReservationDao;
import com.movie.smallbox.dto.Reservation;

@Service
public class ReservationService {

	@Autowired
	ReservationDao reservationDao;
	
	public String reservation(Reservation reservation) throws Exception{
		reservation.setBookingTime(new Date());
		
		Map<String, Object> bookingInfo = new HashMap<>();
		bookingInfo.put("theaterName", reservation.getTheaterName());
		bookingInfo.put("scheduleTime", reservation.getScheduleTime());
		bookingInfo.put("seatNumber", reservation.getSeatNumber());
		
		// boolean 변환
		boolean isSeatBooked = reservationDao.isSeatBooked(bookingInfo) > 0;
		if(isSeatBooked) {
			return "이선좌";
		}
		
		reservationDao.insertReservation(reservation);
		return "예매 성공";
	}
	
	public List<Reservation> getReservation(int userId) throws Exception{
		return reservationDao.getReservation(userId);
	}
}
