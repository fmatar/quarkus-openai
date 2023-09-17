package org.slixes.platform.openai.openai.moderation;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Categories(
  @JsonProperty("sexual") boolean sexual,
  @JsonProperty("hate") boolean hate,
  @JsonProperty("harassment") boolean harassment,
  @JsonProperty("self-harm") boolean selfHarm,
  @JsonProperty("sexual/minors") boolean sexualMinors,
  @JsonProperty("hate/threatening") boolean hateThreatening,
  @JsonProperty("violence/graphic") boolean violenceGraphic,
  @JsonProperty("self-harm/intent") boolean selfHarmIntent,
  @JsonProperty("self-harm/instructions") boolean selfHarmInstructions,
  @JsonProperty("harassment/threatening") boolean harassmentThreatening,
  @JsonProperty("violence") boolean violence
) {

}
