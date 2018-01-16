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
	@NotNull private String english_name;
	private String chinese_name;
	private String dutch_name; 
	
	@OneToOne private Picture picture;
	
	public Course() { }
	
	public Course(String english_name,String chinese_name,String dutch_name,Picture picture) { 
		this.english_name = english_name;
		this.picture = picture;
		this.chinese_name =chinese_name;
		this.dutch_name = dutch_name;
	}
	
	public int getId() { return id; }
	
	public String getEnglish_name() { return english_name; }
	public void setEnglish_name(String xname) { this.english_name = xname; }

	public Picture getPicture() {
		return picture;
	}

	public void setPicture(Picture picture) {
		this.picture = picture;
	}

	public String getChinese_name() {
		return chinese_name;
	}

	public void setChinese_name(String chinese_name) {
		this.chinese_name = chinese_name;
	}

	public String getDutch_name() {
		return dutch_name;
	}

	public void setDutch_name(String dutch_name) {
		this.dutch_name = dutch_name;
	}

}
