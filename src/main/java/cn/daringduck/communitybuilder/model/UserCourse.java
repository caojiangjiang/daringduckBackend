package cn.daringduck.communitybuilder.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "user_course")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
public class UserCourse {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@OneToOne private User user;
	
	@OneToOne private Course course;
	
	@OneToOne private User teacher;
	
	private long date;
	

	private boolean passedOrNot;
	
	public UserCourse() {}
	
	public UserCourse(User user, Course course,User teacher, long date,boolean passedOrNot) {
		this.user = user;
		this.course = course;
		this.teacher = teacher;
		this.date = date;
		this.passedOrNot = passedOrNot;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public User getTeacher() {
		return teacher;
	}

	public void setTeacher(User teacher) {
		this.teacher = teacher;
	}

	public boolean isPassedOrNot() {
		return passedOrNot;
	}

	public void setPassedOrNot(boolean passedOrNot) {
		this.passedOrNot = passedOrNot;
	}
}
