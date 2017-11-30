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

@Entity
@Table(name = "courses")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Course implements Iterable<Chapter>{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@NotNull private String name;
	@OneToOne private Chapter firstChapter;
	@OneToOne private Chapter lastChapter;
	
	public Course() { }
	
	public Course(String name) { 
		this.name = name;
	}
	
	public long getId() { return id; }
	
	public String getName() { return name; }
	public void setName(String xname) { this.name = xname; }
	
	@Transient
	public List<Chapter> getChapters() { 		
		List<Chapter> chapters = new LinkedList<Chapter>();
		Chapter chap = firstChapter;

		while (chap != null){
			chapters.add(chap);
			chap = chap.getNextChapter();
		}
		
		return chapters;
	}
	
	/**
	 * 
	 * @param chapter Returns chapter pointing to chapter
	 * @return
	 */
	public Chapter addChapter(Chapter chapter) { 	
		if (firstChapter == null){ 
			firstChapter = chapter;
		} else {
			lastChapter.setNextChapter(chapter);
		}
		Chapter result = lastChapter;
		lastChapter = chapter;
		return result;
	}
	
	public boolean removeChapter(Chapter chapter) {
		// If there are no items in our list
		if (firstChapter == null) {
			return false;
		}

		// If the item is the first one in the list
		if (firstChapter == chapter) {
			firstChapter = firstChapter.getNextChapter();
			return true;
		}
		
		Chapter previousChapter = firstChapter;

		// If it is any other item in the list
		while (previousChapter.getNextChapter() != null){
			Chapter currentChapter = previousChapter.getNextChapter();

			// If the item we got in the loop is the item that should be removed
			if (currentChapter == chapter) {
				previousChapter.setNextChapter(currentChapter.getNextChapter());

				// If we removed the last item reset the last variable to the previous one
				if (currentChapter == lastChapter) {
					lastChapter = previousChapter;
				}
				
				return true;
			}
			
			previousChapter = currentChapter;
		}

		// If item wasn't in the list
		return false;
	}
	
	/////////////////////////////////////
	// CODE TO MAKE THE CLASS ITERABLE //
	/////////////////////////////////////
	public static final class ChapterIterator implements Iterator<Chapter> {

		private Chapter cursor;
		
		private ChapterIterator(Chapter firstPart){
            this.cursor = firstPart;
		}
		
		@Override
		public boolean hasNext() {
			return this.cursor != null;
		}

		@Override
		public Chapter next() {
            if(this.hasNext()) {
                throw new IndexOutOfBoundsException();
            }
        	Chapter current = cursor;
            cursor = current.getNextChapter();
            return current;
		}
	}
	
	@Override
	public Iterator<Chapter> iterator() {
		return new ChapterIterator(firstChapter);
	}
}
