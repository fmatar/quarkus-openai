package com.slixes.platform;

import io.vertx.core.json.JsonObject;
import jakarta.ws.rs.core.MediaType;
import java.io.File;
import lombok.Data;
import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.RestForm;

@Data
public class FileUploadForm {

  @RestForm("file")
  @PartType(MediaType.APPLICATION_OCTET_STREAM)
  File file;

  @RestForm("metadata")
  @PartType(MediaType.APPLICATION_JSON)
  JsonObject metadata;

  @RestForm
  double temperature;
}

