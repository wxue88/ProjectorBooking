package com.assignment.projectorbooking.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.assignment.projectorbooking.exception.ResourceNotFoundException;
import com.assignment.projectorbooking.model.Booking;
import com.assignment.projectorbooking.repository.BookingRepository;

@Service
public class BookingServiceImpl implements BookingService {
	@Autowired
	private BookingRepository repository;
	
	@Override
	public Booking createBooking(Booking booking) {
		return repository.save(booking);
	}
	
	@Override
	public Booking getBooking(int id) throws ResourceNotFoundException {
		Booking booking = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Booking", "id", id));
		return booking;
	}
	
	@Override
	public List<Booking> getBookings() {
		return repository.findAll();
	}
	
	@Override
	public List<Booking> getBookingsByRequestedSlots(LocalDateTime startTime, LocalDateTime endTime) {
		return repository.getBookingsByRequestedSlots(startTime, endTime);
	}

	@Override
	public void deleteBooking(int id) throws ResourceNotFoundException {
		Booking booking = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Booking", "id", id));
		repository.delete(booking);
	}	
	
	@Override
	public void cleanUpTables() {
		repository.deleteAllInBatch();
	}

}
