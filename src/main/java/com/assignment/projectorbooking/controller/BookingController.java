package com.assignment.projectorbooking.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.assignment.projectorbooking.exception.ResourceNotFoundException;
import com.assignment.projectorbooking.model.Booking;
import com.assignment.projectorbooking.model.Projector;
import com.assignment.projectorbooking.service.BookingService;
import com.assignment.projectorbooking.service.ProjectorService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/v1/bookings")
@Api(value="Projector Booking")
public class BookingController {
	@Autowired
	private BookingService bookingService;
	
	@Autowired 
	private ProjectorService projectorService;
	
	private static final String NO_PROJECTOR = "There is no projector in the system.";
	
	private static final String PROJECTOR_NOT_AVAILABLE = "There is no projector available for the requested slot.";
	
	private static final String PROPOSE_NEW_START_TIME = "Propose new start time:"; 
	
	@ApiOperation(value = "Request a projector booking")
	@PostMapping
	public ResponseEntity<Booking> createBooking(@Valid @RequestBody Booking booking) throws ResourceNotFoundException {
		return new ResponseEntity<Booking>(bookingService.createBooking(booking), HttpStatus.OK);
	}
	
	@ApiOperation(value = "Get a projector booking with an ID")
	@GetMapping("/{id}")
	public ResponseEntity<Booking> getBooking(@PathVariable(value="id") int id) throws ResourceNotFoundException {
		Booking booking = bookingService.getBooking(id);
		return new ResponseEntity<Booking>(booking, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Get a list of projector bookings")
	@GetMapping
	public ResponseEntity<List<Booking>> getBookings() {
		List<Booking> bookings = bookingService.getBookings();
		return new ResponseEntity<List<Booking>>(bookings, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Get available projector with requested time slot")
	@GetMapping("/projectors")
	public ResponseEntity<String> getAvailableProjectorByRequestedSlots(
			@RequestParam("start_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime, 
			@RequestParam("end_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime, 
			@RequestParam("advice") Optional<Boolean> advice) {
		System.out.println("startTime=" + startTime + ", endTime=" + endTime + ", advice=" + advice);
		List<Booking> bookings = bookingService.getBookingsByRequestedSlots(startTime, endTime);
		if (bookings.isEmpty()) {
			List<Projector> projectors = projectorService.getProjectors();
			if (projectors.isEmpty()) {
				return buildResponse(NO_PROJECTOR);
			}
			else {
				System.out.println(((Projector)projectors.get(0)).getName());
				return buildResponse(((Projector)projectors.get(0)).getName());
			}
		}
		else {
			String availableProjectorName = projectorService.getAvailableProjectorName(getProjectorIds(bookings));
		
			if (StringUtils.isEmpty(availableProjectorName)) {
				boolean adviceFlag = advice.isPresent() ? advice.get() : false;
				if (adviceFlag) {
					return buildResponse(getProposedNewStartTime(bookings, startTime));
				} else {
					return buildResponse(PROJECTOR_NOT_AVAILABLE);
				}	
			}
			else {
				return buildResponse(availableProjectorName);
			}
		}
	}
	
	private ResponseEntity<String> buildResponse(String message) {
		return new ResponseEntity<>(message, HttpStatus.OK);
	}
	
	private String getProjectorIds(List<Booking> bookings) {
		StringBuilder projectorIds = new StringBuilder();
		String prefix = "";
		for (Booking booking : bookings) {
			projectorIds.append(prefix);
			prefix = ",";
			projectorIds.append(booking.getProjector().getId());			
		}
		return projectorIds.toString();
	}
	
	private String getProposedNewStartTime(List<Booking> bookings, LocalDateTime startTime) {
		Map<LocalDateTime, Long> map = new HashMap<>();
		long min = Long.MAX_VALUE;
		for (Booking booking : bookings) {
			Duration diff = Duration.between(startTime, booking.getEndTime());
			map.put(booking.getEndTime(), diff.toMillis());
			System.out.println("put projectorId=" + booking.getProjector().getId() + ",end time=" + booking.getEndTime() + ", start time=" + startTime + ", min=" + diff.toMillis());
			min = Math.min(min, Math.abs(diff.toMillis()));		
		}

		for (Map.Entry<LocalDateTime, Long> entry : map.entrySet()) {
			if (entry.getValue() == min) {
				return PROPOSE_NEW_START_TIME + entry.getKey();
			}
		}
		return PROJECTOR_NOT_AVAILABLE;
		
	}
	
	@ApiOperation(value = "Cancel a projector booking with an ID")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteBooking(@PathVariable(value="id") int id) throws ResourceNotFoundException {
		bookingService.deleteBooking(id);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
	
}
