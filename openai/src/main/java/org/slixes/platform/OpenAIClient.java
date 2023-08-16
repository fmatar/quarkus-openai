package org.slixes.platform;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.Set;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestStreamElementType;
import org.slixes.platform.openai.completion.CompletionChunk;
import org.slixes.platform.openai.completion.CompletionRequest;
import org.slixes.platform.openai.completion.CompletionResult;
import org.slixes.platform.openai.completion.chat.ChatCompletionChunk;
import org.slixes.platform.openai.completion.chat.ChatCompletionRequest;
import org.slixes.platform.openai.completion.chat.ChatCompletionResult;
import org.slixes.platform.openai.model.Model;

@Path("/v1")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RegisterRestClient(configKey = "openai-service")
@ClientHeaderParam(name = "Authorization", value = "{token}")
public interface OpenAIClient {

  @GET
  @Path("/models")
  Uni<ListResponseWrapper<Model>> models();


  @GET
  @Path("/models/{id}")
  Uni<Model> model(@PathParam("id") String id);

  @POST
  @Path("/completions")
  Uni<CompletionResult> createCompletion(CompletionRequest request);

  @POST
  @Path("/chat/completions")
  Uni<ChatCompletionResult> createChatCompletion(ChatCompletionRequest request);

  @POST
  @Path("/completions")
  @Produces(MediaType.SERVER_SENT_EVENTS)
  @RestStreamElementType(MediaType.APPLICATION_JSON)
  Multi<CompletionChunk> createStreamedCompletion(CompletionRequest request);

  @POST
  @Path("/chat/completions")
  @Produces(MediaType.SERVER_SENT_EVENTS)
  @RestStreamElementType(MediaType.APPLICATION_JSON)
  Multi<ChatCompletionChunk> createStreamedChatCompletion(ChatCompletionRequest request);


  default String token() {
    return "Bearer " + ConfigProvider.getConfig().getValue("openai.token", String.class);
  }

  record ListResponseWrapper<T>(String object, Set<T> data) {

  }
}