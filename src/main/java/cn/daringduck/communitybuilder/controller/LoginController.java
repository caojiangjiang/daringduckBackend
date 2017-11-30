package cn.daringduck.communitybuilder.controller;

import javax.servlet.ServletContext;
import javax.ws.rs.QueryParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import cn.daringduck.communitybuilder.model.AuthToken;
import cn.daringduck.communitybuilder.model.User;
import cn.daringduck.communitybuilder.service.UserService;

/**
 * Login call for the service
 * 
 * @author Jochem Ligtenberg
 */
@Path("/login")
public class LoginController {

	UserService userService;

	public LoginController(@Context ServletContext context) {
		this.userService = (UserService) context.getAttribute("userService");
	}

	/**
	 * Send back a token to the user who tries to log in, if the user password
	 * combination is correct. otherwise return 400
	 * 
	 * @return List of moments
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response login(@QueryParam("username") String username, @QueryParam("password") String password) {
		User user = userService.authenticate(username, password);

		if (user == null) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}

		// Generate a token
		AuthToken token = userService.generateAuthToken(user);

		// Return the token to the RESTful service
		return Response.status(Response.Status.OK).entity(token.getToken()).build();
	}

}
