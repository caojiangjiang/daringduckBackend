package cn.daringduck.communitybuilder.controller;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.data.domain.Page;

import cn.daringduck.communitybuilder.RequestException;
import cn.daringduck.communitybuilder.model.Role;
import cn.daringduck.communitybuilder.service.RoleService;

/**
 * This HelloWorld resource acts as a Resource on the REST server It will return
 * a simple JSON object with one key-value pair: msg => 'Hello world!'
 * 
 * @author Jochem Ligtenberg
 */
@Path("/roles")
public class RoleController extends GenericController {

	private final RoleService roleService;
	ServletContext context;
	
	public RoleController(@Context ServletContext context) {
		super(context);
		this.context = context;
		this.roleService = (RoleService) context.getAttribute("roleService");
	}

	/**
	 * Get all the roles from the database
	 * 
	 * @return
	 * @throws RequestException 
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response roles(@HeaderParam("Auth-Token") String token) throws RequestException {
		secure(token, "admin");

		Page<Role> roles = roleService.getPage(0);
		return Response.status(Response.Status.OK).entity(roles).build();
	}
	
}
