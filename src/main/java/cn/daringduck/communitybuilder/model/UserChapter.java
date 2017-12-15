package cn.daringduck.communitybuilder.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "user_chapter")
public class UserChapter {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@NotNull @OneToOne private User user;
	
	@NotNull @OneToOne private Chapter chapter;
	
	@NotNull @OneToOne private User teacher;
	
	private int score;
	
	private long date;
	
	@NotNull private boolean passOrNot;
	
	public UserChapter() {}
	
	public UserChapter(User user,Chapter chapter,User teacher,long date,boolean passOrNot,int score) {
		this.user = user;
		this.chapter = chapter;
		this.teacher = teacher;
		this.date = date;
		this.passOrNot = passOrNot;
		this.score = score;
	}
	
	public long getId() {
		return this.id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Chapter getCourse() {
		return chapter;
	}

	public void setCourse(Chapter chapter) {
		this.chapter = chapter;
	}

	public User getTeacher() {
		return teacher;
	}

	public void setTeacher(User teacher) {
		this.teacher = teacher;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public boolean isPassOrNot() {
		return passOrNot;
	}

	public void setPassOrNot(boolean passOrNot) {
		this.passOrNot = passOrNot;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

}
