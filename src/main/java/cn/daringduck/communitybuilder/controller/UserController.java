package cn.daringduck.communitybuilder.controller;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
import java.util.List;

import cn.daringduck.communitybuilder.RequestException;
import cn.daringduck.communitybuilder.model.Course;
import cn.daringduck.communitybuilder.model.Moment;
import cn.daringduck.communitybuilder.model.User;
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
	
	public UserController(@Context ServletContext context) {
		super(context);
		this.userService = (UserService) context.getAttribute("userService");
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
	 * 
	 * @return
	 * @throws RequestException 
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response createUser(@HeaderParam("Auth-Token") String token, @FormParam("username") String username,
			@FormParam("password") String password, @FormParam("nickname") String nickname,
			@FormParam("gender") int gender, @
			FormParam("phone") String phone, @FormParam("wechat") String wechat,
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
	 * @return
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
	 * get users by role Id
	 * 
	 * @return
	 * @throws RequestException 
	 **/
	@GET
	@Path("/getUserByRoleId/{roleId: [0-9]*}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getUserByRoleId(@HeaderParam("Auth-Token") String token, @PathParam("roleId") int roleId) throws RequestException {
	 
	  secure(token, "admin");
	 
	  return Response.status(Response.Status.OK).entity(userService.getUserByRoleId(roleId)).build();
	 
	}
	 
	
	/**
	 * Update the information for the user with id
	 * 
	 * @return
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
	 * Get moments of user with userId
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
	 * Get moment of user with momentId
	 * 
	 * @throws RequestException
	 */
	@GET
	@Path("/{userId: [0-9]*}/moments/{id: [0-9]*}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response userMoment(@HeaderParam("Auth-Token") String token, @PathParam("userId") long userId, @PathParam("id") long id,
			@QueryParam("page") int page) throws RequestException {
		
		secure(token, "admin");
		
		return Response.status(Response.Status.OK).entity(userService.getUserMoment(userId,id)).build();
	}

	
	
	/**
	 *user add a moment for himself
	 */
	@POST
	@Path("/{userId: [0-9]*}/moments")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response addUserMoment(@HeaderParam("Auth-Token") String token,
			@PathParam("userId") long userId,
			@FormParam("title") String title, @FormParam("privacy") String privacy,
			@FormParam("eventDate") String eventDate) throws RequestException {
		
		// set who have the authority to do use this api
		String members[] = {"teacher","admin"}; 
		
		//Verify user identity
		secure(token, members);
		
		User user = userService.get(userId);
		
		Moment moment = userService.addUserMoment(user.getId(), title, privacy,eventDate);
		
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
			@HeaderParam("email") String email) throws RequestException 
	{

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
	public Response meFriends(@HeaderParam("Auth-Token") String token,@QueryParam("page") int page) throws RequestException 
	{
		// Get the user data using the entity manager
		secure(token, "*");

		User user = userService.findUserByAuthToken(token);

		// Return the user data to the RESTful service
		return Response.status(Response.Status.OK).entity(user.getFriends()).build();
	}
	
	
	/**
	 * get the courses of a user
	 * 
	 * @throws RequestException
	 */
	@GET
	@Path("/getMyCourse")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getMyCourse(@HeaderParam("Auth-Token") String token,@QueryParam("page") int page,@QueryParam("type") int type) throws RequestException
	{
		secure(token, "*");
		//judge whether the user have the permission to get the UserCourses
		User user = userService.findUserByAuthToken(token);
		
		//get userCourse and accroding to the type to decide the language(1.english 2.chinese 3.dutch)
		String userCourse = userService.getUserCourse(user.getId(),type);
		
		//return the result to the RESTful service
		return Response.status(Response.Status.OK).entity(userCourse).build();
		
	}
	
	/**
	 * get courses that user does not have
	 * 
	 * @throws RequestException
	 */
	@GET
	@Path("/getCourseAvaliable/{userId: [0-9]*}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getCourseAvaliable(@HeaderParam("Auth-Token") String token,@PathParam("userId") long userId,@QueryParam("page") int page,@QueryParam("type") int type) throws RequestException
	{
		// set who have the authority to do use this api
		String members[] = {"teacher","admin"}; 
		
		//Verify user identity
		secure(token, members);
		
		User user = userService.get(userId);
		
		//get userCourse and accroding to the type to decide the language(1.english 2.chinese 3.dutch)
		String courses = userService.getCourseAvaliable(user.getId(),page,type);
		
		//return the result to the RESTful service
		return Response.status(Response.Status.OK).entity(courses).build();
		
	}
	
	  /**
	   * get my chapters 
	   *
	   * @throws RequestException
	   */
	  @GET
	  @Path("/getMyChapter")
	  @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	  public Response getMyChapter(@HeaderParam("Auth-Token") String token,@QueryParam("courseId") int courseId,@QueryParam("page") int page,@QueryParam("type") int type) throws RequestException
	  {	
		  	secure(token, "*");
			//judge whether the user have the permission to get the UserCourses
			User user = userService.findUserByAuthToken(token);
			
			//get userChapter
			String userChapter = userService.getUserChapter(user.getId(),courseId,type);
			
			//Return the user data to the RESTful service
			return Response.status(Response.Status.OK).entity(userChapter).build();
	  }
	
	///////////
	// course //
	///////////
	
	/**
	 *add user course
	 * 
	 * @throws RequestException
	 */
	@POST
	@Path("/addUserCourse")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response addUserCourse(@HeaderParam("Auth-Token") String token,@FormParam("userId")long userId,@FormParam("courseId") int courseId,
			@FormParam("teacherId")long teacherId,@FormParam("passOrNot") boolean passOrNot,@FormParam("date") long date) throws RequestException 
	{
		// set who have the authority to do use this api
		String members[] = {"teacher","admin"}; 
		
		//Verify user identity
		secure(token, members);

		//add userCourse according to the given data 
		boolean userCourse = userService.addUserCourse(userId,courseId,teacherId,passOrNot,date);

		// Return the result to the RESTful service
		return Response.status(Response.Status.OK).entity(userCourse).build();
	}
	
	
	/**
	 * change the status of a user in a course
	 * 
	 * @throws RequestException
	 */
	@PUT
	@Path("/changeUserCourse")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response changeUserCourse(@HeaderParam("Auth-Token") String token, @FormParam("userId") long userId,
			@FormParam("courseId") int courseId, @FormParam("teacherId") long teacherId ,@FormParam("status") String status,
			@FormParam("date") long date) throws RequestException 
	{
		// set who have the authority to do use this api
		String members[] = {"teacher","admin"}; 
		
		//Verify user identity
		secure(token, members);
		
		//change userCourse according to the given data 
		boolean userCourse = userService.changeUserCourse(userId, courseId,teacherId,status,date);
		
		//return the result to the RESTful service
		return Response.status(Response.Status.OK).entity(userCourse).build();
	}
	

	
	/**
	 * get the courses of a user
	 * 
	 * @throws RequestException
	 */
	@GET
	@Path("/getUserCourse/{userId:[0-9]*}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getUserCourse(@HeaderParam("Auth-Token") String token,@PathParam("userId") long userId,@QueryParam("type") int type) throws RequestException
	{
		//set who have the authority to do use this api
		String members[] = {"teacher","admin"};
		
		//judge whether the user have the permission to get the UserCourses
		secure(token, members);
		
		//get userCourse
		String userCourse = userService.getUserCourse(userId,type);
		
		//return the result to the RESTful service
		return Response.status(Response.Status.OK).entity(userCourse).build();
		
	}
	
	
	/**
	 * get a course for a user
	 * 
	 * @throws RequestException
	 */
	@GET
	@Path("/getUserCourse/{userId:[0-9]*}/{courseId:[0-9]*}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getOneUserCourse(@HeaderParam("Auth-Token") String token,@PathParam("userId") long userId,@PathParam("courseId") int courseId,@QueryParam("type") int type) throws RequestException
	{
		//set who have the authority to do use this api
		String members[] = {"teacher","admin"};
		
		//judge whether the user have the permission to get the UserCourses
		secure(token, members);
		
		//get userCourse
		String userCourse = userService.getOneUserCourse(userId,courseId,type);
		
		//return the result to the RESTful service
		return Response.status(Response.Status.OK).entity(userCourse).build();
		
	}
	
	/**
	 * delete a course of a user
	 * 
	 * @throws RequestException
	 */
	@DELETE
	@Path("/deleteUserCourse/{userId: [0-9]*}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response deleteUserCourse(@HeaderParam("Auth-Token") String token,
			@PathParam("userId") long userId,@FormParam("courseId") int courseId) throws RequestException
	{
		//set who have the authority to do use this api
		String members[] = {"teacher","admin"};
		
		//judge whether the user have the permission to get the UserCourses
		secure(token, members);
		
		//delete userCourse
		boolean userCourse = userService.deleteUserCourse(userId,courseId);
		
		//return the result to the RESTful service
		return Response.status(Response.Status.OK).entity(userCourse).build();
		
	}
	
	
	  ///////////
	 
	  // chapter //
	 
	  ///////////
	
	/**
	 * Get the friends of the logged in user
	 * 
	 * @throws RequestException
	 */
	@POST
	@Path("/addUserChapter")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response addUserChapter(@HeaderParam("Auth-Token") String token,@FormParam("userId")long userId,
			@FormParam("chapterId") long chapterId,@FormParam("teacherId")long teacherId,
			@FormParam("score") int score ,@FormParam("passOrNot") boolean passOrNot,@FormParam("comment")String comment) throws RequestException 
	{
		// Get the user data using the entity manager
		String members[] = {"teacher","admin"};
		
		//judge whether the user have the permission to get the UserCourses
		secure(token, members);
		
		//add userChapter
		boolean userChapter = userService.addUserChapter(userId,chapterId,teacherId,score,passOrNot,comment);

		// Return the user data to the RESTful service
		return Response.status(Response.Status.OK).entity(userChapter).build();
	}
	 
	  /**
	   * get the chapters of a user
	   *
	   * @throws RequestException
	   */
	  @GET
	  @Path("/getUserChapter/{userId: [0-9]*}/{courseId: [0-9]*}")
	  @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	  public Response getUserChapter(@HeaderParam("Auth-Token") String token,@PathParam("userId") long userId,@PathParam("courseId") int courseId,@QueryParam("type") int type) throws RequestException
	  {	
			//set who have the authority to do use this api
			String members[] = {"teacher","admin"};
			
			//judge whether the user have the permission to get the UserCourses
			secure(token, members);
			
			//get userChapter
			String userChapter = userService.getUserChapter(userId,courseId,type);
			
			//Return the user data to the RESTful service
			return Response.status(Response.Status.OK).entity(userChapter).build();
	  }
	  
	  
	  /**
	   * get a chapter for a user
	   *
	   * @throws RequestException
	   */
	  @GET
	  @Path("/getUserChapter/{userId: [0-9]*}/{courseId:[0-9]*}/{chapterId: [0-9]*}")
	  @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	  public Response getOneUserChapter(@HeaderParam("Auth-Token") String token,@PathParam("userId") long userId,@PathParam("chapterId") long chapterId,@QueryParam("type") int type) throws RequestException
	  {	
			//set who have the authority to do use this api
			String members[] = {"teacher","admin"};
			
			//judge whether the user have the permission to get the UserCourses
			secure(token, members);
			
			//get userChapter
			String userChapter = userService.getOneUserChapter(userId,chapterId,type);
			
			//Return the user data to the RESTful service
			return Response.status(Response.Status.OK).entity(userChapter).build();
	  }
	  
		/**
		 * change the status of a user in a chapter
		 * 
		 * @throws RequestException
		 */
		@PUT
		@Path("/changeUserChapter")
		@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
		@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
		public Response changeUserChapter(@HeaderParam("Auth-Token") String token, @FormParam("userId") long userId,
				@FormParam("chapterId") long chapterId, @FormParam("teacherId") long teacherId,
				@FormParam("score") int score ,@FormParam("status") String status,
				@FormParam("date") long date,@FormParam("comment") String comment) throws RequestException 
		{
			
			//set who have the authority to do use this api
			String members[] = {"teacher","admin"};
			
			//judge whether the user have the permission to get the UserCourses
			secure(token, members);
			
			//change userChapter
			boolean userChapter = userService.changeUserChapter(userId, chapterId,teacherId,score,status,date,comment);
			
			//Return the user data to the RESTful service
			return Response.status(Response.Status.OK).entity(userChapter).build();
		}
		
		
		/////////////
		// Friends //
		/////////////
		
		/**
		 * get user's friends
		 * **/
		@GET
		@Path("/getMyFriends")
		@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
		  public Response getMyFriends(@HeaderParam("Auth-Token") String token) throws RequestException
		  {	
	
				//judge whether the user have the permission to get the UserCourses
				secure(token, "*");
				
				//accroding the token to get a user
				User user = userService.findUserByAuthToken(token);
				
				//find friends by user
				String friends = userService.getMyFriends(user);
				
				//Return the user data to the RESTful service
				return Response.status(Response.Status.OK).entity(friends).build();
		  }
		
		/**
		 * get user's friends
		 **/
		@POST
		@Path("/addMyFriends")
		@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
		@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
		  public Response addMyFriends(@HeaderParam("Auth-Token") String token,@FormParam("friendsId")long friendsId) throws RequestException
		  {	
	
				//judge whether the user have the permission to get the UserCourses
				secure(token, "*");
				
				//accroding the token to get a user
				User user = userService.findUserByAuthToken(token);
				
				User friends = userService.get(friendsId);
				
				//find friends by user
				boolean result = userService.addMyFriends(user, friends);
				
				//Return the user data to the RESTful service
				return Response.status(Response.Status.OK).entity(result).build();
		  }
		
		
		/**
		 * delete a friend from my friend list
		 * 
		 * @throws RequestException
		 */
		@DELETE
		@Path("/deleteMyFriends")
		@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
		@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
		public Response deleteMyFriends(@HeaderParam("Auth-Token") String token,
				@PathParam("friendsId") long friendsId) throws RequestException
		{
			//judge whether the user have the permission to get the UserCourses
			secure(token, "*");
			
			//get user
			User user = userService.findUserByAuthToken(token);
			
			//get friend
			User friend = userService.get(friendsId);
			
			//delete userCourse
			boolean result = userService.deleteMyFriends(user,friend);
			
			//return the result to the RESTful service
			return Response.status(Response.Status.OK).entity(result).build();
			
		}
}
