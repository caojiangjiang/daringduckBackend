package cn.daringduck.communitybuilder.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
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
	
	@NotNull private String title;
	private int courseId;
	
	@NotNull private boolean requiredOrNot;
	
	public Chapter() {}
	
	public Chapter(String title,int courseId) {
		this.title = title;
		this.courseId= courseId;
	}
	
	public long getId() { return id;}
	
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }


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
	
}
