package cn.daringduck.communitybuilder;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.json.JSONObject;

/**
 * If one of the servlets throws a RequestException. This class will nicely handle
 * this exception
 * 
 * @author Jochem Ligtenberg
 */
@Provider
public class RequestExceptionMapper implements ExceptionMapper<RequestException> {
	
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response toResponse(RequestException ex) {
		Error err = ex.getError();
		JSONObject json = new JSONObject();
		json.put("errorCode", err.errorCode);
		json.put("errorMessage", err.errorMessage);
		
		return Response.status(err.httpStatus).entity(json.toString()).build();
	}
}