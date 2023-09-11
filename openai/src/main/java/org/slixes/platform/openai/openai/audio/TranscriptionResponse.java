package org.slixes.platform.openai.openai.audio;


import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class TranscriptionResponse {

  String text;
}
