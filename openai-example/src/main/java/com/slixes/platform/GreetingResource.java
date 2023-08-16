package com.slixes.platform;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.Json;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.slixes.platform.OpenAI;
import org.slixes.platform.openai.completion.CompletionRequest;
import org.slixes.platform.openai.completion.CompletionResult;
import org.slixes.platform.openai.model.Model;

import java.util.Set;

@Path("/")
public class GreetingResource {

  @Inject
  OpenAI client;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("completion")
  public Uni<CompletionResult> completion() {
    var req = new CompletionRequest();
    req.setModel("text-davinci-003");
    req.setPrompt("Give me the name of a song by Iron Maiden that starts with I am a man who walks alone. Do not prefix or include any special characters.");
    req.setTemperature(1.9);
    req.setMaxTokens(100);
    req.setTopP(1D);
    req.setFrequencyPenalty(0D);
    req.setPresencePenalty(0D);
    Log.info(Json.encode(req));
    return client.createCompletion(req);
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("models")
  public Uni<Set<Model>> models() {
    return client.models();
  }
}
