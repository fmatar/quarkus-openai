package io.slixes.platform.openai.openai.audio;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.ws.rs.core.MediaType;
import java.io.File;
import lombok.Data;
import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.RestForm;

@Data
public class TranslationRequest {

  @RestForm("file")
  @PartType(MediaType.APPLICATION_OCTET_STREAM)
  final File file;
  @RestForm
  final String model;
  @RestForm
  String prompt;
  @RestForm("response_format")
  @JsonProperty("response_format")
  final String responseFormat;
  @RestForm
  final double temperature;
}


