package cn.daringduck.communitybuilder.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
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
import javax.ws.rs.core.Response.ResponseBuilder;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.core.io.UrlResource;

import com.sun.research.ws.wadl.Resource;

import cn.daringduck.communitybuilder.RequestException;
import cn.daringduck.communitybuilder.model.Picture;
import cn.daringduck.communitybuilder.service.PictureService;
import javassist.expr.NewArray;

/**
 * Implements the RESTful calls for pictures
 * 
 * @author Jochem Ligtenberg
 */
@Path("/pictures")
public class PictureController extends GenericController{

	private final PictureService pictureService;
	private final ServletContext context;
	
	public PictureController(@Context ServletContext context) {
		super(context);
		this.context = context;
		this.pictureService = (PictureService) context.getAttribute("pictureService");
	}

	/**
	 * Upload an image jpg or png to the server
	 * @throws RequestException 
	 */
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response uploadFile(@HeaderParam("Auth-Token") String token,
			@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataBodyPart body) throws RequestException {

		secure(token, "*");
		
		// Get the user data using the entity manager
		String subtype = body.getMediaType().getSubtype();

		switch(subtype) {
		case "jpeg":
		case "jpg":
		case "png":
			break;
		default:
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		
		String imageName = UUID.randomUUID().toString() + "." + subtype;
		String uploadedFileLocation = context.getRealPath("/images/"+imageName);
		
		
		Picture picture = pictureService.createPictureReference(uploadedFileLocation);
		
		// Store the file
		try {
			writeToFile(uploadedInputStream, uploadedFileLocation);
			
			// If the file is stored
			return Response.status(Response.Status.OK).entity(picture).build();
		} catch (IOException e){
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}

	}

	/**
	 * Write the incoming file to a file on the system
	 * @throws IOException 
	 */
	private void writeToFile(InputStream uploadedInputStream, String uploadedFileLocation) throws IOException {
		OutputStream out = new FileOutputStream(new File(uploadedFileLocation),true);
		int read = 0;
		byte[] bytes = new byte[1024];

		out = new FileOutputStream(new File(uploadedFileLocation));
		while ((read = uploadedInputStream.read(bytes)) != -1) {
			out.write(bytes, 0, read);
		}
		out.flush();
		out.close();

	}
	
	
	
	/**
	 * download an image jpg or png to the server
	 * @throws UnsupportedEncodingException 
	 * @throws RequestException 
	 */
	@GET
	@Path("/{id:[0-9]*}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
    public Response downImage( @PathParam("id") long id) throws UnsupportedEncodingException {
		
		Picture picture = pictureService.get(id);
		String path = picture.getFileLocation();

		System.out.println(path);
		String imageName = path.substring(path.lastIndexOf('\\')+1);
		
		System.out.println(imageName);
		
		File file =new File(path);
		long fileLength = file.length();
		
	     ResponseBuilder responseBuilder = Response.ok(file);
         responseBuilder.type("application/x-msdownload");
         responseBuilder.header("Content-Disposition", "attachment; filename=" 
                 + URLEncoder.encode(imageName, "UTF-8"));
         responseBuilder.header("Content-Length", Long.toString(fileLength));
         Response response = responseBuilder.build();
         return response;
		
    }
}
