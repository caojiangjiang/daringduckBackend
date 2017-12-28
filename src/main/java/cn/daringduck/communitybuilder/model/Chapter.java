package cn.daringduck.communitybuilder.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A Chapter in a Course
 * 
 * @author Jochem Ligtenberg
 */
@Entity
@Table(name = "chapters")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Chapter{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@NotNull private String english_title;
	
	private String chinese_title;
	
	private String dutch_title;
	
	private int courseId;
	
	@NotNull private boolean requiredOrNot;
	
	public Chapter() {}
	
	public Chapter(String english_title,String chinese_title,String dutch_title,int courseId,boolean requiredOrNot) { 
		this.english_title = english_title;
		this.chinese_title = chinese_title;
		this.dutch_title = dutch_title;
		this.courseId= courseId; 
		this.requiredOrNot = requiredOrNot; 
	} 
	
	public long getId() { return id;}
	
	public String getEnglishTitle() { return english_title; }
	public void setEnglishTitle(String english_title) { this.english_title = english_title; }


	public boolean isRequiredOrNot() {
		return requiredOrNot;
	}

	public void setRequiredOrNot(boolean requiredOrNot) {
		this.requiredOrNot = requiredOrNot;
	}

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public String getChinese_title() {
		return chinese_title;
	}

	public void setChinese_title(String chinese_title) {
		this.chinese_title = chinese_title;
	}

	public String getDutch_title() {
		return dutch_title;
	}

	public void setDutch_title(String dutch_title) {
		this.dutch_title = dutch_title;
	}
	
}
