/*
package com.dinenowinc.dinenow.resources.unused;

import static java.util.Collections.singletonMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.dinenowinc.dinenow.resources.ResourceUtils;
import org.apache.commons.lang.RandomStringUtils;

//import com.sun.jersey.core.header.FormDataContentDisposition;
//import com.sun.jersey.multipart.FormDataBodyPart;
//import com.sun.jersey.multipart.FormDataParam;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Path("/images")
@Api("/images")
public class ImageResource {

	@GET
	@Path("/{fileName}")
	@ApiOperation(value="api get image items", notes="http://localhost:30505/api/images/18t4gwjEm7esR15MLvAMwKqqL.jpg")
	@Produces("image/jpg")
	public Response getImage(@PathParam("fileName") String fileName){
		
		java.nio.file.Path photoPath = Paths.get(System.getProperty("user.dir"), "photos",fileName);	
		return returnFile(photoPath.toFile());
	}
	
	private Response returnFile(File file) {
	    if (!file.exists()) {
	        return ResourceUtils.asFailedResponse(Status.NOT_FOUND, "Not Found");
	    }
	    try {
	        return Response.ok(new FileInputStream(file)).build();
	    } catch (FileNotFoundException e) {
	    	return ResourceUtils.asFailedResponse(Status.NOT_FOUND, "Not Found");
	    }
	}
	
	
	
	
	*/
/*
	@POST
	@Path("/upload")
	@ApiOperation(value="api upload image item",notes="POST file=XXXXXXXXXXXXXXXXX")
	@ApiImplicitParams(@ApiImplicitParam(
            dataType = "file",
            name = "file",
            value="Certificate file",
            paramType = "body"))
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFile(@ApiParam(access = "internal")  @FormDataParam("file") InputStream uploadedInputStream,@ApiParam(access = "internal")  @FormDataParam("file") FormDataContentDisposition fileDetail)  {
		try {
			java.nio.file.Path photoPath = Paths.get(
					System.getProperty("user.dir"), "photos");
			if (!photoPath.toFile().exists()) {
				photoPath.toFile().mkdir();
			}
			String fileName = String.format("%s.%s",
					RandomStringUtils.randomAlphanumeric(25), "jpg");
			java.nio.file.Path filePath = Paths.get(
					System.getProperty("user.dir"), "photos", fileName);

			// save it
			writeToFile(uploadedInputStream, filePath.toString());
			return ResourceUtils.asSuccessResponse(Status.OK,
					singletonMap("link", fileName));
		} catch (Exception e) {
			return ResourceUtils.asFailedResponse(Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
 *//*

	private void writeToFile(InputStream uploadedInputStream, String uploadedFileLocation) {
 
		try {
			OutputStream out = new FileOutputStream(new File(
					uploadedFileLocation));
			int read = 0;
			byte[] bytes = new byte[1024];
 
			out = new FileOutputStream(new File(uploadedFileLocation));
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
*/
