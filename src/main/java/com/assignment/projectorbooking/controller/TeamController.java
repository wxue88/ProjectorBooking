package com.assignment.projectorbooking.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.projectorbooking.exception.ResourceNotFoundException;
import com.assignment.projectorbooking.model.Team;
import com.assignment.projectorbooking.service.TeamService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/v1/teams")
@Api(value="Team")
public class TeamController {
	@Autowired
	private TeamService teamService;
	
	@ApiOperation(value = "Create a new team")
	@PostMapping
	public Team createTeam(@Valid @RequestBody Team team) {
		return teamService.createTeam(team);
	}
	
	@ApiOperation(value = "Get a team with an ID")
	@GetMapping("/{id}")
	public ResponseEntity<Team> getTeam(@PathVariable(value="id") int id) throws ResourceNotFoundException {
		Team team = teamService.getTeam(id);
		return new ResponseEntity<Team>(team, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Get a list of teams")
	@GetMapping
	public ResponseEntity<List<Team>> getTeams() {
		List<Team> teams = teamService.getTeams();
		return new ResponseEntity<List<Team>>(teams, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Delete a team with an ID")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteTeam(@PathVariable(value="id") int id) throws ResourceNotFoundException{
		teamService.deleteTeam(id);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
}
