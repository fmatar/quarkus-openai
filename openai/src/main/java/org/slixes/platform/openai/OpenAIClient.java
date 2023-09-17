package org.slixes.platform.openai;

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
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestStreamElementType;
import org.slixes.platform.openai.openai.audio.TranscriptionRequest;
import org.slixes.platform.openai.openai.audio.TranscriptionResponse;
import org.slixes.platform.openai.openai.audio.TranslationRequest;
import org.slixes.platform.openai.openai.chat.ChatCompletionChunk;
import org.slixes.platform.openai.openai.chat.ChatCompletionRequest;
import org.slixes.platform.openai.openai.chat.ChatCompletionResult;
import org.slixes.platform.openai.openai.completion.CompletionChunk;
import org.slixes.platform.openai.openai.completion.CompletionRequest;
import org.slixes.platform.openai.openai.completion.CompletionResult;
import org.slixes.platform.openai.openai.model.Model;
import org.slixes.platform.openai.openai.moderation.ModerationRequest;
import org.slixes.platform.openai.openai.moderation.ModerationResponse;

/**
 * REST client interface for calling OpenAI API endpoints.
 *
 * @author Fady Matar
 */
@Path("/v1")
@Produces
@Consumes
@RegisterRestClient(configKey = "openai-service")
@ClientHeaderParam(name = "Authorization", value = "{token}")
public interface OpenAIClient {

  Logger LOGGER = Logger.getLogger(OpenAIClient.class.getName());


  /**
   * Gets the list of models available from OpenAI.
   *
   * @return A Uni with the model data wrapped in a ListResponseWrapper.
   */
  @GET
  @Path("/models")
  Uni<ListResponseWrapper<Model>> getModels();

  /**
   * Gets details for a specific OpenAI model.
   *
   * @param id The id of the model to get.
   * @return A Uni with the Model data.
   */
  @GET
  @Path("/models/{id}")
  Uni<Model> model(@PathParam("id") String id);

  /**
   * Creates a text completion for the provided request.
   *
   * @param request The CompletionRequest configuring the completion.
   * @return A Uni with the CompletionResult.
   */
  @POST
  @Path("/completions")
  Uni<CompletionResult> createCompletion(CompletionRequest request);

  /**
   * Creates a chat completion based on provided conversation history.
   *
   * @param request The ChatCompletionRequest with history.
   * @return A Uni with the ChatCompletionResult.
   */
  @POST
  @Path("/chat/completions")
  Uni<ChatCompletionResult> createChatCompletion(ChatCompletionRequest request);

  /**
   * Streams a text completion for the provided request.
   *
   * @param request The CompletionRequest to stream.
   * @return A Multi emitting CompletionChunk items.
   */
  @POST
  @Path("/completions")
  @Produces(MediaType.SERVER_SENT_EVENTS)
  @RestStreamElementType(MediaType.APPLICATION_JSON)
  Multi<CompletionChunk> createStreamedCompletion(CompletionRequest request);

  /**
   * Streams a chat completion for the provided request.
   *
   * @param request The ChatCompletionRequest to stream.
   * @return A Multi emitting ChatCompletionChunk items.
   */
  @POST
  @Path("/chat/completions")
  @Produces(MediaType.SERVER_SENT_EVENTS)
  @RestStreamElementType(MediaType.APPLICATION_JSON)
  Multi<ChatCompletionChunk> createStreamedChatCompletion(ChatCompletionRequest request);

  /**
   * Gets the API access token to use for authentication.
   *
   * @return The Bearer token string.
   */
  default String token() {
    String token = ConfigProvider.getConfig().getValue("openai.token", String.class);
    if (token == null || token.isEmpty()) {
      LOGGER.error("API token is missing from configuration.");
      throw new IllegalStateException("API token is missing.");
    }
    return "Bearer " + token;
  }

  /**
   * Wrapper class for list responses from OpenAI.
   *
   * @param <T> The type of the data in the list.
   */
  record ListResponseWrapper<T>(String object, Set<T> data) {

  }

  @POST
  @Path("/audio/transcriptions")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  Uni<TranscriptionResponse> createTranscription(TranscriptionRequest request);


  @POST
  @Path("/audio/transcriptions")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  Uni<String> createTextTranscription(TranscriptionRequest request);


  @POST
  @Path("/audio/translations")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  Uni<TranscriptionResponse> createTranslation(TranslationRequest request);


  @POST
  @Path("/audio/translations")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  Uni<String> createTextTranslation(TranslationRequest request);


  @POST
  @Path("/moderations")
  @Consumes(MediaType.APPLICATION_JSON)
  Uni<ModerationResponse> createModeration(ModerationRequest request);
}