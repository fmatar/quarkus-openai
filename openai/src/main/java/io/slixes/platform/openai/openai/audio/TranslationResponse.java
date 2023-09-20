package io.slixes.platform.openai.openai.audio;

import java.util.List;

public record TranslationResponse(
  String task,
  String language,
  Double duration,
  String text,
  List<Segment> segments
) {

}

