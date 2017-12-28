package cn.daringduck.communitybuilder.controller;
import java.util.List;
import javax.servlet.ServletContext;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.data.domain.Page;

import cn.daringduck.communitybuilder.RequestException;
import cn.daringduck.communitybuilder.model.Moment;
import cn.daringduck.communitybuilder.model.MomentPart;
import cn.daringduck.communitybuilder.model.User;
import cn.daringduck.communitybuilder.service.MomentService;
import cn.daringduck.communitybuilder.service.UserService;

/**
 * This class contains all the RESTful calls that have anything
 * to do with moments
 * 
 * @author Jochem Ligtenberg
 * 
 */
@Path("/moments")
public class MomentController extends GenericController {

	private final MomentService momentService;
	private final UserService userService;

	
	public MomentController(@Context ServletContext context) {
		super(context);
		this.momentService = (MomentService) context.getAttribute("momentService");
		this.userService = (UserService) context.getAttribute("userService");
	}
	
	/**
	 * Send back a list with the most recent moments to the Restful service
	 * @return List of moments
	 * @throws RequestException 
	 */
	@GET
	@Path("/pageMoments")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response moments(@QueryParam("page") int page) throws RequestException {
	
		Page<Moment> moments = momentService.getPage(page);
		
		List<Moment> momentList = moments.getContent();
		
		return Response.status(200).entity(momentList).build();
		
	}
	
	/**
	 * Get information about a moment with id
	 */
	@GET
	@Path("/{id: [0-9]*}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getMoment(@PathParam("id") long id) {

		Moment moment = momentService.get(id);
		return Response.status(Response.Status.OK).entity(moment).build();
		
	}
	
	////////////////////////////////////////////////////////////////////
	// Me
	////////////////////////////////////////////////////////////////////	
	
	/**
	 * Get the user's own moments
	 * @throws RequestException 
	 */
	@GET
	@Path("/getMyMoments")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getMyMoments(@HeaderParam("Auth-Token") String token,@QueryParam("page") int page) throws RequestException {
		secure(token, "*");
		User user = userService.findUserByAuthToken(token);
		List<Moment> moments = momentService.getMyMoment(user,page);
		return Response.status(Response.Status.OK).entity(moments).build();
		
	}
	
//	/**
//	 * Get the user's friends' moments
//	 * @throws RequestException 
//	 */
//	@GET
//	@Path("/getMyFriendsMoments")
//	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
//	public Response getMyFriendsMoments(@HeaderParam("Auth-Token") String token,@QueryParam("page") int page) throws RequestException {
//		secure(token, "*");
//		User user = userService.findUserByAuthToken(token);
//		List<Moment> moments = userService.getMyFriendsMoments(user);
//		return Response.status(Response.Status.OK).entity(moments).build();
//		
//	}
//	
	/**
	 * Get a specific friend's moments
	 * @throws RequestException 
	 */
	@GET
	@Path("/getSpecificFriendMoments")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getSpecificFriendMoments(@HeaderParam("Auth-Token") String token,@QueryParam("friendId") long friendId,@QueryParam("page") int page) throws RequestException {
		secure(token, "*");
		User user = userService.get(friendId);
		List<Moment> moments = momentService.getMyMoment(user,page);
		return Response.status(Response.Status.OK).entity(moments).build();
		
	}
	
	/**
	 *user add a moment for himself
	 */
	@POST
	@Path("/me/addMoment")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response addMoment(@HeaderParam("Auth-Token") String token,
			@FormParam("title") String title, @FormParam("privacy") String privacy,
			@FormParam("eventDate") String eventDate) throws RequestException {
		
		secure(token, "*");
		
		User user = userService.findUserByAuthToken(token);
		
		Moment moment = userService.addUserMoment(user.getId(), title, privacy,eventDate);
		
		return Response.status(Response.Status.OK).entity(moment).build();
	}
	
	
	/**
	 * get DD01 newest moment
	 * @throws RequestException 
	 */
	@GET
	@Path("/getDD01NewestMoment")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getDD01NewestMoment(@HeaderParam("Auth-Token") String token) throws RequestException {
		Moment moment = momentService.getDD01NewestMoment();
		return Response.status(Response.Status.OK).entity(moment).build();
	}
	
	
	

	////////////////////////////////////////////////////////////////////
	// Moment Part
	////////////////////////////////////////////////////////////////////	
	
	
	/**
	 * get a momentPart about a moment with id
	 * @throws RequestException 
	 */
	@GET
	@Path("/{momentId: [0-9]*}/get")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getMomentPart(@PathParam("momentId") long momentId) throws RequestException {
		return Response.status(Response.Status.OK).entity(momentService.getMomentPartWithId(momentId)).build();
	}
	
	
	/**
	 * add paragraph about a moment with id
	 * @throws RequestException 
	 */
	@POST
	@Path("/{momentId: [0-9]*}/add")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response addMomentPart(@HeaderParam("Auth-Token") String token,@PathParam("momentId") long momentId,@FormParam("pictureId") long pictureId,@FormParam("part") int part,@FormParam("text") String text) throws RequestException {
		secure(token, "*");
		MomentPart momentPart = momentService.addMomentPart(part, text, momentId,pictureId);
		return Response.status(Response.Status.OK).entity(momentPart).build();
	}
	
	
	/**
	 * Get information about a moment with id
	 * @throws RequestException 
	 */
	@POST
	@Path("/{momentPartId: [0-9]*}/change")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response changeMomentPart(@HeaderParam("Auth-Token") String token, @PathParam("momentPartId") long momentPartId,@FormParam("text") String text,@FormParam("pictureId") long pictureId,@FormParam("part")int part) throws RequestException {
		secure(token, "*");
		MomentPart momentPart = momentService.updateMomentPartWithId(momentPartId, text,pictureId);
		
		if(momentPart==null)
			return Response.status(Response.Status.BAD_REQUEST).entity("fail").build();
			
		return Response.status(Response.Status.OK).entity(momentPart).build();
	}
	
	/**
	 * delete momentsPart
	 */
	@DELETE
	@Path("/{id: [0-9]*}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response deleteMomentPart(@HeaderParam("Auth-Token") String token, @PathParam("id") long id) {
		//Moment moment = momentService.deleteMomentPart(id);
		return Response.status(Response.Status.OK).build();
	}
	
	public void  name() {
		
	}
	public void  friend() {
		
	}

}
