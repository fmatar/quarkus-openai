package org.slixes.platform;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.wildfly.common.Assert.assertTrue;

import io.quarkus.logging.Log;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Multi;
import io.vertx.core.json.Json;
import jakarta.inject.Inject;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.slixes.platform.openai.ChatMessage;
import org.slixes.platform.openai.Role;
import org.slixes.platform.openai.completion.CompletionChunk;
import org.slixes.platform.openai.completion.CompletionRequest;
import org.slixes.platform.openai.completion.chat.ChatCompletionRequest;
import org.slixes.platform.openai.completion.chat.ChatCompletionResult;
import org.slixes.platform.openai.completion.chat.Function;
import org.slixes.platform.openai.completion.chat.Parameters;

@QuarkusTest
class OpenAIClientTest {

  @Inject
  OpenAI client;

  @Test
  void testModels() {
    var models = client.models().await().indefinitely();
    assertThat(models, is(not(empty())));
    var curieModel = client.model("curie").await().indefinitely();
    assertThat(curieModel.getId(), is(equalTo("curie")));
  }

  @Test
  void testCompletion() {
    var req = new CompletionRequest();
    req.setModel("text-davinci-003");
    req.setPrompt(
      "Give me the name of a song by Iron Maiden that starts with I am a man who walks alone");
    req.setTemperature(1.9);
    req.setMaxTokens(100);
    req.setTopP(1D);
    req.setFrequencyPenalty(0D);
    req.setPresencePenalty(0D);
    Log.info(Json.encode(req));

    var result = client.createCompletion(req).await().indefinitely();
    assertThat(result.getChoices().size(), is(greaterThan(0)));
    req.setStream(true);

    var completionStage = client.createStreamedCompletion(req);

    var indefinitely = completionStage.collect().asList().await().indefinitely();

    assertThat(indefinitely.size(), is(greaterThan(0)));
  }

  @Test
  void testChatCompletion() {
    var req = new ChatCompletionRequest();
    req.setModel("gpt-4-0613");
    req.setMaxTokens(300);
    req.setTemperature(0.9D);
    req.setN(4);
    req.setMessages(List.of(
      new ChatMessage("I am the best musician in the world.", Role.SYSTEM, "toota", null),
      new ChatMessage("What are the most complex jazz chord variations?", Role.USER, "toota", null),
      new ChatMessage("Hmm let's ask the master", Role.ASSISTANT, "toota", null)
    ));
    client.createChatCompletion(req).onItem().invoke(result -> {
      Log.info(Json.encode(result));
    }).await().indefinitely();
    /* Let's test the same execution using the streaming approach */
    req.setStream(true);
    var chunks = client.createStreamedChatCompletion(req).collect().asList().await().indefinitely();
    // Check if all elements have the same ID
    var expectedId = chunks.get(0).getId();
    assertTrue(chunks.stream().allMatch(element -> element.getId().equals(expectedId)));
  }


  @Test
  void testChatCompletionWithFunction() {
    var location = new HashMap<String, Object>();
    location.put("type", "string");
    location.put("description", "The city and state, e.g. San Francisco, CA");

    var unitProp = new HashMap<String, Object>();
    unitProp.put("type", "string");
    unitProp.put("enum", Arrays.asList("celsius", "fahrenheit"));

    var properties = new HashMap<String, Object>();
    properties.put("location", location);
    properties.put("unit", unitProp);

    var req = ChatCompletionRequest.builder()
      .setModel("gpt-4-0613")
      .setMaxTokens(300)
      .setTemperature(0.9D)
      .setN(4)
      .setMessages(List.of(
        new ChatMessage("What's the weather like in Seattle Washington?", Role.USER, "weather",
          null)
      ))
      .setFunctions(List.of(
        Function.builder()
          .setName("weather")
          .setDescription("Get the weather for a location")
          .setParameters(
            Parameters.builder()
              .setType("object")
              .setProperties(properties)
              .setRequired(Collections.singletonList("location"))
              .build()
          ).build()
      )).build();

    ChatCompletionResult result = client.createChatCompletion(req).await().indefinitely();
    assertThat(result.getChoices().size(), is(greaterThan(0)));
  }
}
