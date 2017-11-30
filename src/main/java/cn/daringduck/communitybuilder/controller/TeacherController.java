package cn.daringduck.communitybuilder.controller;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.data.domain.Page;

import cn.daringduck.communitybuilder.RequestException;
import cn.daringduck.communitybuilder.model.User;
import cn.daringduck.communitybuilder.service.TeacherService;

/**
 * This HelloWorld resource acts as a Resource on the REST server It will return
 * a simple JSON object with one key-value pair: msg => 'Hello world!'
 * 
 * @author Jochem Ligtenberg
 */
@Path("/teachers")
public class TeacherController extends GenericController {

	private final TeacherService teacherService;
	ServletContext context;
	
	public TeacherController(@Context ServletContext context) {
		super(context);
		this.context = context;
		this.teacherService = (TeacherService) context.getAttribute("teacherService");
	}

	/**
	 * Get all the teacher from the database
	 * 
	 * @return
	 * @throws RequestException 
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response teachers(@HeaderParam("Auth-Token") String token, @QueryParam("page") int page) throws RequestException {
		
		secure(token, "admin");

		Page<User> teachers = teacherService.getTeachers(page);
		
		return Response.status(Response.Status.OK).entity(teachers).build();
	}
	
}
