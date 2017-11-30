package cn.daringduck.communitybuilder.model;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
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
public class Chapter implements Iterable<ChapterPart>{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@NotNull private String title;
	@OneToOne private Chapter nextChapter;
	@OneToOne private ChapterPart firstPart;
	@OneToOne private ChapterPart lastPart;
	
	public Chapter() {}
	
	public Chapter(String title) {
		this.title = title;
	}
	
	public long getId() { return id;}
	
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
	
	protected Chapter getNextChapter() { return nextChapter; }
	protected void setNextChapter(Chapter nextChapter) { this.nextChapter = nextChapter; }
	
	/**
	 * Constructs a list from the chapter parts
	 * @return
	 */
	@Transient
	public List<ChapterPart> getChapterParts() { 		
		List<ChapterPart> parts = new LinkedList<ChapterPart>();
		ChapterPart part = firstPart;

		while (part != null){
			parts.add(part);
			part = part.getNextPart();
		}
		
		return parts;
	}
	
	/**
	 * Add a chapterPart
	 * @param part
	 */
	public void addChapterPart(ChapterPart part) { 	
		if (firstPart == null){ 
			firstPart = part;
		} else {
			lastPart.setNextPart(part);
		}
		lastPart = part;
	}

	/**
	 * Remove a part from the parts list
	 * @param part
	 * 			The part that should be removed
	 * @return is the part found or not
	 */
	public boolean removeChapterPart(ChapterPart part) {
		// If there are no items in our list
		if (firstPart == null) {
			return false;
		}
		
		// If the item is the first one in the list
		if (firstPart == part) {
			firstPart = firstPart.getNextPart();
			return true;
		}
		
		ChapterPart previousPart = firstPart;
		
		// If it is any other item in the list
		while (previousPart.getNextPart() != null) {
			ChapterPart currentChapter = previousPart.getNextPart();
			
			// If the item we got in the loop is the item that should be removed
			if (currentChapter == part) {
				previousPart.setNextPart(currentChapter.getNextPart());
				
				// If we removed the last item reset the last variable to the previous one
				if (currentChapter == lastPart) {
					lastPart = previousPart;
				}
				
				return true;
			}
			
			previousPart = currentChapter;
		}
		
		// If item wasn't in the list
		return false;
	}

	/////////////////////////////////////
	// CODE TO MAKE THE CLASS ITERABLE //
	/////////////////////////////////////
	
	public static final class ChapterPartIterator implements Iterator<ChapterPart> {

		private ChapterPart cursor;
		
		private ChapterPartIterator(ChapterPart firstPart){
            this.cursor = firstPart;
		}
		
		@Override
		public boolean hasNext() {
			return this.cursor != null;
		}

		@Override
		public ChapterPart next() {
            if(this.hasNext()) {
                throw new IndexOutOfBoundsException();
            }
        	ChapterPart current = cursor;
            cursor = current.getNextPart();
            return current;
		}
	}
	
	@Override
	public Iterator<ChapterPart> iterator() {
		return new ChapterPartIterator(firstPart);
	}
	
}
