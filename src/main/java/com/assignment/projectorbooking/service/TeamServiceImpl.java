package com.assignment.projectorbooking.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.assignment.projectorbooking.exception.ResourceNotFoundException;
import com.assignment.projectorbooking.model.Team;
import com.assignment.projectorbooking.repository.TeamRepository;

@Service
public class TeamServiceImpl implements TeamService {
	@Autowired
	private TeamRepository repository;

	@Override
	public Team createTeam(Team team) {
		return repository.save(team);
	}
	
	@Override
	public Team getTeam(int id) throws ResourceNotFoundException {
		Team team = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Team", "id", id));
		return team;
	}
	
	@Override
	public List<Team> getTeams() {
		return repository.findAll();
	}

	@Override
	public void deleteTeam(int id) throws ResourceNotFoundException {
		Team team = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Team", "id", id));
		repository.delete(team);
	}
	
	@Override
	public void cleanUpTables() {
		repository.deleteAllInBatch();
	}
}
