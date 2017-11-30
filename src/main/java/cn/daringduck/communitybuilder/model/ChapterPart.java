package cn.daringduck.communitybuilder.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "chapter_parts")
public class ChapterPart {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@OneToOne private ChapterPart nextPart;
	
	private String text;
	@NotNull @OneToOne private Picture picture;
	
	public ChapterPart() {
		super();
	}
	
	public ChapterPart(String text, Picture picture) {
		this.text = text;
		this.picture = picture;
	}
	
	public long getId() { return id; }
	
	public String getText() { return text; }
	public void setText(String text) { this.text = text; }
	
	public Picture getPicture() { return picture; }
	public void setPicture(Picture picture) { this.picture = picture; }
	
	public ChapterPart getNextPart() { return nextPart; }
	public void setNextPart(ChapterPart nextPart) { this.nextPart = nextPart; }
	
}
