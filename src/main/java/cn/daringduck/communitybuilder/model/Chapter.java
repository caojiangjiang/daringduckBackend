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
	@NotNull @OneToOne private Course course;
	
	public Chapter() {}
	
	public Chapter(String title,Course course) {
		this.title = title;
		this.course = course;
	}
	
	public long getId() { return id;}
	
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }


	public Course getCourse() {
		return course;
	}

	public void setCourseId(Course course) {
		this.course = course;
	}
	
}
