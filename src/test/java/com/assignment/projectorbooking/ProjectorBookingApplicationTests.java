package com.assignment.projectorbooking;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.assignment.projectorbooking.ProjectorBookingApplication;
import com.assignment.projectorbooking.model.Booking;
import com.assignment.projectorbooking.model.Projector;
import com.assignment.projectorbooking.model.Team;
import com.assignment.projectorbooking.service.BookingService;
import com.assignment.projectorbooking.service.ProjectorService;
import com.assignment.projectorbooking.service.TeamService;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProjectorBookingApplication.class,  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProjectorBookingApplicationTests {
	
	private static final String PROJECTOR1 = "PROJECTOR_1";
	
	private static final String TEAM1 = "TEAM_1";
	
	private LocalDateTime BOOKING1_START_TIME;
	
	private LocalDateTime BOOKING1_END_TIME;
	
	private static final String BOOKING1_START_TIME_STR = "2019-01-20T10:00:00Z";
	
	private static final String BOOKING1_END_TIME_STR = "2019-01-20T12:00:00Z";
	
	private static final String REQUESTED_START_TIME_1_STR = "2019-01-20T08:00:00Z";
	
	private static final String REQUESTED_END_TIME_1_STR = "2019-01-20T09:00:00Z";
	
	private static final String REQUESTED_START_TIME_2_STR = "2019-01-20T10:30:00Z";
	
	private static final String REQUESTED_END_TIME_2_STR = "2019-01-20T11:30:00Z";
	
	@Autowired
	private ProjectorService projectorService;
	
	@Autowired
	private TeamService teamService;
	
	@Autowired
	private BookingService bookingService;
	
	@Test
	public void contextLoads() {
	}
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@LocalServerPort
	private int serverPort;
	
	private String getUrl() {
		return  "http://localhost:" + serverPort;
	}
	
	@Before
	public void setUp() {
		projectorService.cleanUpTables();
		teamService.cleanUpTables();
		bookingService.cleanUpTables();		
	}
		
	private LocalDateTime convertTime(String time) {
		 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
			        .withZone(ZoneId.of("UTC"));
		 return LocalDateTime.parse(time, formatter);
	}
		
	private ResponseEntity<Projector> createProjector1() {
		Projector projector = new Projector();
		projector.setName(PROJECTOR1);
		return (ResponseEntity<Projector>) restTemplate.postForEntity(getUrl() + "/v1/projectors", projector, Projector.class);
	}
	
	private ResponseEntity<Team> createTeam1() {
		Team team = new Team();
		team.setName(TEAM1);
		return (ResponseEntity<Team>) restTemplate.postForEntity(getUrl() + "/v1/teams", team, Team.class);
	}
	
	private ResponseEntity<Booking> createBooking1() {
		//create projector1
		ResponseEntity<Projector> projector1Response = createProjector1();	
		//create team1
		ResponseEntity<Team> team1Response = createTeam1();
		//create booking1 with projector1, team1, BOOKING1_START_TIME,and BOOKING1_END_TIME
		BOOKING1_START_TIME = convertTime(BOOKING1_START_TIME_STR);
		BOOKING1_END_TIME = convertTime(BOOKING1_END_TIME_STR);
		Booking booking1 = new Booking();
		booking1.setProjector(projector1Response.getBody());
		booking1.setTeam(team1Response.getBody());
		booking1.setStartTime(BOOKING1_START_TIME);
		booking1.setEndTime(BOOKING1_END_TIME);
		return (ResponseEntity<Booking>) restTemplate.postForEntity(getUrl() + "/v1/bookings", booking1, Booking.class);
	}
	
	@Test
	public void testA1_CreateProjector() {	
		ResponseEntity<Projector> response = createProjector1();
		assertEquals(PROJECTOR1, response.getBody().getName());
		assertNotNull(response.getBody().getId());
	}
	
	@Test
	public void testA2_GetProjectorById() {
		ResponseEntity<Projector> response = createProjector1();
		Projector projector = restTemplate.getForObject(getUrl() + "/v1/projectors/" + response.getBody().getId(), Projector.class);
		assertEquals(PROJECTOR1, projector.getName());
	}
	
	@Test
	public void testA3_GetProjectors() {
		createProjector1();
		ResponseEntity<List<Projector>> response = restTemplate.exchange(
				getUrl() + "/v1/projectors",
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<List<Projector>>() {});
		List<Projector> projectors = response.getBody();
		assertEquals(1, projectors.size());
	}
	
	@Test
	public void testA4_DeleteProjector_NoRowInBookingTable() {
		ResponseEntity<Projector> postResponse = createProjector1();
		restTemplate.delete(getUrl() + "/v1/projectors/" + postResponse.getBody().getId());
		//verify the row is deleted from projector table
		ResponseEntity<List<Projector>> response = restTemplate.exchange(
				getUrl() + "/v1/projectors",
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<List<Projector>>() {});
		List<Projector> projectors = response.getBody();
		assertEquals(0, projectors.size());
	}
	
	@Test
	public void testA5_DeleteProjector_WithRowInBookingTable() {		
		ResponseEntity<Booking> bookingResponse = createBooking1();
		restTemplate.delete(getUrl() + "/v1/projectors/" + bookingResponse.getBody().getProjector().getId());
		//verify the row is deleted from projector table
		ResponseEntity<List<Projector>> response = restTemplate.exchange(
				getUrl() + "/v1/projectors",
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<List<Projector>>() {});
		List<Projector> projectors = response.getBody();
		assertEquals(0, projectors.size());
		//verify the row is deleted from booking table
		ResponseEntity<List<Booking>> bookingsResponse = restTemplate.exchange(
				getUrl() + "/v1/bookings",
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<List<Booking>>() {});
		List<Booking> bookings = bookingsResponse.getBody();
		assertEquals(0, bookings.size());
	}	
	
	@Test
	public void testB1_CreateTeam() {
		ResponseEntity<Team> postResponse = createTeam1();
		assertEquals(TEAM1, postResponse.getBody().getName());
		assertNotNull(postResponse.getBody().getId());
	}
	
	@Test
	public void testB2_GetTeamById() {
		ResponseEntity<Team> teamPostResponse = createTeam1();
		Team team = restTemplate.getForObject(getUrl() + "/v1/teams/" + teamPostResponse.getBody().getId(), Team.class);
		assertEquals(TEAM1, team.getName());
	}
	
	@Test
	public void testB3_GetTeams() {
		createTeam1();
		ResponseEntity<List<Team>> response = restTemplate.exchange(
				getUrl() + "/v1/teams",
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<List<Team>>() {});
		List<Team> teams = response.getBody();
		assertEquals(1, teams.size());
	}
	
	@Test
	public void testB4_DeleteTeam_NoRowInBookingTable() {
		ResponseEntity<Team> postResponse = createTeam1();
		restTemplate.delete(getUrl() + "/v1/teams/" + postResponse.getBody().getId());
		//verify the row is deleted from booking table
		ResponseEntity<List<Team>> response = restTemplate.exchange(
				getUrl() + "/v1/teams",
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<List<Team>>() {});
		List<Team> teams = response.getBody();
		assertEquals(0, teams.size());
	}
	
	@Test
	public void testB5_DeleteTeam_WithRowInBookingTable() {		
		ResponseEntity<Booking> teamResponse = createBooking1();
		restTemplate.delete(getUrl() + "/v1/teams/" + teamResponse.getBody().getTeam().getId());
		//verify the row is deleted from team table
		ResponseEntity<List<Team>> teamsResponse = restTemplate.exchange(
				getUrl() + "/v1/teams",
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<List<Team>>() {});
		List<Team> teams = teamsResponse.getBody();
		assertEquals(0, teams.size());
		//verify the row is deleted from booking table
		ResponseEntity<List<Booking>> bookingsResponse = restTemplate.exchange(
				getUrl() + "/v1/bookings",
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<List<Booking>>() {});
		List<Booking> bookings = bookingsResponse.getBody();
		assertEquals(0, bookings.size());
	}
	
	@Test
	public void testC1_CreateBooking() {		
		ResponseEntity<Booking> postResponse = createBooking1();
		assertEquals(PROJECTOR1, postResponse.getBody().getProjector().getName());
		assertEquals(TEAM1, postResponse.getBody().getTeam().getName());
		assertEquals(BOOKING1_START_TIME, postResponse.getBody().getStartTime());
		assertEquals(BOOKING1_END_TIME, postResponse.getBody().getEndTime());
	}
	
	@Test
	public void testC2_GetBookingById() {
		ResponseEntity<Booking> postResponse = createBooking1();
		Booking booking = restTemplate.getForObject(getUrl() + "/v1/bookings/" + postResponse.getBody().getId(),  Booking.class);
		assertEquals(PROJECTOR1, booking.getProjector().getName());
		assertEquals(TEAM1, booking.getTeam().getName());
		assertEquals(BOOKING1_START_TIME, booking.getStartTime());
		assertEquals(BOOKING1_END_TIME, booking.getEndTime());
	}
	
	@Test
	public void testC3_GetBookings() {
		createBooking1();
		ResponseEntity<List<Booking>> response = restTemplate.exchange(
				getUrl() + "/v1/bookings",
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<List<Booking>>() {});
		List<Booking> bookings = response.getBody();
		assertEquals(1, bookings.size());
	}
	
	@Test
	public void testC4_DeleteBooking_Not_Impact_Projector_Team_Table() {
		ResponseEntity<Booking> postResponse = createBooking1();
		restTemplate.delete(getUrl() + "/v1/bookings/" + postResponse.getBody().getId());
		//verify the row is deleted from booking table
		ResponseEntity<List<Booking>> response = restTemplate.exchange(
				getUrl() + "/v1/bookings",
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<List<Booking>>() {});
		List<Booking> bookings = response.getBody();
		assertEquals(0, bookings.size());
		//verify the row is not deleted from projector table
		ResponseEntity<List<Projector>> projectorResponse = restTemplate.exchange(
				getUrl() + "/v1/projectors",
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<List<Projector>>() {});
		List<Projector> projectors = projectorResponse.getBody();
		assertEquals(1, projectors.size());
		//verify the row is not deleted from team table
		ResponseEntity<List<Team>> teamResponse = restTemplate.exchange(
				getUrl() + "/v1/teams",
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<List<Team>>() {});
		List<Team> teams = teamResponse.getBody();
		assertEquals(1, teams.size());
	}
	

	@Test
	public void testC5_GetAvailableProjectorByRequestedSlot_FindProjector() {
		//Booking1: projector1, team1,  start time:"2019-01-20T10:00:00Z", end time:"2019-01-20T12:00:00Z"
		//REQUESTED_START_TIME_1_STR:"2019-01-20T08:00:00Z", REQUESTED_END_TIME_1_STR: "2019-01-20T09:00:00Z" -> PROJECTOR1 is available
		createBooking1();
		String checkProjectorAvailability = restTemplate.getForObject(getUrl() + "/v1/bookings/projectors?start_time="+REQUESTED_START_TIME_1_STR+"&end_time="+REQUESTED_END_TIME_1_STR, String.class);
		assertEquals(PROJECTOR1, checkProjectorAvailability);
	}
	
	@Test
	public void testC6_GetAvailableProjectorByRequestedSlot_NoProjectorAvailable_Without_AdviceFlag() {
		//Booking1: projector1, team1,  start time:"2019-01-20T10:00:00Z", end time:"2019-01-20T12:00:00Z"
		//REQUESTED_START_TIME_1_STR:"2019-01-20T10:30:00Z", REQUESTED_END_TIME_1_STR: "2019-01-20T11:30:00Z" -> No projector available
		createBooking1();
		String checkProjectorAvailability = restTemplate.getForObject(getUrl() + "/v1/bookings/projectors?start_time="+REQUESTED_START_TIME_2_STR+"&end_time="+REQUESTED_END_TIME_2_STR, String.class);
		assertEquals("There is no projector available for the requested slot.", checkProjectorAvailability);
	}
	
	@Test
	public void testC7_GetAvailableProjectorByRequestedSlot_NoProjectorAvailable_With_AdviceFlag_FALSE() {
		//Booking1: projector1, team1,  start time:"2019-01-20T10:00:00Z", end time:"2019-01-20T12:00:00Z"
		//REQUESTED_START_TIME_1_STR:"2019-01-20T10:30:00Z", REQUESTED_END_TIME_1_STR: "2019-01-20T11:30:00Z" -> No projector available
		createBooking1();
		String checkProjectorAvailability = restTemplate.getForObject(getUrl() + "/v1/bookings/projectors?start_time="+REQUESTED_START_TIME_2_STR+"&end_time="+REQUESTED_END_TIME_2_STR + "&advice=false", String.class);
		assertEquals("There is no projector available for the requested slot.", checkProjectorAvailability);
	}
	
	@Test
	public void testC8_GetAvailableProjectorByRequestedSlot_NoProjectorAvailable_With_AdviceFlag_TRUE() {
		//Booking1: projector1, team1,  start time:"2019-01-20T10:00:00Z", end time:"2019-01-20T12:00:00Z"
		//REQUESTED_START_TIME_1_STR:"2019-01-20T10:30:00Z", REQUESTED_END_TIME_1_STR: "2019-01-20T11:30:00Z" -> No projector available
		createBooking1();
		String checkProjectorAvailability = restTemplate.getForObject(getUrl() + "/v1/bookings/projectors?start_time="+REQUESTED_START_TIME_2_STR+"&end_time="+REQUESTED_END_TIME_2_STR + "&advice=true", String.class);
		assertEquals("Propose new start time:2019-01-20T12:00", checkProjectorAvailability);
	}
}

