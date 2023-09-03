
package org.slixes.platform;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Set;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slixes.platform.openai.completion.CompletionChunk;
import org.slixes.platform.openai.completion.CompletionRequest;
import org.slixes.platform.openai.completion.CompletionResult;
import org.slixes.platform.openai.completion.chat.ChatCompletionChunk;
import org.slixes.platform.openai.completion.chat.ChatCompletionRequest;
import org.slixes.platform.openai.completion.chat.ChatCompletionResult;
import org.slixes.platform.openai.model.Model;

/**
 * Client for accessing the OpenAI API. Provides reactive methods for integrating with OpenAI capabilities like text
 * completion, image generation, embeddings etc.
 *
 * @author Fady Matar
 */
@ApplicationScoped
public class OpenAI {

  /**
   * Injected OpenAI REST client for calling API endpoints.
   */
  @RestClient
  OpenAIClient client;

  /**
   * Gets a list of models available from OpenAI.
   *
   * @return A Uni with a set of Model instances.
   */
  public Uni<Set<Model>> models() {
    return client.models().onItem().transform(OpenAIClient.ListResponseWrapper::data);
  }

  /**
   * Gets details of a specific model.
   *
   * @param id The id of the model to get details for.
   * @return A Uni with the Model instance.
   */
  public Uni<Model> model(String id) {
    return client.model(id);
  }

  /**
   * Generates a text completion for the provided prompt and parameters.
   *
   * @param request The CompletionRequest configuring the completion.
   * @return A Uni with the CompletionResult.
   */
  public Uni<CompletionResult> createCompletion(CompletionRequest request) {
    return client.createCompletion(request);
  }

  /**
   * Creates a streamed text completion for the provided request.
   *
   * @param request The CompletionRequest to stream.
   * @return A Multi emitting CompletionChunk items.
   */
  public Multi<CompletionChunk> createStreamedCompletion(CompletionRequest request) {
    return client.createStreamedCompletion(request)
      .filter(chunk -> !chunk.getId().equals("[DONE]"));
  }

  /**
   * Creates a completion for a conversation based on chat history.
   *
   * @param request The ChatCompletionRequest with history.
   * @return A Uni with the ChatCompletionResult.
   */
  public Uni<ChatCompletionResult> createChatCompletion(ChatCompletionRequest request) {
    return client.createChatCompletion(request);
  }

  /**
   * Streams a chat completion for the provided request.
   *
   * @param request The ChatCompletionRequest to stream.
   * @return A Multi emitting ChatCompletionChunk items.
   */
  public Multi<ChatCompletionChunk> createStreamedChatCompletion(ChatCompletionRequest request) {
    return client.createStreamedChatCompletion(request)
      .filter(chunk -> !chunk.getId().equals("[DONE]"));
  }

}