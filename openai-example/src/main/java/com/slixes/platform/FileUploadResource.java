package com.slixes.platform;

import io.smallrye.common.annotation.Blocking;
import io.vertx.core.json.Json;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
@Path("/feed")
public class FileUploadResource {

  @POST
  @Path("/upload")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Blocking
  public Response uploadFile(FileUploadForm form) {
    // Handle the file part
    var file = form.getFile();

    // Handle the metadata part
    var metadata = form.getMetadata();

    var temp = form.getTemperature();

    // Your logic to process the file and metadata goes here
    return Response.ok(Json.encode("File uploaded successfully with metadata")).build();
  }
}