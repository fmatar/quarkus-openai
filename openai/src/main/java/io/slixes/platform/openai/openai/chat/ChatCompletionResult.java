package io.slixes.platform.openai.openai.chat;


import io.slixes.platform.openai.openai.common.Usage;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

/**
 * Object containing a response from the chat completions api.
 */
@Data
@Builder
@Jacksonized
public class ChatCompletionResult {

  /**
   * Unique id assigned to this chat completion.
   */
  String id;

  /**
   * The type of object returned, should be "chat.completion"
   */
  String object;

  /**
   * The creation time in epoch seconds.
   */
  long created;

  /**
   * The GPT model used.
   */
  String model;

  /**
   * A list of all generated completions.
   */
  List<ChatCompletionChoice> choices;

  /**
   * The API usage for this request.
   */
  Usage usage;
}
