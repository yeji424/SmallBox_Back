package com.movie.smallbox.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Reservation {

	private int reservationId;
    private int userId;
    private String movieTitle;
    private String theaterName;
    private int movieId; // movieId 추가

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private String scheduleTime;

    private List<String> seatNumbers;
    private Date bookingTime;

    // DB 조회 결과를 받을 임시 필드 (테이블에 없어도 됨)
    private String seatNumbersStr;

    public Reservation() {}

    // 생성자에 movieId 추가
    public Reservation(int reservationId, int userId, String movieTitle, String theaterName, int movieId, String scheduleTime,
                       List<String> seatNumbers, Date bookingTime) {
        this.reservationId = reservationId;
        this.userId = userId;
        this.movieTitle = movieTitle;
        this.theaterName = theaterName;
        this.movieId = movieId;  // movieId 추가
        this.scheduleTime = scheduleTime;
        this.seatNumbers = seatNumbers;
        this.bookingTime = bookingTime;
    }
    
    //getter, setter, toString에 movieId 추가.

	public int getMovieId() {
		return movieId;
	}

	public void setMovieId(int movieId) {
		this.movieId = movieId;
	}
	
	@Override
	public String toString() {
		return "Reservation [reservationId=" + reservationId + ", userId=" + userId + ", movieTitle=" + movieTitle
				+ ", theaterName=" + theaterName + ", movieId=" + movieId + ", scheduleTime=" + scheduleTime
				+ ", seatNumbers=" + seatNumbers + ", bookingTime=" + bookingTime + ", seatNumbersStr="
				+ seatNumbersStr + "]";
	}

	public int getReservationId() {
		return reservationId;
	}

	public void setReservationId(int reservationId) {
		this.reservationId = reservationId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getMovieTitle() {
		return movieTitle;
	}

	public void setMovieTitle(String movieTitle) {
		this.movieTitle = movieTitle;
	}

	public String getTheaterName() {
		return theaterName;
	}

	public void setTheaterName(String theaterName) {
		this.theaterName = theaterName;
	}

	public String getScheduleTime() {
		return scheduleTime;
	}

	public void setScheduleTime(String scheduleTime) {
		this.scheduleTime = scheduleTime;
	}

	public List<String> getSeatNumbers() {
		return seatNumbers;
	}

	public void setSeatNumbers(List<String> seatNumbers) {
		this.seatNumbers = seatNumbers;
	}

	public Date getBookingTime() {
		return bookingTime;
	}

	public void setBookingTime(Date bookingTime) {
		this.bookingTime = bookingTime;
	}

	public String getSeatNumbersStr() {
		return seatNumbersStr;
	}

	public void setSeatNumbersStr(String seatNumbersStr) {
		this.seatNumbersStr = seatNumbersStr;
	}
}