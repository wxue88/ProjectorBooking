package com.assignment.projectorbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.assignment.projectorbooking.model.Projector;

@Repository
public interface ProjectorRepository extends JpaRepository<Projector, Integer> {
	@Query(value = "SELECT name FROM projector WHERE FIND_IN_SET(id, ?1) = 0 LIMIT 1", nativeQuery = true)
	public String getAvailableProjectorName(String bookedProjectorIds);
	@Query(value = "SELECT* FROM projector WHERE name = ?1", nativeQuery = true)
	public Projector findByName(String name);
}
