package com.movie.smallbox.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
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
}
