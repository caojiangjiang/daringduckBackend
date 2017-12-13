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
@Table(name = "chapter_chapterPart")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ChapterChapterPart {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@NotNull @OneToOne private Chapter chapter;
	
	@NotNull @OneToOne private ChapterPart chapterPart;
	
	private int position;
	
	public ChapterChapterPart() {}
	
	public ChapterChapterPart(Chapter chapter, ChapterPart chapterPart,int position) {
		this.chapter = chapter;
		this.chapterPart = chapterPart;
		this.position = position;
		
	}
	
	public long getId() {
		return this.id;
	}

	public Chapter getChapter() {
		return chapter;
	}

	public void setChapter(Chapter chapter) {
		this.chapter = chapter;
	}

	public ChapterPart getChapterPart() {
		return chapterPart;
	}

	public void setChapterPart(ChapterPart chapterPart) {
		this.chapterPart = chapterPart;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

}
