package com.slixes.platform;


import io.vertx.core.json.JsonObject;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

public class FileUploadForm {

  @RestForm("file")
  public FileUpload file;

  @RestForm("metadata")
  public JsonObject metadata;
}

