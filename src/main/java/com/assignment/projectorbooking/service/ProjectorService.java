package com.assignment.projectorbooking.service;

import java.util.List;

import com.assignment.projectorbooking.exception.ResourceNotFoundException;
import com.assignment.projectorbooking.model.Projector;

public interface ProjectorService {

	public Projector createProjector(Projector projector);
	
	public Projector getProjector(int id) throws ResourceNotFoundException;
	
	public List<Projector> getProjectors();
	
	public String getAvailableProjectorName(String bookedProjectorIds);
	
	public void deleteProjector(int id) throws ResourceNotFoundException;
	
	public void cleanUpTables();

}
