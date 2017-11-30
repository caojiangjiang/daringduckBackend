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
import cn.daringduck.communitybuilder.model.Class;
import cn.daringduck.communitybuilder.model.User;
import cn.daringduck.communitybuilder.service.ClassService;
import cn.daringduck.communitybuilder.service.UserService;

/**
 * 
 * @author Letitia, Jochem Ligtenberg
 *
 */

@Path("/classes")
public class ClassController extends GenericController{
	
	ClassService classService;
	UserService userService;
	
	public ClassController(@Context ServletContext context) {
		super(context);
		this.classService = (ClassService) context.getAttribute("classService");
		this.userService = (UserService) context.getAttribute("userService");
	}
	
	/**
	 * Get all the classes from the database
	 * 
	 * @return
	 * @throws RequestException 
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response classes(@HeaderParam("Auth-Token") String token, @QueryParam("page") int page) throws RequestException {
		secure(token, "admin");
		
		Page<Class> classes = classService.getPage(page);
		return Response.status(Response.Status.OK).entity(classes).build();
	}
	
	/**
	 * Get information about the user with id
	 * @throws RequestException 
	 */
	@GET
	@Path("/{id: [0-9]*}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getClass(@HeaderParam("Auth-Token") String token, @PathParam("id") int id) throws RequestException {
		secure(token, "admin");
		
		Class theClass = classService.get(id);
		return Response.status(Response.Status.OK).entity(theClass).build();
	}
	
	/**
	 * Create a new Class
	 * @throws RequestException 
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response createClass(@HeaderParam("Auth-Token") String token, @FormParam("code") String code,
			@FormParam("name") String name, @FormParam("club") int clubId) throws RequestException {
		secure(token, "admin");
		
		Class theClass = classService.createClass(code, name, clubId);
		return Response.status(Response.Status.OK).entity(theClass).build();
	}
	
	/**
	 * Edit a class
	 * @throws RequestException 
	 */
	@PUT
	@Path("/{id: [0-9]*}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response editClass(@HeaderParam("Auth-Token") String token, @PathParam("id") int id, @QueryParam("code") String code, 
			@QueryParam("name") String name, @QueryParam("club") int clubId) throws RequestException {
		secure(token, "admin");
		
		Class theClass = classService.updateClass(id, code, name, clubId);
		return Response.status(Response.Status.OK).entity(theClass).build();
	}
	
	/**
	 * Get all the available members of a class
	 * 
	 * @return
	 * @throws RequestException 
	 */
	@GET
	@Path("/{id: [0-9]*}/availableMembers")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getAvailableMembers(@HeaderParam("Auth-Token") String token, @PathParam("id") int id, @QueryParam("page") int page) throws RequestException {
		secure(token, "admin");
		
		Page<User> availableMembers = classService.getAvailableMembers(id, page);
		return Response.status(Response.Status.OK).entity(availableMembers).build();
	}
	
	/**
	 * Get members of class with id
	 * @throws RequestException 
	 */
	@GET
	@Path("/{id: [0-9]*}/members")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response classMembers(@HeaderParam("Auth-Token") String token, @PathParam("id") int id) throws RequestException {
		secure(token, "admin");
		
		Class theClass = classService.get(id);
		return Response.status(Response.Status.OK).entity(theClass.getMembers()).build();
	}

	/**
	 * Add a member to a class
	 */
	@POST
	@Path("/{class: [0-9]*}/members/")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response addMember(@HeaderParam("Auth-Token") String token, @FormParam("user") long userId, 
			@PathParam("class") int classId) throws RequestException {

		secure(token, "admin");
		
		// Get the class
		Class theClass = classService.get(classId);
		
		// Get the user
		User member = userService.get(userId);
		
		classService.addMember(theClass, member);

		// Return the class with the new member
		return Response.status(Response.Status.OK).entity(theClass).build();	
	}
	
	/**
	 * Remove a member from a class
	 * @throws RequestException 
	 */
	@DELETE
	@Path("/{class: [0-9]*}/members/{member: [0-9]*}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response removeMember(@HeaderParam("Auth-Token") String token, @PathParam("member") long userId, 
			@PathParam("class") int classId) throws RequestException {

		secure(token, "admin");
		
		// Get the class
		Class theClass = classService.get(classId);
		
		// Get the user
		User member = userService.get(userId);
		
		classService.removeMember(theClass, member);
		
		return Response.status(Response.Status.OK).entity(theClass).build();	
	}
	
}
