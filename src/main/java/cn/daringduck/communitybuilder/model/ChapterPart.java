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
@Table(name = "chapter_parts")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
public class ChapterPart {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	private String english_text;
	
	private String chinese_text;
	
	private String dutch_text;
	
	@NotNull @OneToOne private Picture picture;
	
	private long chapterId;
	
	@NotNull @OneToOne private Course relationCourse;
	
	public ChapterPart() {}
	
	public ChapterPart(String english_text,String chinese_text,String dutch_text, Picture picture,long chapterId,Course course) {
		this.english_text = english_text;
		this.chinese_text = chinese_text;
		this.dutch_text =dutch_text;
		this.picture = picture;
        this.setChapterId(chapterId);
		this.relationCourse = course;
	}
	
	public long getId() { return id; }
	
	public Picture getPicture() { return picture; }
	public void setPicture(Picture picture) { this.picture = picture; }

	public String getEnglish_text() {
		return english_text;
	}

	public void setEnglish_text(String english_text) {
		this.english_text = english_text;
	}

	public String getChinese_text() {
		return chinese_text;
	}

	public void setChinese_text(String chinese_text) {
		this.chinese_text = chinese_text;
	}

	public String getDutch_text() {
		return dutch_text;
	}

	public void setDutch_text(String dutch_text) {
		this.dutch_text = dutch_text;
	}

	public Course getRelationCourse() {
		return relationCourse;
	}

	public void setRelationCourse(Course relationCourse) {
		this.relationCourse = relationCourse;
	}

	public long getChapterId() {
		return chapterId;
	}

	public void setChapterId(long chapterId) {
		this.chapterId = chapterId;
	}
	
}
