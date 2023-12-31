package org.slixes.platform.openai.completion;

import java.util.List;

import io.vertx.core.json.Json;
import lombok.Data;

/**
 * Object containing a response chunk from the completions streaming api.
 *
 * <p>https://beta.openai.com/docs/api-reference/completions/create
 */
@Data
public class CompletionChunk {
  /** A unique id assigned to this completion. */
  String id;

  /**
   * https://beta.openai.com/docs/api-reference/create-completion The type of object returned,
   * should be "text_completion"
   */
  String object;

  /** The creation time in epoch seconds. */
  long created;

  /** The model used. */
  String model;

  /** A list of generated completions. */
  List<CompletionChoice> choices;

  public String toString() {
    return Json.encode(this);
  }
}
