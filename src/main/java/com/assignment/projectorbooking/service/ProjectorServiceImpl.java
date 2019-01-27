package com.assignment.projectorbooking.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.assignment.projectorbooking.exception.ResourceNotFoundException;
import com.assignment.projectorbooking.model.Projector;
import com.assignment.projectorbooking.repository.ProjectorRepository;

@Service
public class ProjectorServiceImpl implements ProjectorService {
	@Autowired
	private ProjectorRepository repository;

	@Override
	public Projector createProjector(Projector projector) {
		return repository.save(projector);
	}
	
	@Override
	public Projector getProjector(int id) throws ResourceNotFoundException {
		return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Projector", "id", id));
	}
	
	@Override
	public String getAvailableProjectorName(String bookedProjectorIds) {
		return repository.getAvailableProjectorName(bookedProjectorIds);
	}
	
	@Override
	public List<Projector> getProjectors() {
		return repository.findAll();
	}

	@Override
	public void deleteProjector(int id) throws ResourceNotFoundException {
		Projector projector = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Projector", "id", id));
		repository.delete(projector);
	}

	@Override
	public void cleanUpTables() {
		repository.deleteAllInBatch();
	}
}
