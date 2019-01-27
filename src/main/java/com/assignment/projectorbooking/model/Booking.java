package com.assignment.projectorbooking.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModelProperty;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "booking")
public class Booking {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@ApiModelProperty(notes = "The database generated booking ID")
	private int id;
	
	@OneToOne
	@JoinColumn(name = "projector_id")
	@ApiModelProperty(notes = "The ID of a projector is requested")
	private Projector projector;
	
	@OneToOne
	@JoinColumn(name = "team_id")
	@ApiModelProperty(notes = "The ID of the team requests a projector")
	private Team team;
	
	@Column(name = "start_time", nullable = false)	
	@ApiModelProperty(notes = "The start time that the team requests a projector")
    private LocalDateTime startTime;
	
	@Column(name = "end_time", nullable = false)
	@ApiModelProperty(notes = "The end time that the team requests a projector")
	private LocalDateTime endTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Projector getProjector() {
		return projector;
	}

	public void setProjector(Projector projector) {
		this.projector = projector;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}
	
	public String toString() {
		return "Booking [id=" + id + ", projectorId=" + projector.getId() + ", teamId=" + team.getId() + ", startTime=" + startTime + ", endTime=" + endTime + "]";
	}
}
