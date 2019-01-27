package com.assignment.projectorbooking.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.assignment.projectorbooking.model.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
	@Query(value = "SELECT * FROM BOOKING WHERE end_time > ?1 AND start_time < ?2", nativeQuery = true)
	public List<Booking> getBookingsByRequestedSlots(LocalDateTime startTime, LocalDateTime endTime);
	//@Query(value = "SELECT * FROM PROJ_BOOKING WHERE (start_time >= ?1 AND start_time < ?2) OR (end_time > ?1 AND end_time <= ?2", nativeQuery = true)
	//public List<Booking> getBookingsByRequestedSlots(LocalDateTime startTime, LocalDateTime endTime);

}
