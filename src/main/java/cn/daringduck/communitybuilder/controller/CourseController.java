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
	public Response courses(@QueryParam("page") int page) throws RequestException {
		String courses = courseService.getPageOfCourse(page);
		return Response.status(Response.Status.OK).entity(courses).build();
	}

	/**
	 * Get the course with id
	 * @throws RequestException 
	 */
	@GET
	@Path("/{id: [0-9]*}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response course(@PathParam("id") int id) throws RequestException {
		String course = courseService.getCourse(id);
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
	public Response addCourse(@HeaderParam("Auth-Token") String token, @FormParam("name") String name,@FormParam("pictureId")long pictureId)
			throws RequestException {
		secure(token, "admin");

		Course course = courseService.addCourse(name,pictureId);
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
	public Response editCourse(@HeaderParam("Auth-Token") String token, @PathParam("id") int id, @QueryParam("name") String name,@QueryParam("pictureId") long pictureId)
			throws RequestException {
		secure(token, "admin");
		Course course = courseService.editCourse(id, name,pictureId);
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
	public Response chapters(@PathParam("courseId") int id) throws RequestException {
		
		return Response.status(Response.Status.OK).entity(courseService.getChapters(id)).build();
	}
	
	/**
	 * Get a chapter
	 */
	@GET
	@Path("/{courseId: [0-9]*}/chapters/{chapterId: [0-9]*}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response chapter(@PathParam("chapterId") long id) {
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
			@FormParam("title") String title,@FormParam("requiredOrNot")  String requiredOrNot) throws RequestException {
		secure(token, "admin");
		Chapter chapter = courseService.addChapterStep1(courseId, title,requiredOrNot);
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
		Boolean result = courseService.addChapterStep2(courseId, lists);
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
			@QueryParam("title") String title,@QueryParam("requiredOrNot") String requiredOrNot) throws RequestException {
		secure(token, "admin");
		Chapter chapter = courseService.editChapter(chapterId, title,requiredOrNot);
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
	 */
	@GET
	@Path("/getChapterPartList/{chapterId: [0-9]*}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getChapterPartList(@PathParam("chapterId") long chapterId) {
		
		List<ChapterPart> chapterParts = courseService.getChapterPartList(chapterId);
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
		secure(token, "member");
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
			@FormParam("text") String text, @FormParam("pictureId") long pictureId) throws RequestException {
		secure(token, "admin");

		ChapterPart part = courseService.addChapterPartStep1(chapterId, text, pictureId);
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

		Boolean result = courseService.addChapterPartStep2(chapterId, lists);
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
			@QueryParam("text") String text, @QueryParam("pictureId") long pictureId) throws RequestException {
		secure(token, "admin");

		ChapterPart part = courseService.editChapterPart(chapterPartId, text, pictureId);
		
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
