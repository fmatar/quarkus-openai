package org.slixes.platform.openai;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
@JsonDeserialize(builder = Usage.UsageBuilder.class)
public class Usage {

  @JsonProperty("prompt_tokens")
  long promptTokens;
  @JsonProperty("completion_tokens")
  long completionTokens;
  @JsonProperty("total_tokens")
  long totalTokens;


  @JsonPOJOBuilder(withPrefix = "")
  public static class UsageBuilder {
    // Lombok will add builder methods here
  }
}
