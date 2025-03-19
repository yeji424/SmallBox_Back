package com.movie.smallbox.service;

import java.util.ArrayList;
import java.util.Arrays;
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

    public String reservation(Reservation reservation) throws Exception {
        reservation.setBookingTime(new Date());
        List<String> seatNumbers = reservation.getSeatNumbers();

        List<Reservation> reservations = new ArrayList<>();
         
        for (String seat : seatNumbers) {
            Map<String, Object> bookingInfo = new HashMap<>();
            bookingInfo.put("theaterName", reservation.getTheaterName());
            bookingInfo.put("scheduleTime", reservation.getScheduleTime());
            bookingInfo.put("seatNumber", seat);

            // 이미 예약된 좌석인지 확인
            boolean isSeatBooked = reservationDao.isSeatBooked(bookingInfo) > 0;
            if (isSeatBooked) {
                return "이선좌"; // 예약 실패 (이미 예약된 좌석 존재)
            }

            // 예약 객체를 생성하여 리스트에 추가
            Reservation newReservation = new Reservation();
            newReservation.setUserId(reservation.getUserId());
            newReservation.setMovieTitle(reservation.getMovieTitle());
            newReservation.setTheaterName(reservation.getTheaterName());
            newReservation.setScheduleTime(reservation.getScheduleTime());
            newReservation.setSeatNumbers(List.of(seat)); // 좌석 한 개씩 저장
            newReservation.setBookingTime(new Date());
            newReservation.setMovieId(reservation.getMovieId()); // movieId 설정
            reservations.add(newReservation);
        }

        // 모든 좌석이 예약 가능하면 insert 진행
        reservationDao.insertReservation(reservations);
        return "예매 성공";
    }
   
    // 요기 수정했어요 DB에서 받아온 문자열 쉼표로 분할해서 리스트 타입으로 변환!!
    public List<Reservation> getReservation(int userId) throws Exception {
        List<Reservation> reservations = reservationDao.getReservation(userId);

        for (Reservation reservation : reservations) {
            if (reservation.getSeatNumbersStr() == null || reservation.getSeatNumbersStr().isEmpty()) {
                reservation.setSeatNumbers(new ArrayList<>());
            } else {
                reservation.setSeatNumbers(
                    Arrays.asList(reservation.getSeatNumbersStr().split(","))
                );
            }
        }
        return reservations;
    }

   
   public List<String> getBookedSeats(Map<String, Object> bookingInfo) throws Exception {
       return reservationDao.getBookedSeats(bookingInfo);
   }
}