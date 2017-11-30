package cn.daringduck.communitybuilder.controller;

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

import org.springframework.data.domain.Page;

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
		Page<Course> courses = courseService.getPage(page);
		return Response.status(Response.Status.OK).entity(courses).build();
	}

	/**
	 * Get the course with id
	 */
	@GET
	@Path("/{id: [0-9]*}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response course(@PathParam("id") int id) {
		Course course = courseService.get(id);
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
	public Response addCourse(@HeaderParam("Auth-Token") String token, @FormParam("name") String name)
			throws RequestException {
		secure(token, "admin");

		Course course = courseService.addCourse(name);
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
	public Response editCourse(@HeaderParam("Auth-Token") String token, @PathParam("id") int id, @QueryParam("name") String name)
			throws RequestException {
		secure(token, "admin");

		Course course = courseService.editCourse(id, name);
		return Response.status(Response.Status.OK).entity(course).build();
	}
	
	/**
	 * Delete a course
	 * 
	 * @throws RequestException
	 */
	@DELETE
	@Path("/{id: [0-9]*}")
	public Response deleteCourse(@HeaderParam("Auth-Token") String token, @PathParam("id") int id)
			throws RequestException {
		secure(token, "admin");

		courseService.deleteCourse(id);
		return Response.status(Response.Status.OK).build();
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
	@Path("/{id: [0-9]*}/chapters")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response addChapter(@HeaderParam("Auth-Token") String token, @PathParam("id") int id,
			@FormParam("title") String title) throws RequestException {
		secure(token, "admin");

		Chapter chapter = courseService.addChapter(id, title);
		return Response.status(Response.Status.OK).entity(chapter).build();
	}
	
	/**
	 * Edit a chapter
	 * 
	 * @throws RequestException
	 */
	@PUT
	@Path("/{courseId: [0-9]*}/chapters/{chapterId: [0-9]*}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response editChapter(@HeaderParam("Auth-Token") String token, @QueryParam("chapterId") long chapterId,
			@QueryParam("title") String title) throws RequestException {
		secure(token, "admin");

		Chapter chapter = courseService.editChapter(chapterId, title);
		return Response.status(Response.Status.OK).entity(chapter).build();
	}

	/**
	 * Delete a chapter
	 * 
	 * @throws RequestException
	 */
	@DELETE
	@Path("/{courseId: [0-9]*}/chapters/{chapterId: [0-9]*}")
	public Response deleteChapter(@HeaderParam("Auth-Token") String token, @PathParam("chapterId") long chapterId)
			throws RequestException {
		secure(token, "admin");

		courseService.deleteChapter(chapterId);
		return Response.status(Response.Status.OK).build();
	}
	
	////////////////////////////////////////////////////////////////////
	// Chapter Part
	////////////////////////////////////////////////////////////////////
	/**
	 * Add a chapter part to a chapter
	 * 
	 * @throws RequestException
	 */
	@POST
	@Path("/{courseId: [0-9]*}/chapters/{chapterId: [0-9]*}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response addChapterPart(@HeaderParam("Auth-Token") String token, @PathParam("chapterId") long chapterId,
			@FormParam("text") String text, @FormParam("picture") long pictureId) throws RequestException {
		secure(token, "admin");

		ChapterPart part = courseService.addChapterPart(chapterId, text, pictureId);
		return Response.status(Response.Status.OK).entity(part).build();
	}
	
	/**
	 * Edit a chapter part
	 * 
	 * @throws RequestException
	 */
	@PUT
	@Path("/{courseId: [0-9]*}/chapters/{chapterId: [0-9]*}/parts/{chapterPartId: [0-9]*}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response editChapterPart(@HeaderParam("Auth-Token") String token, @PathParam("chapterPartId") long chapterPartId,
			@QueryParam("text") String text, @QueryParam("picture") long pictureId) throws RequestException {
		secure(token, "admin");

		ChapterPart part = courseService.editChapterPart(chapterPartId, text, pictureId);
		
		return Response.status(Response.Status.OK).entity(part).build();
	}
	
	/**
	 * Delete a chapter
	 * 
	 * @throws RequestException
	 */
	@DELETE
	@Path("/{courseId: [0-9]*}/chapters/{chapterId: [0-9]*}/parts/{chapterPartId: [0-9]*}")
	public Response deleteChapterPart(@HeaderParam("Auth-Token") String token, @PathParam("chapterPartId") long partId)
			throws RequestException {
		secure(token, "admin");

		courseService.deleteChapterPart(partId);
		return Response.status(Response.Status.OK).build();
	}
	
}
