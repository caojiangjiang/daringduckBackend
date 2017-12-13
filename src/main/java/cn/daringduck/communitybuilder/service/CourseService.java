package cn.daringduck.communitybuilder.service;

import cn.daringduck.communitybuilder.model.Chapter;
import cn.daringduck.communitybuilder.model.ChapterChapterPart;
import cn.daringduck.communitybuilder.model.ChapterPart;
import cn.daringduck.communitybuilder.model.Course;
import cn.daringduck.communitybuilder.model.CourseChapter;
import cn.daringduck.communitybuilder.model.Picture;
import cn.daringduck.communitybuilder.repository.ChapterChapterPartRepository;
import cn.daringduck.communitybuilder.repository.ChapterPartRepository;
import cn.daringduck.communitybuilder.repository.ChapterRepository;
import cn.daringduck.communitybuilder.repository.CourseChapterRepository;
import cn.daringduck.communitybuilder.repository.CourseRepository;
import cn.daringduck.communitybuilder.repository.PictureRepository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.daringduck.communitybuilder.Error;
import cn.daringduck.communitybuilder.RequestException;

/**
 * Manages courses, chapters and chapter parts
 * 
 * @author Jochem Ligtenberg
 */
@Service
@Transactional
public class CourseService extends GenericService<Course, Integer> {

	private final CourseRepository courseRepository;
	private final ChapterRepository chapterRepository;
	private final ChapterPartRepository partRepository;
	private final PictureRepository pictureRepository;
	private final CourseChapterRepository courseChapterRepository;
	private final ChapterChapterPartRepository chapterChapterPartRepository;

	@Autowired
	public CourseService(CourseRepository courseRepository, ChapterRepository chapterRepository,
			ChapterPartRepository partRepository, PictureRepository pictureRepository,
			CourseChapterRepository courseChapterRepository,ChapterChapterPartRepository chapterChapterPartRepository) {
		super(courseRepository);
		this.courseRepository = courseRepository;
		this.chapterRepository = chapterRepository;
		this.partRepository = partRepository;
		this.pictureRepository = pictureRepository;
		this.courseChapterRepository = courseChapterRepository;
		this.chapterChapterPartRepository = chapterChapterPartRepository;
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
		//get courseChapters
		List<CourseChapter> courseChapters = courseChapterRepository.getChapterFromCourseChapterByCourseId(id);
		
		//delete courseChapters
		courseChapterRepository.delete(courseChapters);
		
		System.out.println(courseChapters.size());
		
		for(int i=0;i<courseChapters.size();i++) {
			
			//get chapters
			Chapter chapter = courseChapters.get(i).getChapter();
			
			//get chapterChapterParts
			List<ChapterChapterPart> chapterChapterParts = chapterChapterPartRepository.getByChapterId(chapter.getId());
			
			//delete chapterChapterParts
			chapterChapterPartRepository.delete(chapterChapterParts);
			
			System.out.println(chapterChapterParts.size());
			
			//delete ChapterPart
			for(int j=0;j<chapterChapterParts.size();j++) {
				System.out.println(chapterChapterParts.get(j).getChapterPart());
				
				partRepository.delete(chapterChapterParts.get(j).getChapterPart());
			}
			
			//delete chapter
			chapterRepository.delete(chapter);
		}
		
		//delete course
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
		
		List <CourseChapter> courseChapters = courseChapterRepository.getChapterFromCourseChapterByCourseId(courseId);
		
		List <Chapter> chapters = new ArrayList<>();
		
		for(int i=0;i<courseChapters.size();i++) {
			Chapter chapter = courseChapters.get(i).getChapter();
			
			chapters.add(chapter);
		}
		
		return chapters;
	}
	
	/**
	 * Get a specific chapter
	 * @param id
	 * @return
	 */
	public Chapter getChapter(long id) {
		return chapterRepository.findOne(id);
	}
	
	public Chapter addChapterStep1(int courseId, String title) throws RequestException {
		Course course = courseRepository.getOne(courseId);
		
		
		if (course == null) {
			throw new RequestException(Error.COURSE_DOES_NOT_EXIST);
		}

		Chapter chapter = new Chapter(title,course);

		return chapter;
	}
	
	
	public Boolean addChapterStep2(int courseId, String lists) throws RequestException {
		
		Course course = courseRepository.getOne(courseId);
		
		courseChapterRepository.deleteChapterFromCourseChapterByCourseId(courseId);
		
		int j = 1;
		
		if(course == null) {
			throw new RequestException(Error.COURSE_DOES_NOT_EXIST); 
		}
		
		String []item = lists.split(",");
		
		for(int i=0;i<item.length;i++) {
			long chapterId =Integer.parseInt(item[i]);
			Chapter chapter = chapterRepository.getOne(chapterId);
			CourseChapter courseChapter = new CourseChapter(course,chapter,j);
			courseChapterRepository.save(courseChapter);
			j++;
		}

		return true;
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
	 * @throws RequestException 
	 */
	public void deleteChapter(int courseId , long chapterId) throws RequestException {
		//TODO: UNSAFE, FIRST NEED TO MANAGE LIST IN COURSE
		
		List <CourseChapter> courseChapters = courseChapterRepository.getChapterFromCourseChapterByCourseId(courseId);
		
		courseChapterRepository.deleteChapterFromCourseChapterByCourseId(courseId);
		
		Course course = courseRepository.getOne(courseId);
		
		if(course == null) {
			throw new RequestException(Error.COURSE_DOES_NOT_EXIST);
		}
		
		int j = 1;
		
		//delete the Specified courseChapter then reorder old courseChapter
		for(int i=0;i<courseChapters.size();i++) {
			
			if(courseChapters.get(i).getChapter().getId()!=chapterId) {
				Chapter chapter = courseChapters.get(i).getChapter();
				
				CourseChapter courseChapter = new CourseChapter(course,chapter,j);
				
				courseChapterRepository.save(courseChapter);
				j++;
			}
		}
		
		chapterChapterPartRepository.deleteChapterPartFromChapterChapterPartByChapterId(chapterId);
		
		chapterRepository.delete(chapterId);
		
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
	public ChapterPart addChapterPartStep1(long chapterId, String text, long pictureId) throws RequestException {

		
		Picture picture = pictureRepository.getOne(pictureId);
		
		if (picture == null) {
			throw new RequestException(Error.PICTURE_DOES_NOT_EXIST);
		}
		
		ChapterPart part = new ChapterPart(text, picture,chapterId);
		
		
		System.out.println(text);
		System.out.println(picture);
		System.out.println(chapterId);

		partRepository.save(part);
		
		return part;
		
	}
	
	
	/**
	 * Add a chapter part
	 * @param chapterId
	 * @param text
	 * @param pictureId
	 * @return
	 * @throws RequestException
	 */
	public Boolean addChapterPartStep2(long chapterId, String lists) throws RequestException {
		
		int j = 1;
		
		String []item = lists.split(",");
		
		Chapter chapter = chapterRepository.getOne(chapterId);
		
		chapterChapterPartRepository.deleteChapterPartFromChapterChapterPartByChapterId(chapterId);
		
		for(int i=0;i<item.length;i++) {
			
			long chapterPartId = Integer.parseInt(item[i]);
			ChapterPart chapterPart = partRepository.getOne(chapterPartId);
			ChapterChapterPart chapterChapterPart = new ChapterChapterPart(chapter,chapterPart,j);
			chapterChapterPartRepository.save(chapterChapterPart);
			j++;
		
		}

		return true;
		
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
	public void deleteChapterPart(long chapterId ,long partId) {
		//TODO: UNSAFE, FIRST NEED TO MANAGE LIST IN Chapter
		
		//partRepository.delete(partId);
		List<ChapterChapterPart> chapterChapterParts = chapterChapterPartRepository.getByChapterId(chapterId);
		
		int j = 1;
		
		chapterChapterPartRepository.deleteChapterPartFromChapterChapterPartByChapterId(chapterId);
		
		Chapter chapter = chapterRepository.getOne(chapterId);
		
		for(int i=0;i<chapterChapterParts.size();i++) {
			
			if(chapterChapterParts.get(i).getChapterPart().getId()!=partId) {
				ChapterPart chapterPart = chapterChapterParts.get(i).getChapterPart();
				ChapterChapterPart chapterChapterPart = new ChapterChapterPart(chapter,chapterPart,j);
				chapterChapterPartRepository.save(chapterChapterPart);
				j++;
			}
		}

		
		partRepository.delete(partId);
		
	}
	
	public List<ChapterPart> getChapterPartList(long chapterId){
		return partRepository.getChapterPartListByChapterId(chapterId);
	}
	
	public ChapterPart getChapterPart(long chapterPartId) {		
		return partRepository.getOne(chapterPartId);
	}

}
