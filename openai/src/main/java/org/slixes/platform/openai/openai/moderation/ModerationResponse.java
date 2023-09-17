package org.slixes.platform.openai.openai.moderation;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record ModerationResponse(
  @JsonProperty("id") String id,
  @JsonProperty("model") String model,
  @JsonProperty("results") List<ModerationResult> results
) {

}