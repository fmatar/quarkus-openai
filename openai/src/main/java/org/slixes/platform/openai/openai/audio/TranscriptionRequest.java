package org.slixes.platform.openai.openai.audio;


import java.io.File;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.jboss.resteasy.reactive.PartFilename;
import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.RestForm;

@Data
@Builder
@Jacksonized
public class TranscriptionRequest {


  private final File file;
  private final String model;
  private String prompt;
  @RestForm("response_format")
  private final AudioResponseFormat responseFormat;
  private final double temperature;
  private final String language;
  private String transcription;
}
