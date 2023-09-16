package org.slixes.platform.openai.openai.audio;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record TranscriptionResponse(
  String task,
  String language,
  Double duration,
  String text,
  List<Segment> segments
) {

}

