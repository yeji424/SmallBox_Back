package com.movie.smallbox.dto;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

public class Reservation {

	private int reservationId;
    private int userId;
    private String movieTitle;
    private String theaterName;
    // gpt의 힘이란..
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private String scheduleTime;
    private String seatNumber;
    private Date bookingTime;
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
	public String getSeatNumber() {
		return seatNumber;
	}
	public void setSeatNumber(String seatNumber) {
		this.seatNumber = seatNumber;
	}
	public Date getBookingTime() {
		return bookingTime;
	}
	public void setBookingTime(Date bookingTime) {
		this.bookingTime = bookingTime;
	}
	public Reservation() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Reservation(int reservationId, int userId, String movieTitle, String theaterName, String scheduleTime,
			String seatNumber, Date bookingTime) {
		super();
		this.reservationId = reservationId;
		this.userId = userId;
		this.movieTitle = movieTitle;
		this.theaterName = theaterName;
		this.scheduleTime = scheduleTime;
		this.seatNumber = seatNumber;
		this.bookingTime = bookingTime;
	}
	@Override
	public String toString() {
		return "Reservation [reservationId=" + reservationId + ", userId=" + userId + ", movieTitle=" + movieTitle
				+ ", theaterName=" + theaterName + ", scheduleTime=" + scheduleTime + ", seatNumber=" + seatNumber
				+ ", bookingTime=" + bookingTime + "]";
	}
}
