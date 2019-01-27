package com.assignment.projectorbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.assignment.projectorbooking.model.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {
	@Query(value = "SELECT * FROM team WHERE name = ?1", nativeQuery = true)
	public Team findByName(String name);
}
