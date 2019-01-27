package com.assignment.projectorbooking.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;


@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name="projector")
public class Projector {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@ApiModelProperty(notes = "The database generated projector ID")
	private int id;
	
	@OneToOne(mappedBy="projector", cascade=CascadeType.REMOVE)
	private Booking booking;
	
	@Column(nullable = false, unique = true, length = 32)
	@ApiModelProperty(notes = "The name of the projector")
	private String name;
	
	public Projector() {
		
	}
	
	/*public Projector(String name) {
		this.name = name;
	}*/

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	@Override
	public String toString() {
		return "Projector [id=" + id + ", name=" + name + "]";
	}
}
