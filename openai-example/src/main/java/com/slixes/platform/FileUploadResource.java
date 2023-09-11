package com.slixes.platform;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.nio.file.Files;

@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
@Path("/feed")
public class FileUploadResource {

  @POST
  @Path("/upload")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response uploadFile(FileUploadForm form) throws IOException {
    // Handle the file part
    var path = form.file.uploadedFile();
    var bytes = Files.readAllBytes(path);

    var fileName = form.file.fileName();

    // Handle the metadata part
    var metadata = form.metadata;

    // Your logic to process the file and metadata goes here

    return Response.ok(Json.encode("File uploaded successfully with metadata")).build();
  }
}