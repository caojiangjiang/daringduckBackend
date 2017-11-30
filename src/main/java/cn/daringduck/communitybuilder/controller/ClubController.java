package cn.daringduck.communitybuilder.controller;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.data.domain.Page;
import org.springframework.orm.jpa.JpaSystemException;

import cn.daringduck.communitybuilder.Error;
import cn.daringduck.communitybuilder.RequestException;
import cn.daringduck.communitybuilder.model.Club;
import cn.daringduck.communitybuilder.service.ClubService;

/**
 * 
 * @author Letitia, Jochem Ligtenberg
 *
 */
@Path("/clubs")
public class ClubController extends GenericController {
	
	private ClubService clubService;

	public ClubController(@Context ServletContext context) {
		super(context);
		this.clubService = (ClubService) context.getAttribute("clubService");
	}
	
	/**
	 * Get all the clubs from the database
	 * 
	 * @return
	 * @throws RequestException 
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response clubs(@HeaderParam("Auth-Token") String token, @QueryParam("page") int page) throws RequestException {
		secure(token, "admin");
		
		Page<Club> clubs = clubService.getPage(page);
		return Response.status(Response.Status.OK).entity(clubs).build();
	}
	
	/**
	 * Create a new club
	 * @throws RequestException 
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response createClub(@HeaderParam("Auth-Token") String token,  @FormParam("code") String code,
			@FormParam("name") String name, @FormParam("location") String location) throws RequestException {

		secure(token, "admin");
		
		Club club = clubService.createClub(code, name, location);
		return Response.status(Response.Status.OK).entity(club).build();
	}

	/**
	 * Get a club
	 * @throws RequestException 
	 */
	@GET
	@Path("/{id: [0-9]*}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response club(@HeaderParam("Auth-Token") String token, @PathParam("id") int id) throws RequestException {

		secure(token, "admin");
		
		Club club = clubService.get(id);
		return Response.status(Response.Status.OK).entity(club).build();
	}
	
	/**
	 * Edit a club
	 * @throws RequestException 
	 */
	@PUT
	@Path("/{id: [0-9]*}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response editClub(@HeaderParam("Auth-Token") String token, @PathParam("id") int id, @QueryParam("code") String code, 
			@QueryParam("name") String name, @QueryParam("location") String location) throws RequestException {

		secure(token, "admin");
		
		Club club = clubService.updateClub(id, code, name, location);
		return Response.status(Response.Status.OK).entity(club).build();
	}
	
	/**
	 * Delete a club
	 * @throws RequestException 
	 */
	@DELETE
	@Path("/{id: [0-9]*}")
	public Response deleteClub(@HeaderParam("Auth-Token") String token, @PathParam("id") int id) throws RequestException {

		secure(token, "admin");
		
		try {
			clubService.deleteClub(id);
		} catch (JpaSystemException e) {
			throw new RequestException(Error.CLUB_USED);
		}
		
		return Response.status(Response.Status.OK).build();
	}
}
