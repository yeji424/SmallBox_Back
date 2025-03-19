package com.movie.smallbox.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.movie.smallbox.dto.Reservation;
import com.movie.smallbox.service.MemberService;
import com.movie.smallbox.service.ReservationService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin("http://127.0.0.1:5500/")
public class ReservationController {

	@Autowired
	ReservationService reservationService;

	@Autowired
	MemberService memberService;
	
	@PostMapping("reservation")
	public Map<String, String> reservation(HttpServletRequest request, @RequestBody Reservation reservation){
		Map<String, String> responseData = new HashMap<>();
		/* 이제 필터에서 다 해줍니다 ^^
	    if (authorization == null || authorization.trim().isEmpty()) {
	        responseData.put("msg", "로그인 먼저 해주세요.");
	        return responseData;
	    }
		*/
		try {
			int userId = (int) request.getAttribute("userId"); // request에서 userId 가져오기
			// 이러면 해커가 조작 못함
			reservation.setUserId(userId);
			
			// 좌석 선택 안햇을 때 : null값 체크
	        if (reservation.getSeatNumbers() == null || reservation.getSeatNumbers().isEmpty()) {
	            responseData.put("msg", "좌석을 선택해주세요.");
	            return responseData;
	        }
			
			// 예매 로직
			// 잠시 확인용 보안때문에 나중에 바꿀거임
            String result = reservationService.reservation(reservation);
            responseData.put("msg", result);
		} catch (Exception e) {
            e.printStackTrace();
            responseData.put("msg", "예매 중 오류가 발생했습니다.");
		}
		return responseData;	
	}
	
	@PostMapping("myReservation")
	public Map<String, Object> getReservation(HttpServletRequest request){
		Map<String, Object> responseData = new HashMap<>();
		
		try {
			int userId = (int) request.getAttribute("userId");
            List<Reservation> reservations = reservationService.getReservation(userId);
            responseData.put("reservations", reservations);
		} catch (Exception e) {
            e.printStackTrace();
            responseData.put("msg", "예매 내역 조회 중 오류가 발생했습니다.");
		}
		return responseData;
	}
	
	@PostMapping("cancelReservation")
	public Map<String, Object> cancelReservation(HttpServletRequest request, @RequestBody Map<String, Integer> requestData) {
	    Map<String, Object> responseData = new HashMap<>();
	    
	    try {
	        int userId = (int) request.getAttribute("userId"); // 세션에서 사용자 ID 가져오기
	        int reservationId = requestData.get("reservationId"); // 요청 데이터에서 예약 ID 가져오기
	        
	        // 예약 취소 서비스 호출
	        Map<String, Object> result = reservationService.cancelReservation(reservationId, userId);
	        
	        return result; // 서비스에서 반환한 결과를 그대로 클라이언트에 전달
	    } catch (Exception e) {
	        e.printStackTrace();
	        responseData.put("success", false);
	        responseData.put("message", "예약 취소 중 오류가 발생했습니다.");
	        return responseData;
	    }
	}
	
	@PostMapping("bookedSeats")
	public Map<String, Object> getBookedSeats(@RequestBody Map<String, String> requestData) {
	    Map<String, Object> responseData = new HashMap<>();

	    try {
	        // 요청 데이터에서 극장명과 상영 시간 추출
	        String theaterName = requestData.get("theaterName");
	        String scheduleTime = requestData.get("scheduleTime");

	        Map<String, Object> params = new HashMap<>();
	        params.put("theaterName", theaterName);
	        params.put("scheduleTime", scheduleTime);

	        // 예약된 좌석 목록 가져오기
	        List<String> bookedSeats = reservationService.getBookedSeats(params);
	        responseData.put("bookedSeats", bookedSeats);
	    } catch (Exception e) {
	        e.printStackTrace();
	        responseData.put("msg", "예매된 좌석 조회 중 오류 발생");
	    }

	    return responseData;
	}

}
