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
	
	private String text;
	@NotNull @OneToOne private Picture picture;
	
	private long chapterId;
	
	public ChapterPart() {}
	
	public ChapterPart(String text, Picture picture,long chapterId) {
		this.text = text;
		this.picture = picture;
		this.chapterId = chapterId;
	}
	
	public long getId() { return id; }
	
	public String getText() { return text; }
	public void setText(String text) { this.text = text; }
	
	public Picture getPicture() { return picture; }
	public void setPicture(Picture picture) { this.picture = picture; }

	

	public long getChapterId() {
		return chapterId;
	}

	public void setChapterId(long chapterId) {
		this.chapterId = chapterId;
	}
	
}
