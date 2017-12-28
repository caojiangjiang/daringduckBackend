package cn.daringduck.communitybuilder.controller;

import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import cn.daringduck.communitybuilder.RequestException;
import cn.daringduck.communitybuilder.model.Chapter;
import cn.daringduck.communitybuilder.model.ChapterPart;
import cn.daringduck.communitybuilder.model.Course;
import cn.daringduck.communitybuilder.service.CourseService;

/**
 * This HelloWorld resource acts as a Resource on the REST server It will return
 * a simple JSON object with one key-value pair: msg => 'Hello world!'
 * 
 * @author Jochem Ligtenberg
 */
@Path("/courses")
public class CourseController extends GenericController {

	private final CourseService courseService;
	ServletContext context;

	public CourseController(@Context ServletContext context) {
		super(context);
		this.context = context;
		this.courseService = (CourseService) context.getAttribute("courseService");
	}

	////////////////////////////////////////////////////////////////////
	// GENERAL
	////////////////////////////////////////////////////////////////////

	/**
	 * Get a page with courses
	 * 
	 * @return
	 * @throws RequestException
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response courses(@QueryParam("page") int page,@QueryParam("type") int type) throws RequestException {
		String courses = courseService.getPageOfCourse(page,type);
		return Response.status(Response.Status.OK).entity(courses).build();
	}

	/**
	 * Get the course with id
	 * @throws RequestException 
	 */
	@GET
	@Path("/{id: [0-9]*}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response course(@PathParam("id") int id,@QueryParam("type") int type) throws RequestException {
		String course = courseService.getCourse(id,type);
		return Response.status(Response.Status.OK).entity(course).build();
	}
	
	
	
	/**
	 * Get the course with id(for search)
	 * @throws RequestException 
	 */
	@GET
	@Path("/searchCourse")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response searchCourse(@QueryParam("name") String name,@QueryParam("type") int type) throws RequestException {
		String course = courseService.searchCourse(name,type);
		return Response.status(Response.Status.OK).entity(course).build();
	}

	/**
	 * Add a course
	 * 
	 * @throws RequestException
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response addCourse(@HeaderParam("Auth-Token") String token, @FormParam("english_name") String english_name,@FormParam("chinese_name") String chinese_name,@FormParam("dutch_name") String dutch_name,@FormParam("pictureId")long pictureId)
			throws RequestException {
		secure(token, "admin");
		
		//add course
		Course course = courseService.addCourse(english_name,chinese_name,dutch_name,pictureId);
		
		//Return the result course to the RESTful service
		return Response.status(Response.Status.OK).entity(course).build();
	}

	/**
	 * Edit a course
	 * 
	 * @throws RequestException
	 */
	@PUT
	@Path("/{id: [0-9]*}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response editCourse(@HeaderParam("Auth-Token") String token, @PathParam("id") int id, @QueryParam("english_name") String english_name,@QueryParam("chinese_name") String chinese_name,@QueryParam("dutch_name") String dutch_name,@QueryParam("pictureId") long pictureId)
			throws RequestException {
		secure(token, "admin");
		Course course = courseService.editCourse(id, english_name,chinese_name,dutch_name,pictureId);
		return Response.status(Response.Status.OK).entity(course).build();
	}
	
	/**
	 * Delete a course
	 * 
	 * @throws RequestException
	 */
	@DELETE
	@Path("/{courseId: [0-9]*}")
	public Response deleteCourse(@HeaderParam("Auth-Token") String token, @PathParam("courseId") int courseId)
			throws RequestException {
		secure(token, "admin");
		return Response.status(Response.Status.OK).entity(courseService.deleteCourse(courseId)).build();
	}

	////////////////////////////////////////////////////////////////////
	// Chapter
	////////////////////////////////////////////////////////////////////

	/**
	 * Get chapters in a course
	 * @throws RequestException 
	 */
	@GET
	@Path("/{courseId: [0-9]*}/chapters/")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response chapters(@HeaderParam("Auth-Token") String token,@PathParam("courseId") int id,@QueryParam("type") int type) throws RequestException {
//		secure(token, "*");
		return Response.status(Response.Status.OK).entity(courseService.getChapters(id,type)).build();
	}
	
	/**
	 * Get a chapter
	 * @throws RequestException 
	 */
	@GET
	@Path("/{courseId: [0-9]*}/chapters/{chapterId: [0-9]*}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response chapter(@HeaderParam("Auth-Token") String token,@PathParam("chapterId") long id) throws RequestException {
//		secure(token, "*");
		Chapter chapter = courseService.getChapter(id);
		return Response.status(Response.Status.OK).entity(chapter).build();
	}
	
	
	/**
	 * Add a chapter to a course
	 * 
	 * @throws RequestException
	 */
	@POST
	@Path("/addChapterStep1/{courseId: [0-9]*}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response addChapterStep1(@HeaderParam("Auth-Token") String token, @PathParam("courseId") int courseId,
			@FormParam("english_title") String english_title,@FormParam("chinese_title") String chinese_title,@FormParam("dutch_title") String dutch_title,@FormParam("requiredOrNot")  String requiredOrNot) throws RequestException {
		secure(token, "admin");
		Chapter chapter = courseService.addChapterStep1(courseId,english_title,chinese_title,dutch_title,requiredOrNot);
		return Response.status(Response.Status.OK).entity(chapter).build();
	}
	
	/**
	 * Add a chapter to a course
	 * 
	 * @throws RequestException
	 */
	@POST
	@Path("/addChapterStep2/{courseId: [0-9]*}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response addChapterStep2(@HeaderParam("Auth-Token") String token, @PathParam("courseId") int courseId,
			@FormParam("lists") String lists) throws RequestException {
		secure(token, "admin");
		boolean result = courseService.addChapterStep2(courseId, lists);
		return Response.status(Response.Status.OK).entity(result).build();
	}
	
	/**
	 * Edit a chapter
	 * 
	 * @throws RequestException
	 */
	@PUT
	@Path("/{courseId: [0-9]*}/chapters/{chapterId: [0-9]*}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response editChapter(@HeaderParam("Auth-Token") String token, @PathParam("chapterId") long chapterId,
			@QueryParam("english_title") String english_title,@QueryParam("chinese_title") String chinese_title,@QueryParam("dutch_title") String dutch_title,@QueryParam("requiredOrNot") String requiredOrNot) throws RequestException {
		secure(token, "admin");
		Chapter chapter = courseService.editChapter(chapterId, english_title,chinese_title,dutch_title,requiredOrNot);
		return Response.status(Response.Status.OK).entity(chapter).build();
	}

	/**
	 * Delete a chapter
	 * 
	 * @throws RequestException
	 */
	@DELETE
	@Path("/{courseId: [0-9]*}/chapters/{chapterId: [0-9]*}")
	public Response deleteChapter(@HeaderParam("Auth-Token") String token,@PathParam("courseId")int courseId, @PathParam("chapterId") long chapterId)
			throws RequestException {
		secure(token, "admin");
		return Response.status(Response.Status.OK).entity(courseService.deleteChapter(courseId , chapterId)).build();
	}
	
//	/**
//	 * set a chapter required or not
//	 * */
//	@PUT
//	@Path("/chapterRequiredOrNot/{chapterId: [0-9]*}")
//	public Response chapterRequiredOrNot(@HeaderParam("Auth-Token") String token,@PathParam("chapterId") long chapterId,@FormParam("requiredOrNot") String requiredOrNot)
//			throws RequestException {
//		secure(token, "teacher");
//		Chapter chapter = courseService.chapterRequiredOrNot(chapterId,requiredOrNot);
//		return Response.status(Response.Status.OK).entity(chapter).build();
//	}
	
	////////////////////////////////////////////////////////////////////
	// Chapter Part
	////////////////////////////////////////////////////////////////////
	
	/**
	 * Get  chapterPart list
	 * @throws RequestException 
	 */
	@GET
	@Path("/getChapterPartList/{chapterId: [0-9]*}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getChapterPartList(@HeaderParam("Auth-Token") String token,@PathParam("chapterId") long chapterId,@QueryParam("type") int type) throws RequestException {
		//set who have the authority to do use this api
		//String members[] = {"teacher","admin","member","parent"};
		
		//judge whether the user have the permission to get the UserCourses
		secure(token, "*");
		
		String chapterParts = courseService.getChapterPartList(chapterId,type);
		
		return Response.status(Response.Status.OK).entity(chapterParts).build();
	}
	
	
	/**
	 * Get a chapterPart
	 * 
	 * @throws RequestException 
	 */
	@GET
	@Path("/getChapterPart/{chapterPartId: [0-9]*}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getChapterPart(@HeaderParam("Auth-Token") String token,@PathParam("chapterPartId") long chapterPartId) throws RequestException {
		//set who have the authority to do use this api
		//String members[] = {"teacher","admin","member","parent"};
		
		//judge whether the user have the permission to get the UserCourses
		secure(token, "*");
		
		ChapterPart chapterPart = courseService.getChapterPart(chapterPartId);
		
		return Response.status(Response.Status.OK).entity(chapterPart).build();
	}
	
	
	/**
	 * Add a chapterPart to a chapter
	 * 
	 * @throws RequestException
	 */
	@POST
	@Path("/addChapterPartStep1/{chapterId: [0-9]*}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response addChapterPartStep1(@HeaderParam("Auth-Token") String token, @PathParam("chapterId") long chapterId,
			@FormParam("english_text") String english_text,@FormParam("chinese_text") String chinese_text,@FormParam("dutch_text") String dutch_text, @FormParam("pictureId") long pictureId) throws RequestException {
		secure(token, "admin");

		ChapterPart part = courseService.addChapterPartStep1(chapterId, english_text,chinese_text,dutch_text, pictureId);
		return Response.status(Response.Status.OK).entity(part).build();
	}
	
	/**
	 * Add a chapterPart to a chapter
	 * 
	 * @throws RequestException
	 */
	@POST
	@Path("/addChapterPartStep2/{chapterId: [0-9]*}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response addChapterPartStep2(@HeaderParam("Auth-Token") String token, @PathParam("chapterId") long chapterId,
			@FormParam("lists") String lists) throws RequestException {
		secure(token, "admin");

		boolean result = courseService.addChapterPartStep2(chapterId, lists);
		return Response.status(Response.Status.OK).entity(result).build();
	}
	
	
	/**
	 * Edit a chapterPart
	 * 
	 * @throws RequestException
	 */
	@PUT
	@Path("/chapters/{chapterId: [0-9]*}/parts/{chapterPartId: [0-9]*}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response editChapterPart(@HeaderParam("Auth-Token") String token, @PathParam("chapterPartId") long chapterPartId,
			@QueryParam("english_text") String english_text,@QueryParam("chinese_text") String chinese_text,@QueryParam("dutch_text") String dutch_text,@QueryParam("pictureId") long pictureId) throws RequestException {
		secure(token, "admin");

		ChapterPart part = courseService.editChapterPart(chapterPartId, english_text,chinese_text,dutch_text, pictureId);
		
		return Response.status(Response.Status.OK).entity(part).build();
	}
	
	/**
	 * Delete a chapterPart
	 * 
	 * @throws RequestException
	 */
	@DELETE
	@Path("/deleteChapterPart/{chapterId: [0-9]*}/parts/{chapterPartId: [0-9]*}")
	public Response deleteChapterPart(@HeaderParam("Auth-Token") String token,@PathParam("chapterId") long chapterId, @PathParam("chapterPartId") long partId)
			throws RequestException {
		secure(token, "admin");
		return Response.status(Response.Status.OK).entity(courseService.deleteChapterPart(chapterId,partId)).build();
	}
	
}
