package org.slixes.platform.openai.openai.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slixes.platform.openai.openai.common.Role;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonDeserialize(builder = ChatMessage.ChatMessageBuilder.class)
public class ChatMessage {

  private String content;
  private Role role;
  private String name;
  @JsonProperty("function_call")
  private FunctionCall functionCall;

  @JsonPOJOBuilder(withPrefix = "")
  public static class ChatMessageBuilder {
    // Lombok will add builder methods here
  }
}