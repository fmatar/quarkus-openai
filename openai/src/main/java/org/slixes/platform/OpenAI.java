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

@ApplicationScoped
public class OpenAI {

  @RestClient
  OpenAIClient client;

  public Uni<Set<Model>> models() {
    return client.models().onItem().transform(OpenAIClient.ListResponseWrapper::data);
  }

  public Uni<Model> model(String id) {
    return client.model(id);
  }


  public Uni<CompletionResult> createCompletion(CompletionRequest request) {
    return client.createCompletion(request);
  }

  public Multi<CompletionChunk> createStreamedCompletion(CompletionRequest request) {
    return client.createStreamedCompletion(request)
      .filter(chunk -> !chunk.getId().equals("[DONE]"));
  }


  public Uni<ChatCompletionResult> createChatCompletion(ChatCompletionRequest request) {
    return client.createChatCompletion(request);
  }

  public Multi<ChatCompletionChunk> createStreamedChatCompletion(ChatCompletionRequest request) {
    return client.createStreamedChatCompletion(request)
      .filter(chunk -> !chunk.getId().equals("[DONE]"));
  }
};