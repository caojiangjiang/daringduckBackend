package cn.daringduck.communitybuilder.controller;

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

import org.hibernate.procedure.internal.Util.ResultClassesResolutionContext;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.research.ws.wadl.Method;

import cn.daringduck.communitybuilder.RequestException;
import cn.daringduck.communitybuilder.model.Moment;
import cn.daringduck.communitybuilder.model.MomentPart;
import cn.daringduck.communitybuilder.model.Picture;
import cn.daringduck.communitybuilder.service.MomentService;

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

	
	public MomentController(@Context ServletContext context) {
		super(context);
		this.momentService = (MomentService) context.getAttribute("momentService");
	}
	
	/**
	 * Send back a list with the most recent moments to the Restful service
	 * @return List of moments
	 */
	@GET
	@Path("/pageMoments")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response moments(@QueryParam("page") int page) {
	
		Page<Moment> moments = momentService.getPage(page);
		return Response.status(200).entity(moments).build();
		
	}
	
	/**
	 * Get information about a moment with id
	 */
	@GET
	@Path("/{id: [0-9]*}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response getMoment(@HeaderParam("Auth-Token") String token, @PathParam("id") long id) {

		Moment moment = momentService.get(id);
		return Response.status(Response.Status.OK).entity(moment).build();
		
	}
	
	
	/**
	 * get DD01 newest moment
	 */
//	@GET
//	@Path("/getDD01NewestMoment")
//	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
//	public Response getDD01NewestMoment() {
//		Moment moment = momentService.getDD01NewestMoment();
//		return Response.status(Response.Status.OK).entity(moment).build();
//	}
//	
	/**
	 * add paragraph about a moment with id
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
	

}
