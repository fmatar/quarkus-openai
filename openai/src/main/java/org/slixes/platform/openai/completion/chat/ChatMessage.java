package org.slixes.platform.openai.completion.chat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slixes.platform.openai.common.Role;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonDeserialize(builder = ChatMessage.ChatMessageBuilder.class)
public class ChatMessage {

  private String content;
  private Role role;
  private String name;
  private FunctionCall functionCall;

  @JsonPOJOBuilder(withPrefix = "")
  public static class ChatMessageBuilder {
    // Lombok will add builder methods here
  }
}