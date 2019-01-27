package com.assignment.projectorbooking.service;

import java.time.LocalDateTime;
import java.util.List;

import com.assignment.projectorbooking.exception.ResourceNotFoundException;
import com.assignment.projectorbooking.model.Booking;

public interface BookingService {
	
	public Booking createBooking(Booking booking);
	
	public Booking getBooking(int id) throws ResourceNotFoundException;
	
	public List<Booking> getBookings();
	
	public List<Booking> getBookingsByRequestedSlots(LocalDateTime startTime, LocalDateTime endTime);
	
	public void deleteBooking(int id) throws ResourceNotFoundException;
	
	public void cleanUpTables();
}
