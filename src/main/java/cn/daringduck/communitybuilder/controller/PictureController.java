package cn.daringduck.communitybuilder.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;

import cn.daringduck.communitybuilder.RequestException;
import cn.daringduck.communitybuilder.model.Picture;
import cn.daringduck.communitybuilder.service.PictureService;

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
			@FormDataParam("file") FormDataBodyPart body,
			
			  //type can be course��user
			  @FormDataParam("type") String type,
			  
			  //id can be user_id��course_id
			  //if you just want to upload a picture for avatar, you don't need to fill in this param
		      @FormDataParam("id") String id, 
		      
		      //for user the subDirectory is moment_id, for course the subDirectory is chapter_id
		      //so when you want to use this api, you need give me subDirectory like moment_1��chapter_1
		      //if do not have subDirectory, the api will consider the picture is user picture
		      @FormDataParam("subDirectory") String subDirectory
		      ) throws RequestException { 

		secure(token, "*");
		
		// Get the user data using the entity manager
		String subtype = body.getMediaType().getSubtype();
		
		// limit the type of the picture
		switch(subtype) {
		case "jpeg":
		case "jpg":
		case "png":
		case "pdf":
		case "gif":
			break;
		default:
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		
		//inital the imageName and pathName
		String imageName = "";
		String pathName ="/root/img";
		
		//if the directory don't exist we need established the directory
		pathName = pathName+"/"+type;
		File file = new File(pathName);
		if(!file.isDirectory()) {
			file.mkdir();
		}
		
		//if the directory don't exist we need established the directory
		pathName = pathName +"/"+id;
		File file1 = new File(pathName);
		if(!file1.isDirectory()) {
			file1.mkdir();
		}
		
		//when the subDirectory is null it means the picture is for user's avatar or for course's picture
		if(subDirectory.equalsIgnoreCase("")) {
			//image name
			imageName = "1."+subtype;
		}
		//when the subDirectory is not null it means the picture is for user's moment or for course's chapter
		else {
			//image name
			imageName = UUID.randomUUID().toString() + "." + subtype;
	
			//path name
			pathName = pathName + "/" + subDirectory;
			File file3 = new File(pathName);
			if(!file3.isDirectory()) {
				file3.mkdir();
			}
		}
		
		
//		String imageName = "";
//		String pathName ="D:\\img";
//		
//		//if the directory don't exist we need established the directory
//		pathName = pathName+"\\"+type;
//		File file = new File(pathName);
//		if(!file.isDirectory()) {
//			file.mkdir();
//		}
//		
//		//if the directory don't exist we need established the directory
//		pathName = pathName +"\\"+id;
//		File file1 = new File(pathName);
//		if(!file1.isDirectory()) {
//			file1.mkdir();
//		}
//		
//		//when the subDirectory is null it means the picture is for user's avatar or for course's picture
//		if(subDirectory.equalsIgnoreCase("")) {
//			//image name
//			imageName = "1."+subtype;
//		}
//		//when the subDirectory is not null it means the picture is for user's moment or for course's chapter
//		else {
//			//image name
//			imageName = UUID.randomUUID().toString() + "." + subtype;
//	
//			//path name
//			pathName = pathName + "\\" + subDirectory;
//			File file3 = new File(pathName);
//			if(!file3.isDirectory()) {
//				file3.mkdir();
//			}
//		}
			
//		String uploadedFileLocation = pathName+"\\"+imageName;
		String uploadedFileLocation = pathName+"/"+imageName;
		
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
