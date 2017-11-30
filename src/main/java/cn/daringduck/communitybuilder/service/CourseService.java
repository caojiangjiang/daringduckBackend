package cn.daringduck.communitybuilder.service;

import cn.daringduck.communitybuilder.model.Chapter;
import cn.daringduck.communitybuilder.model.ChapterPart;
import cn.daringduck.communitybuilder.model.Course;
import cn.daringduck.communitybuilder.model.Picture;
import cn.daringduck.communitybuilder.repository.ChapterPartRepository;
import cn.daringduck.communitybuilder.repository.ChapterRepository;
import cn.daringduck.communitybuilder.repository.CourseRepository;
import cn.daringduck.communitybuilder.repository.PictureRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.daringduck.communitybuilder.Error;
import cn.daringduck.communitybuilder.RequestException;

/**
 * Manages courses, chapters and chapter parts
 * 
 * @author Jochem Ligtenberg
 */
@Service
public class CourseService extends GenericService<Course, Integer> {

	private final CourseRepository courseRepository;
	private final ChapterRepository chapterRepository;
	private final ChapterPartRepository partRepository;
	private final PictureRepository pictureRepository;

	@Autowired
	public CourseService(CourseRepository courseRepository, ChapterRepository chapterRepository,
			ChapterPartRepository partRepository, PictureRepository pictureRepository) {
		super(courseRepository);
		this.courseRepository = courseRepository;
		this.chapterRepository = chapterRepository;
		this.partRepository = partRepository;
		this.pictureRepository = pictureRepository;
	}

	////////////////////////////////////////////////////////////////////
	// Course
	////////////////////////////////////////////////////////////////////
	
	/**
	 * Add a new course
	 * @param name
	 * @return
	 */
	public Course addCourse(String name) {
		Course course = new Course(name);
		courseRepository.save(course);
		return course;
	}
	
	/**
	 * Edit a course
	 * @param id
	 * @param name
	 * @return
	 * @throws RequestException
	 */
	public Course editCourse(int id, String name) throws RequestException {
		Course course = courseRepository.getOne(id);
		
		if (course == null) {
			throw new RequestException(Error.COURSE_DOES_NOT_EXIST);
		}
		
		if (name != null) {
			course.setName(name);
		}
		
		courseRepository.save(course);
		
		return course;
	}

	/**
	 * Delete a course
	 * @param id
	 */
	public void deleteCourse(int id) {
		courseRepository.delete(id);
	}
	
	////////////////////////////////////////////////////////////////////
	// Chapter
	////////////////////////////////////////////////////////////////////

	/**
	 * Get chapters in a course
	 * @param courseId
	 * @return
	 * @throws RequestException
	 */
	public List<Chapter> getChapters(int courseId) throws RequestException {
		Course course = courseRepository.getOne(courseId);

		if (course == null) {
			throw new RequestException(Error.COURSE_DOES_NOT_EXIST);
		}
		
		return course.getChapters();
	}
	
	/**
	 * Get a specific chapter
	 * @param id
	 * @return
	 */
	public Chapter getChapter(long id) {
		return chapterRepository.findOne(id);
	}
	
	public Chapter addChapter(int courseId, String title) throws RequestException {
		Course course = courseRepository.getOne(courseId);

		if (course == null) {
			throw new RequestException(Error.COURSE_DOES_NOT_EXIST);
		}

		Chapter chapter = new Chapter(title);

		Chapter prevChapter = course.addChapter(chapter);
		chapterRepository.save(chapter);
		if(prevChapter != null ) { 
			chapterRepository.save(prevChapter);
		}
		courseRepository.save(course);

		return chapter;
	}
	
	/**
	 * Edit a chapter
	 * @param chapterId
	 * @param title
	 * @return
	 * @throws RequestException
	 */
	public Chapter editChapter(long chapterId, String title) throws RequestException {
		Chapter chapter = chapterRepository.getOne(chapterId);
		
		if (chapter == null) {
			throw new RequestException(Error.CHAPTER_DOES_NOT_EXIST);
		}
		System.out.println("CHAP:"+ chapter);
		System.out.println("TITL:"+ title);
		if (title != null) {
			chapter.setTitle(title);
		}

		chapterRepository.save(chapter);
		
		return chapter;
	}
	
	/**
	 * Delete a specific chapter
	 * @param chapterId
	 */
	public void deleteChapter(long chapterId) {
		//TODO: UNSAFE, FIRST NEED TO MANAGE LIST IN COURSE
		
		//chapterRepository.delete(chapterId);
	}

	////////////////////////////////////////////////////////////////////
	// Chapter Part
	////////////////////////////////////////////////////////////////////
	
	/**
	 * Add a chapter part
	 * @param chapterId
	 * @param text
	 * @param pictureId
	 * @return
	 * @throws RequestException
	 */
	public ChapterPart addChapterPart(long chapterId, String text, long pictureId) throws RequestException {
		Chapter chapter = chapterRepository.getOne(chapterId);
		
		if (chapter == null) {
			throw new RequestException(Error.CHAPTER_DOES_NOT_EXIST);
		}
		
		Picture picture = pictureRepository.getOne(pictureId);
		
		if (picture == null) {
			throw new RequestException(Error.PICTURE_DOES_NOT_EXIST);
		}
		
		ChapterPart part = new ChapterPart(text, picture);
		chapter.addChapterPart(part);
		partRepository.save(part);
		chapterRepository.save(chapter);
		
		return part;
		
	}

	/**
	 * Edit a chapter part
	 * @param chapterPartId
	 * @param text
	 * @param pictureId
	 * @return
	 * @throws RequestException
	 */
	public ChapterPart editChapterPart(long chapterPartId, String text, long pictureId) throws RequestException {
		ChapterPart part = partRepository.getOne(chapterPartId);
		
		if (part == null) {
			throw new RequestException(Error.CHAPTER_PART_DOES_NOT_EXIST);
		}
		
		if (text != null) {
			part.setText(text);
		}
		
		if (pictureId != 0) {
			Picture picture = pictureRepository.getOne(pictureId);
			
			if (picture == null) {
				throw new RequestException(Error.PICTURE_DOES_NOT_EXIST);
			}
			
			part.setPicture(picture);
		}
		
		partRepository.save(part);
		
		return part;
	}

	/**
	 * Delete a chapter part
	 * @param partId
	 */
	public void deleteChapterPart(long partId) {
		//TODO: UNSAFE, FIRST NEED TO MANAGE LIST IN Chapter
		
		//partRepository.delete(partId);
	}

}
