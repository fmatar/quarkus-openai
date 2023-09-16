package org.slixes.platform.openai.openai.audio;

import java.util.List;

public record Segment(
  int id,
  int seek,
  double start,
  double end,
  String text,
  List<Integer> tokens,
  double temperature,
  double avg_logprob,
  double compression_ratio,
  double no_speech_prob
) {

}