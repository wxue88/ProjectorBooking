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
import com.assignment.projectorbooking.model.Projector;
import com.assignment.projectorbooking.service.ProjectorService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/v1/projectors")
@Api(value="Projector")
public class ProjectorController {
	@Autowired
	private ProjectorService projectorService;
	
	@ApiOperation(value = "Create a projector")
	@PostMapping
	public Projector createProjector(@Valid @RequestBody Projector projector) {
		return projectorService.createProjector(projector);
	}
	
	@ApiOperation(value = "Get a projector with an ID")
	@GetMapping("/{id}")
	public ResponseEntity<Projector> getProjector(@PathVariable(value="id") int id) throws ResourceNotFoundException {
		Projector projector = projectorService.getProjector(id);
		return new ResponseEntity<Projector>(projector, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Get a list of projectors")
	@GetMapping
	public ResponseEntity<List<Projector>> getProjectors() {
		List<Projector> projectors = projectorService.getProjectors();
		return new ResponseEntity<List<Projector>>(projectors, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Delete a projector with an ID")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteProjector(@PathVariable(value="id") int id) throws ResourceNotFoundException {
		projectorService.deleteProjector(id);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
}
