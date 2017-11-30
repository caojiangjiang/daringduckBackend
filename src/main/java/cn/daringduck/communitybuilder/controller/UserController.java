package cn.daringduck.communitybuilder.controller;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.POST;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.data.domain.Page;

import java.rmi.AccessException;

import cn.daringduck.communitybuilder.RequestException;
import cn.daringduck.communitybuilder.model.Moment;
import cn.daringduck.communitybuilder.model.StudentsStudyStatus;
import cn.daringduck.communitybuilder.model.User;
import cn.daringduck.communitybuilder.service.StatusService;
import cn.daringduck.communitybuilder.service.UserService;

/**
 * This HelloWorld resource acts as a Resource on the REST server It will return
 * a simple JSON object with one key-value pair: msg => 'Hello world!'
 * 
 * @author Jochem Ligtenberg
 */
@Path("/users")
public class UserController extends GenericController {

	ServletContext context;
	private UserService userService;
	private StatusService statusService;
	

	
	public UserController(@Context ServletContext context) {
		super(context);
		this.userService = (UserService) context.getAttribute("userService");
		this.statusService = (StatusService)context.getAttribute("statusService");
	}

	/**
	 * Get all the users from the database
	 * 
	 * @return
	 * @throws AccessException
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response users(@HeaderParam("Auth-Token") String token, @QueryParam("page") int page)
			throws RequestException {
		secure(token, "admin");

		Page<User> users = userService.getPage(page);
		return Response.status(Response.Status.OK).entity(users).build();
	}

	/**
	 * Create a new user
	 * @throws RequestException 
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response createUser(@HeaderParam("Auth-Token") String token, @FormParam("username") String username,
			@FormParam("password") String password, @FormParam("nickname") String nickname,
			@FormParam("gender") int gender, @FormParam("phone") String phone, @FormParam("wechat") String wechat,
			@FormParam("email") String email, @FormParam("role") int roleId,@FormParam("pictureId") long pictureId, @QueryParam("club") int clubId) throws RequestException {

		secure(token, "admin");
		// Get the user who is doing the request
		User callingUser = getUser(token);

		// Only admin can make users with a different role
		if (callingUser == null || !callingUser.getRole().getName().toUpperCase().equals("ADMIN")) {
			roleId = 1;
		}

		User user = userService.addUser(username, password, gender, nickname, phone, wechat, email,pictureId, roleId, clubId);

		// Return the newly created user to the RESTful service
		return Response.status(Response.Status.OK).entity(user).build();
	}

	/**
	 * Get information about the user with id
	 * 
	 * @throws AccessException
	 */
	@GET
	@Path("/{id: [0-9]*}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response user(@HeaderParam("Auth-Token") String token, @PathParam("id") long id) throws RequestException {
		secure(token, "admin");

		User user = userService.get(id);
		return Response.status(Response.Status.OK).entity(user).build();
	}

	/**
	 * Update the information for the user with id
	 * 
	 * @throws RequestException
	 */
	@PUT
	@Path("/{id: [0-9]*}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response changeUser(@HeaderParam("Auth-Token") String token, @PathParam("id") long id,
			@QueryParam("username") String username, @QueryParam("password") String password,
			@QueryParam("nickname") String nickname, @QueryParam("gender") int gender,
			@QueryParam("phone") String phone, @QueryParam("wechat") String wechat, @QueryParam("email") String email,
			@QueryParam("disabled") int disabled, @QueryParam("role") int roleId,@QueryParam("pictureId") long pictureId, @QueryParam("club") int clubId) throws RequestException {

		secure(token, "admin");

		// Get the user data using the entity manager
		User user = userService.updateUserWithId(id, username, password, nickname, gender, phone, wechat, email,
				disabled,pictureId, roleId, clubId);

		// Return the user data to the RESTful service
		return Response.status(Response.Status.OK).entity(user).build();
	}

	/**
	 * Get friends of user with id
	 * 
	 * @throws RequestException
	 */
	@GET
	@Path("/{id: [0-9]*}/friends")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response userFriends(@HeaderParam("Auth-Token") String token, @PathParam("id") long id)
			throws RequestException {
		secure(token, "admin");

		User user = userService.get(id);
		return Response.status(Response.Status.OK).entity(user.getFriends()).build();
	}

	/**
	 * Get classes of user with id
	 * 
	 * @throws RequestException
	 */
	@GET
	@Path("/{id: [0-9]*}/classes")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response userClasses(@HeaderParam("Auth-Token") String token, @PathParam("id") long id)
			throws RequestException {
		secure(token, "admin");

		User user = userService.get(id);
		return Response.status(Response.Status.OK).entity(user.getClasses()).build();
	}

	/////////////
	// MOMENTS //
	/////////////

	/**
	 * Get moments of user with id
	 * 
	 * @throws RequestException
	 */
	@GET
	@Path("/{id: [0-9]*}/moments")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response userMoments(@HeaderParam("Auth-Token") String token, @PathParam("id") long id,
			@QueryParam("page") int page) throws RequestException {
		secure(token, "admin");

		return Response.status(Response.Status.OK).entity(userService.getUserMoments(id, page)).build();
	}
	

	/**
	 * Get moment of user with id
	 * 
	 * @throws RequestException
	 */
	@GET
	@Path("/{userId: [0-9]*}/moments/{id: [0-9]*}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response userMoment(@HeaderParam("Auth-Token") String token, @PathParam("userId") long userId, @PathParam("id") long id,
			@QueryParam("page") int page) throws RequestException {
		secure(token, "admin");

		return Response.status(Response.Status.OK).entity(userService.getUserMoment(userId, id)).build();
	}

	/**
	 * Add a moment for user with id
	 */
	@POST
	@Path("/{id: [0-9]*}/moments")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response userAddMoment(@HeaderParam("Auth-Token") String token, @PathParam("id") long userId,
			@FormParam("title") String title, @FormParam("privacy") String privacy,@FormParam("eventDate") String eventDate) throws RequestException {
		secure(token, "*");
		
		Moment moment = userService.addUserMoment(userId, title, privacy,eventDate);
		
		return Response.status(Response.Status.OK).entity(moment).build();
	}

	////////
	// ME //
	////////

	/**
	 * Get the information for the logged in user
	 * 
	 * @throws RequestException
	 */
	@GET
	@Path("/me")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response me(@HeaderParam("Auth-Token") String token) throws RequestException {
		secure(token, "*");

		// Get the user data using the entity manager
		User user = userService.findUserByAuthToken(token);

		// Return the user data to the RESTful service
		return Response.status(Response.Status.OK).entity(user).build();
	}

	/**
	 * Update the information for the logged in user
	 * 
	 * @throws RequestException
	 */
	@PUT
	@Path("/me")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response changeMe(@HeaderParam("Auth-Token") String token, @HeaderParam("password") String password,
			@HeaderParam("nickname") String nickname, @HeaderParam("gender") int gender,
			@HeaderParam("phone") String phone, @HeaderParam("wechat") String wechat,
			@HeaderParam("email") String email) throws RequestException {

		secure(token, "*");

		// Get the user data using the entity manager
		User user = userService.findUserByAuthToken(token);

		userService.updateUserWithId(user.getId(), user.getUsername(), password, nickname, gender, phone, wechat, email,
				user.isDisabled() ? 1 : 0, 0, 0,0);

		// Return the user data to the RESTful service
		return Response.status(Response.Status.OK).entity(user).build();
	}

	/**
	 * Get the friends of the logged in user
	 * 
	 * @throws RequestException
	 */
	@GET
	@Path("/me/friends")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response meFriends(@HeaderParam("Auth-Token") String token) throws RequestException {
		// Get the user data using the entity manager
		secure(token, "*");

		User user = userService.findUserByAuthToken(token);

		// Return the user data to the RESTful service
		return Response.status(Response.Status.OK).entity(user.getFriends()).build();
	}

	/**
	 * get the result of students' class chapter
	 * 
	 */
	@GET
	@Path("/chapter/getStudentsStudyStatus")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getStudentsStudyStatus(@HeaderParam("Auth-Token")String token,@QueryParam("userId")long userId,@QueryParam("classId")long classId,@QueryParam("chapterId")long chapterId) {
		StudentsStudyStatus studentsStudyStatus =  statusService.getstudentsChapterStatus(userId, classId, chapterId);
		return Response.status(Response.Status.OK).entity(studentsStudyStatus).build();
	}
	
	/**
	 * set the result of students' class chapter
	 * @throws RequestException 
	 * 
	 */
	@POST
	@Path("/chapter/setStudentsStudyStatus")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response setStudentsStudyStatus(@HeaderParam("Auth-Token")String token,@FormParam("userId")long userId,@FormParam("classId")long classId,@FormParam("chapterId")long chapterId,@FormParam("status")int status) throws RequestException {
		secure(token, "teacher");
		
		StudentsStudyStatus studentsStudyStatus = new StudentsStudyStatus();
		studentsStudyStatus =  statusService.setstudentsChapterStatus(userId, classId, chapterId,status);
		return Response.status(Response.Status.OK).entity(studentsStudyStatus).build();
	}
}
