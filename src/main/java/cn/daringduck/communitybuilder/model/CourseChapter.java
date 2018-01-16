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
@Table(name = "course_chapter")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CourseChapter {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@NotNull @OneToOne private Course course;
	@NotNull @OneToOne private Chapter chapter;
	
	@NotNull int position;
	
	public CourseChapter() {}
	
	public CourseChapter(Course course,Chapter chapter,int position) {
		this.setCourse(course);
		this.setChapter(chapter);
		this.position = position;
	}
	
	public long  getId() {
		return this.id;
	}
	
	public int getPosition() {
		return this.position;
	}
	
	public void setPosition(int position) {
		this.position = position;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Chapter getChapter() {
		return chapter;
	}

	public void setChapter(Chapter chapter) {
		this.chapter = chapter;
	}
	

}
