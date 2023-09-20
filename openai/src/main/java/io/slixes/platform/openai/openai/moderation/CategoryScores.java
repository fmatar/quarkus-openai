package io.slixes.platform.openai.openai.moderation;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CategoryScores(
  @JsonProperty("sexual") double sexual,
  @JsonProperty("hate") double hate,
  @JsonProperty("harassment") double harassment,
  @JsonProperty("self-harm") double selfHarm,
  @JsonProperty("sexual/minors") double sexualMinors,
  @JsonProperty("hate/threatening") double hateThreatening,
  @JsonProperty("violence/graphic") double violenceGraphic,
  @JsonProperty("self-harm/intent") double selfHarmIntent,
  @JsonProperty("self-harm/instructions") double selfHarmInstructions,
  @JsonProperty("harassment/threatening") double harassmentThreatening,
  @JsonProperty("violence") double violence
) {

}
