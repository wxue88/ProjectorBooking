package com.assignment.projectorbooking.service;

import java.util.List;

import com.assignment.projectorbooking.exception.ResourceNotFoundException;
import com.assignment.projectorbooking.model.Team;

public interface TeamService {
	
	public Team createTeam(Team team);
	
	public Team getTeam(int id) throws ResourceNotFoundException;
	
	public List<Team> getTeams();
	
	public void deleteTeam(int id) throws ResourceNotFoundException;
	
	public void cleanUpTables();
}
