package cn.daringduck.communitybuilder.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "courses")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Course{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@NotNull private String name;
	
	public Course() { }
	
	public Course(String name) { 
		this.name = name;
	}
	
	public long getId() { return id; }
	
	public String getName() { return name; }
	public void setName(String xname) { this.name = xname; }
	
}
