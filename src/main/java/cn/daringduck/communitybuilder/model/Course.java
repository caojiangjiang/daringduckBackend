package cn.daringduck.communitybuilder.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
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
	
	@OneToOne private Picture picture;
	
	public Course() { }
	
	public Course(String name,Picture picture) { 
		this.name = name;
		this.picture = picture;
	}
	
	
	public int getId() { return id; }
	
	public String getName() { return name; }
	public void setName(String xname) { this.name = xname; }

	public Picture getPicture() {
		return picture;
	}

	public void setPicture(Picture picture) {
		this.picture = picture;
	}

}
