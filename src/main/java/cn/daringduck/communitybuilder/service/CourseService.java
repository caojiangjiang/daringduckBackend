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
import cn.daringduck.communitybuilder.repository.UserChapterRepository;
import cn.daringduck.communitybuilder.repository.UserCourseRepository;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.sym.Name;

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
	private final UserCourseRepository userCourseRepository;
	private final UserChapterRepository userChapterRepository;

	@Autowired
	public CourseService(CourseRepository courseRepository, ChapterRepository chapterRepository,
			ChapterPartRepository partRepository, PictureRepository pictureRepository,
			CourseChapterRepository courseChapterRepository,ChapterChapterPartRepository chapterChapterPartRepository,
			UserCourseRepository userCourseRepository,UserChapterRepository userChapterRepository) {
		super(courseRepository);
		this.courseRepository = courseRepository;
		this.chapterRepository = chapterRepository;
		this.partRepository = partRepository;
		this.pictureRepository = pictureRepository;
		this.courseChapterRepository = courseChapterRepository;
		this.chapterChapterPartRepository = chapterChapterPartRepository;
		this.userCourseRepository = userCourseRepository;
		this.userChapterRepository = userChapterRepository;
	}

	////////////////////////////////////////////////////////////////////
	// Course
	////////////////////////////////////////////////////////////////////
	
	/**
	 * Add a new course
	 * 
	 * @param name
	 * @param pictureId
	 * @return
	 */
	public Course addCourse(String english_name,String chinese_name,String dutch_name,long pictureId) {
		//find the picture by pictureId
		Picture picture = pictureRepository.findOne(pictureId);
		Course course = new Course(english_name,chinese_name,dutch_name,picture);
		courseRepository.save(course);
		return course;
		
	}
	
	/**
	 * get list of Course By page
	 * 
	 * @param page
	 * @return String
	 * */
	
	public String getPageOfCourse(int page,int type){
		
		//pageNumber and pageSize
		Page<Course> coursesPage = courseRepository.findAll(new PageRequest(page, PAGE_SIZE));
		
		//get the Courses
		List<Course> courses = coursesPage.getContent();
		
		if(courses == null) {
			return null;
		}
		else {
			//add chapters into course
			JSONObject jsonObject1 = new JSONObject();
			for(int i =0;i<courses.size();i++) {
				
				Course course = courses.get(i);
				
				JSONObject jsonObject2 = new JSONObject();
				
				//choose the language accroding to the type
				switch (type) {
				
				case 2:{
					if(!course.getChinese_name().equalsIgnoreCase(""))
						jsonObject2.put("name", course.getChinese_name());
					else
						jsonObject2.put("name", course.getEnglish_name());
					break;
				}
				case 3:{
					if(!course.getDutch_name().equalsIgnoreCase(""))
						jsonObject2.put("name", course.getDutch_name());
					else
						jsonObject2.put("name", course.getEnglish_name());
					break;
				}
				default:jsonObject2.put("name", course.getEnglish_name());
					break;
				}
				
				
				if(course.getPicture()!=null) {
					jsonObject2.put("pictureId",course.getPicture().getId());
					jsonObject2.put("picturePosition",course.getPicture().getFileLocation());
				}
				else {
					jsonObject2.put("pictureId","");
					jsonObject2.put("picturePosition","");
				}

				jsonObject2.put("id", course.getId());
				
				jsonObject1.put(i+"", jsonObject2);
			}
			//change json into String
			return jsonObject1.toString();
		}
	}
	
	
	/**
	 * Get courses By courseName
	 * 
	 * @param courseId
	 * @return String
	 * @throws RequestException 
	 * 
	 * */
	
	public String searchCourse(String name, int type) throws RequestException {
		
		JSONObject jsonObject = new JSONObject();
		
		List<Course> courses = new ArrayList<>();
		
		name = name.toUpperCase();
		
		switch (type) {
			case 2:{
				courses = courseRepository.findByChineseNameLike(name);
				break;
			}
			case 3:{
				name = name.toUpperCase();
				courses = courseRepository.findByDutchNameLike(name);
				break;
			}
			default:{
				name = name.toUpperCase();
				courses = courseRepository.findByEnglishNameLike(name);
				break;
			}
		}
		
		for(int i =0;i<courses.size();i++) {
			JSONObject jsonObject2 = new JSONObject();
			
			jsonObject2.put("id", courses.get(i).getId());
			
			if(courses.get(i).getPicture()!=null) {
				jsonObject2.put("pictureId", courses.get(i).getPicture().getId());
				jsonObject2.put("pictureLocation", courses.get(i).getPicture().getFileLocation());
			}
			else {
				jsonObject2.put("pictureId", "");
				jsonObject2.put("pictureLocation", "");
			}
			
			switch (type) {
			case 2:
			{
				if(!courses.get(i).getChinese_name().equalsIgnoreCase(""))
					jsonObject2.put("name", courses.get(i).getChinese_name());
				else
					jsonObject2.put("name", courses.get(i).getEnglish_name());
				break;
			}
			case 3:
			{
				if(!courses.get(i).getDutch_name().equalsIgnoreCase(""))
					jsonObject2.put("name", courses.get(i).getDutch_name());
				else
					jsonObject2.put("name", courses.get(i).getEnglish_name());
				break;
			}

			default:jsonObject2.put("name", courses.get(i).getEnglish_name());
				break;
			}
			
			jsonObject.put(i+"", jsonObject2);
		}
		
		return jsonObject.toString();
		
	}
	
	/**
	 * Get a course By courseId
	 * 
	 * @param courseId
	 * @return String
	 * @throws RequestException 
	 * 
	 * */
	
	public String getCourse(int courseId,int type) throws RequestException {
		
		Course course = courseRepository.findOne(courseId);
		
		if(course == null) {
			throw new RequestException(Error.COURSE_DOES_NOT_EXIST); 
		}
		
		//add courses
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", courseId);
		//choose the language accroding to the type
		switch (type) {
			
		case 2:{
			if(!course.getChinese_name().equalsIgnoreCase(""))
				jsonObject.put("name", course.getChinese_name());
			else
				jsonObject.put("name", course.getEnglish_name());
			break;
		}
		case 3:{
			if(!course.getDutch_name().equalsIgnoreCase(""))
				jsonObject.put("name", course.getDutch_name());
			else
				jsonObject.put("name", course.getEnglish_name());
			break;
		}
		default:jsonObject.put("name", course.getEnglish_name());
			break;
		}
		
		//add picture
		Picture picture = course.getPicture();
		if(picture==null) {
			jsonObject.put("pictureId", "");
			jsonObject.put("picturePosition","");
		}
		else {
			jsonObject.put("pictureId", picture.getId());
			jsonObject.put("picturePosition", picture.getFileLocation());
		}

		
		List<CourseChapter> courseChapters = courseChapterRepository.getChapterFromCourseChapterByCourseId(courseId);
		
		if(courseChapters!=null) {
			List<Chapter> chapters = new ArrayList<>();
			for(int i =0;i<courseChapters.size();i++) {
				chapters.add(courseChapters.get(i).getChapter());
			}
			jsonObject.put("chapters", chapters);
		}
		
		return jsonObject.toString();
	}
	
	/**
	 * Edit a course
	 * @param id
	 * @param name
	 * @return
	 * @throws RequestException
	 */
	public Course editCourse(int id, String english_name,String chinese_name,String dutch_name,long pictureId) throws RequestException {
		//find a course by id
		Course course = courseRepository.findOne(id);
		
		if (course == null) {
			throw new RequestException(Error.COURSE_DOES_NOT_EXIST);
		}
		
		Picture picture = pictureRepository.findOne(pictureId);
		
		if(picture!=null) {
			course.setPicture(picture);
		}
		
		//reset the course name
		if (english_name != null) {
			course.setEnglish_name(english_name);
		}
		
		if(chinese_name!=null) {
			course.setChinese_name(chinese_name);
		}
		
		if(dutch_name!=null) {
			course.setDutch_name(dutch_name);
		}
		
		courseRepository.save(course);
		
		return course;
	}

	/**
	 * Delete a course
	 * @param id
	 */
	public boolean deleteCourse(int id) {		
		//get courseChapters
		List<CourseChapter> courseChapters = courseChapterRepository.getChapterFromCourseChapterByCourseId(id);
		
		//delete courseChapters
		courseChapterRepository.delete(courseChapters);
		
		for(int i=0;i<courseChapters.size();i++) {
			
			//get chapters
			Chapter chapter = courseChapters.get(i).getChapter();
			
			//get chapterChapterParts
			List<ChapterChapterPart> chapterChapterParts = chapterChapterPartRepository.getByChapterId(chapter.getId());
			
			//delete chapterChapterParts
			chapterChapterPartRepository.delete(chapterChapterParts);
			
			//delete ChapterPart
			for(int j=0;j<chapterChapterParts.size();j++) {	
				partRepository.delete(chapterChapterParts.get(j).getChapterPart());
			}
			
			//delete userChapter
			userChapterRepository.deleteByChapterId(chapter.getId());
			
			//delete chapter
			chapterRepository.delete(chapter);
		}
		
		//delete userCourse
		userCourseRepository.deleteByCourseId(id);
		
		//delete course
		courseRepository.delete(id);
		
		return true;
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
	public String getChapters(int courseId,int type) throws RequestException {
		
		Course course = courseRepository.findOne(courseId);

		if (course == null) {
			throw new RequestException(Error.COURSE_DOES_NOT_EXIST);
		}
		
		List <CourseChapter> courseChapters = courseChapterRepository.getChapterFromCourseChapterByCourseId(courseId);
		
		JSONObject jsonObject = new JSONObject();
		
		for(int i=0;i<courseChapters.size();i++) {
			JSONObject jsonObject2 = new JSONObject();
			jsonObject2.put("id",courseChapters.get(i).getChapter().getId());
			jsonObject2.put("courseId", courseChapters.get(i).getChapter().getCourse().getId());
			jsonObject2.put("requiredOrNot", courseChapters.get(i).getChapter().isRequiredOrNot());
			switch (type) {
			case 2:{
				if( !courseChapters.get(i).getChapter().getChinese_title().equalsIgnoreCase(""))
					jsonObject2.put("title",  courseChapters.get(i).getChapter().getChinese_title());
				else
					jsonObject2.put("title", courseChapters.get(i).getChapter().getEnglish_title());
				break;
			}
			case 3:{
				if(!courseChapters.get(i).getChapter().getDutch_title().equalsIgnoreCase(""))
					jsonObject2.put("title",  courseChapters.get(i).getChapter().getDutch_title());
				else
					jsonObject2.put("title", courseChapters.get(i).getChapter().getEnglish_title());
				break;
			}
			default:jsonObject2.put("title", courseChapters.get(i).getChapter().getEnglish_title());
				break;
			}
			
			jsonObject.put(i+"",jsonObject2);
		}
		
		return jsonObject.toString();
	}
	
	
	/**
	 * Get chapters in a course
	 * @param courseId
	 * @return
	 * @throws RequestException
	 */
	public List<Chapter>  getAllLanguageChapters(int courseId) throws RequestException {
		
		Course course = courseRepository.findOne(courseId);

		if (course == null) {
			throw new RequestException(Error.COURSE_DOES_NOT_EXIST);
		}
		
		List <CourseChapter> courseChapters = courseChapterRepository.getChapterFromCourseChapterByCourseId(courseId);
		
		if(courseChapters == null) {
			throw new RequestException(Error.USERCOURSE_DOES_NOT_EXIS);
		}
		
		List<Chapter> chapters = new ArrayList<>();
		
		for(int i=0;i<courseChapters.size();i++) {
			chapters.add(courseChapters.get(i).getChapter());
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
	
	/**
	 * add a chapter
	 * @param courseId
	 * @param title
	 * 
	 * */
	public Chapter addChapterStep1(int courseId, String english_title,String chinese_title,String dutch_title,String requiredOrNot) throws RequestException {
		
		Chapter chapter;
		
		Course course = get(courseId);
		
		if(course == null) {
			throw new RequestException(Error.COURSE_DOES_NOT_EXIST);
		}
		
		if(requiredOrNot.equalsIgnoreCase("true")) {
			chapter = new Chapter(english_title,chinese_title,dutch_title,course,true);
		}
		else {
			chapter = new Chapter(english_title,chinese_title,dutch_title,course,false);
		}

		chapterRepository.save(chapter);
		
		return chapter;
	}
	
	/**
	 * add a chapter
	 * @param courseId
	 * @param lists
	 * @return boolean 
	 * 
	 * */
	public boolean addChapterStep2(int courseId, String lists) throws RequestException {
		
		Course course = courseRepository.findOne(courseId);
		
		if(course == null) {
			throw new RequestException(Error.COURSE_DOES_NOT_EXIST); 
		}
		
		courseChapterRepository.deleteChapterFromCourseChapterByCourseId(courseId);
		
		int j = 1;
		
		String []item = lists.split(",");
		
		for(int i=0;i<item.length;i++) {
			//get chapterId and chapter
			long chapterId =Integer.parseInt(item[i]);
			Chapter chapter = chapterRepository.getOne(chapterId);
			
			//set the courseId of chapter
			if(chapter==null) {
				throw new RequestException(Error.CHAPTER_DOES_NOT_EXIST);
			}
			
			chapter.setCourse(course);
			
			chapterRepository.save(chapter);
			
			//create a courseChapter
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
	public Chapter editChapter(long chapterId, String english_title,String chinese_title,String dutch_title,String requiredOrNot) throws RequestException {
		
		Chapter chapter = chapterRepository.findOne(chapterId);
		
		if (chapter == null) {
			throw new RequestException(Error.CHAPTER_DOES_NOT_EXIST);
		}
		
		if(requiredOrNot.equals("true")) {
			chapter.setRequiredOrNot(true);
		}
		else {
			chapter.setRequiredOrNot(false);
		}
		
		if (english_title != null) {
			chapter.setEnglish_title(english_title);
		}

		if(chinese_title!=null) {
			chapter.setChinese_title(chinese_title);
		}
		
		if(dutch_title!=null) {
			chapter.setDutch_title(dutch_title);
		}
		
		chapterRepository.save(chapter);
		
		return chapter;
	}
	
	/**
	 * Delete a specific chapter
	 * @param chapterId
	 * @throws RequestException 
	 */
	public boolean deleteChapter(int courseId , long chapterId) throws RequestException {
		//TODO: UNSAFE, FIRST NEED TO MANAGE LIST IN COURSE
		//get courseChapters by courseId
		List <CourseChapter> courseChapters = courseChapterRepository.getChapterFromCourseChapterByCourseId(courseId);
		
		//delete old courseChapters by courseId
		courseChapterRepository.deleteChapterFromCourseChapterByCourseId(courseId);
		
		//find a course by courseId
		Course course = courseRepository.findOne(courseId);
		
		if(course == null) {
			throw new RequestException(Error.COURSE_DOES_NOT_EXIST);
		}
		
		Chapter chapter1 = getChapter(chapterId);
		
		if(chapter1 == null) {
			throw new RequestException(Error.CHAPTER_DOES_NOT_EXIST);
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
		//delete the relation between chapter and chapterPart
		chapterChapterPartRepository.deleteChapterPartFromChapterChapterPartByChapterId(chapterId);
		
	    //delete user chapter 
	    userChapterRepository.deleteByChapterId(chapterId); 
	     
	    //delete chapterPart 
	    partRepository.deleteByChapter(chapter1); 
		
		//delete chapter
		chapterRepository.delete(chapterId);
		
		return true;
		
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
	public ChapterPart addChapterPartStep1(long chapterId, String english_text,String chinese_text,String dutch_text, long pictureId,int courseId) throws RequestException {

		//get the picture By pictureId
		Picture picture = pictureRepository.findOne(pictureId);
		
		if (picture == null) {
			throw new RequestException(Error.PICTURE_DOES_NOT_EXIST);
		}
		
		Chapter chapter = chapterRepository.findOne(chapterId);
		
		if(chapter == null) {
			throw new RequestException(Error.CHAPTER_DOES_NOT_EXIST);
		}
		
		Course course = get(courseId);
		
		ChapterPart part = new ChapterPart(english_text,chinese_text,dutch_text,picture,chapter,course);

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
	public boolean addChapterPartStep2(long chapterId, String lists) throws RequestException {
		
		int j = 1;
		//get the number of chapter part id list
		String []item = lists.split(",");
		
		//find a chapter by chapterId
		Chapter chapter = chapterRepository.findOne(chapterId);
		
		if(chapter == null) {
			throw new RequestException(Error.CHAPTER_DOES_NOT_EXIST);
		}
		
		chapterChapterPartRepository.deleteChapterPartFromChapterChapterPartByChapterId(chapterId);
		
		for(int i=0;i<item.length;i++) {
			
			long chapterPartId = Integer.parseInt(item[i]);
			ChapterPart chapterPart = partRepository.findOne(chapterPartId);
			
			if(chapterPart == null) {
				throw new RequestException(Error.CHAPTER_PART_DOES_NOT_EXIST);
			}
			
			ChapterChapterPart chapterChapterPart = new ChapterChapterPart(chapter,chapterPart,j);
			
			if(chapterChapterPartRepository.save(chapterChapterPart)==null)
				return false;
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
	public ChapterPart editChapterPart(long chapterPartId, String english_text,String chinese_text, String dutch_text, long pictureId,int courseId) throws RequestException {
		ChapterPart part = partRepository.findOne(chapterPartId);
		
		if (part == null) {
			throw new RequestException(Error.CHAPTER_PART_DOES_NOT_EXIST);
		}
		
		if (english_text != null) {
			part.setEnglish_text(english_text);
		}
		
		if(chinese_text!=null) {
			part.setChinese_text(chinese_text);
		}
		
		if(dutch_text!=null) {
			part.setDutch_text(dutch_text);
		}
		
		if (pictureId != 0) {
			Picture picture = pictureRepository.findOne(pictureId);
			
			if (picture == null) {
				throw new RequestException(Error.PICTURE_DOES_NOT_EXIST);
			}
			
			part.setPicture(picture);
		}
		
		Course course = get(courseId);
		
		if(course!=null)
			part.setRelationCourse(course);
		
		partRepository.save(part);
		
		return part;
	}

	/**
	 * Delete a chapter part
	 * @param partId
	 */
	public boolean deleteChapterPart(long chapterId ,long partId) {
		//TODO: UNSAFE, FIRST NEED TO MANAGE LIST IN Chapter
		
		//partRepository.delete(partId);
		List<ChapterChapterPart> chapterChapterParts = chapterChapterPartRepository.getByChapterId(chapterId);
		
		int j = 1;
		
		chapterChapterPartRepository.deleteChapterPartFromChapterChapterPartByChapterId(chapterId);
		
		Chapter chapter = chapterRepository.findOne(chapterId);
		
		for(int i=0;i<chapterChapterParts.size();i++) {
			
			if(chapterChapterParts.get(i).getChapterPart().getId()!=partId) {
				ChapterPart chapterPart = chapterChapterParts.get(i).getChapterPart();
				ChapterChapterPart chapterChapterPart = new ChapterChapterPart(chapter,chapterPart,j);
				chapterChapterPartRepository.save(chapterChapterPart);
				j++;
			}
		}
		partRepository.delete(partId);
		
		return true;
	}
	
	/**
	 * get list of chapterParts by chapterId
	 * @author 曹将将
	 * @param chapterId
	 * 
	 * @return
	 * @throws RequestException 
	 * 
	 * */
	public String getChapterPartList(long chapterId,int type) throws RequestException{
		
		List<ChapterChapterPart> chapterChapterParts = chapterChapterPartRepository.getByChapterId(chapterId);
		
		if(chapterChapterParts==null) {
			throw new RequestException(Error.CHAPTER_PART_DOES_NOT_EXIST);
		}
		
		JSONObject jsonObject = new JSONObject();
			
		for(int i =0;i<chapterChapterParts.size();i++) {
			JSONObject jsonObject2 = new JSONObject();
				
			jsonObject2.put("id", chapterChapterParts.get(i).getChapterPart().getId());
			
			if(chapterChapterParts.get(i).getChapterPart().getPicture()!=null) {
				jsonObject2.put("pictureId",  chapterChapterParts.get(i).getChapterPart().getPicture().getId());
				jsonObject2.put("pictureLocation",  chapterChapterParts.get(i).getChapterPart().getPicture().getFileLocation());
			}
				
			switch (type) {
			case 2:{
				if(!chapterChapterParts.get(i).getChapterPart().getChinese_text().equalsIgnoreCase(""))
					jsonObject2.put("text",  chapterChapterParts.get(i).getChapterPart().getChinese_text());
				else 
					jsonObject2.put("text",  chapterChapterParts.get(i).getChapterPart().getEnglish_text());
				break;
			}
			case 3:{
				if(!chapterChapterParts.get(i).getChapterPart().getDutch_text().equalsIgnoreCase(""))
					jsonObject2.put("text",  chapterChapterParts.get(i).getChapterPart().getDutch_text());
				else 
					jsonObject2.put("text",  chapterChapterParts.get(i).getChapterPart().getEnglish_text());
				break;
			}

			default:jsonObject2.put("text",  chapterChapterParts.get(i).getChapterPart().getEnglish_text());
				break;
			}
				
			jsonObject.put(i+"", jsonObject2);
		}
			
		return jsonObject.toString();
			
	}
	
	
	
	/**
	 * get list of chapterParts by chapterId for web
	 * @author 曹将将
	 * @param chapterId
	 * 
	 * @return
	 * @throws RequestException 
	 * 
	 * */
	public String getChapterPartListWeb(long chapterId) throws RequestException{
		
		List<ChapterChapterPart> chapterChapterParts = chapterChapterPartRepository.getByChapterId(chapterId);
		
		if(chapterChapterParts==null) {
			throw new RequestException(Error.CHAPTER_PART_DOES_NOT_EXIST);
		}
		
		JSONObject jsonObject = new JSONObject();
			
		for(int i =0;i<chapterChapterParts.size();i++) {
			JSONObject jsonObject2 = new JSONObject();
				
			jsonObject2.put("id", chapterChapterParts.get(i).getChapterPart().getId());
			
			if(chapterChapterParts.get(i).getChapterPart().getPicture()!=null) {
				jsonObject2.put("pictureId",  chapterChapterParts.get(i).getChapterPart().getPicture().getId());
				jsonObject2.put("pictureLocation",  chapterChapterParts.get(i).getChapterPart().getPicture().getFileLocation());
			}
			
			jsonObject2.put("english_text",  chapterChapterParts.get(i).getChapterPart().getEnglish_text());
			jsonObject2.put("chinese_text",  chapterChapterParts.get(i).getChapterPart().getChinese_text());
			jsonObject2.put("dutch_text",  chapterChapterParts.get(i).getChapterPart().getDutch_text());
				
			jsonObject.put(i+"", jsonObject2);
		}
			
		return jsonObject.toString();
			
	}
	
	/**
	 * get chapterPart by chapterPartId
	 * @author 曹将将
	 * 
	 * @param chapterPart
	 * @return
	 * */
	public ChapterPart getChapterPart(long chapterPartId) {		
		return partRepository.findOne(chapterPartId);
	}
	
	

}
