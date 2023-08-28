package org.slixes.platform;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import io.quarkus.logging.Log;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import jakarta.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import org.slixes.platform.openai.completion.chat.ChatMessage;
import org.slixes.platform.openai.common.Role;
import org.slixes.platform.openai.completion.chat.ChatCompletionRequest;
import org.slixes.platform.openai.completion.chat.ChatCompletionResult;
import org.slixes.platform.openai.completion.chat.Function;
import org.slixes.platform.openai.completion.chat.Parameters;

@QuarkusTest
class ChatCompletionTests {

  @Inject
  OpenAI client;

  @Test
  void testChatCompletion() {
    var req = new ChatCompletionRequest();
    req.setModel("gpt-3.5-turbo");
    req.setMaxTokens(500);
    req.setTemperature(1.1);
    req.setMessages(List.of(
      ChatMessage.builder().content("You are the best musician in the world.").role(Role.SYSTEM).build(),
      ChatMessage.builder().content("What are the most complex jazz chord variations?").role(Role.SYSTEM).build()
    ));
    client.createChatCompletion(req).onItem().invoke(result -> {
      Log.info(Json.encode(result));
      assertThat(result.getChoices().size(), equalTo(1));
      var chatCompletionChoice = result.getChoices().get(0);
      assertThat(chatCompletionChoice.getMessage().getContent(), notNullValue());
      assertThat(chatCompletionChoice.getMessage().getRole(), equalTo(Role.ASSISTANT));
    }).await().indefinitely();
  }

  @Test
  void testChatCompletionWithStreaming() {
    var req = new ChatCompletionRequest();
    req.setModel("gpt-3.5-turbo");
    req.setMaxTokens(500);
    req.setTemperature(1.1);
    req.setMessages(List.of(
      ChatMessage.builder()
        .content(
          "You are the best musician in the world. give me a list of coma separated values of the best 3 flamenco players in the world")
        .role(Role.SYSTEM).build()
    ));
    req.setStream(true);

    var chunks = client.createStreamedChatCompletion(req).collect().asList().await().indefinitely();
    AtomicInteger counter = new AtomicInteger(0);
    chunks.forEach(c -> {
      var message = c.getChoices().get(0).getMessage();
      assertThat(c.getModel(), equalTo("gpt-3.5-turbo-0613"));
      assertThat(c.getObject(), equalTo("chat.completion.chunk"));
      assertThat(c.getCreated(), is(greaterThan(0L)));

      if (counter.get() == 0) {
        assertThat(message.getRole(), equalTo(Role.ASSISTANT));
      } else if (counter.get() == chunks.size() - 1) {
        assertThat(message.getContent(), nullValue());
        assertThat(message.getName(), nullValue());
        assertThat(message.getFunctionCall(), nullValue());
      } else {
        assertThat(message.getRole(), nullValue());
        assertThat(message.getContent(), notNullValue());
      }
      counter.getAndIncrement();
    });
  }


  @Test
  void testChatCompletionWithFunction() {
    var properties = getStringObjectHashMap();

    var req = ChatCompletionRequest.builder()
      .model("gpt-3.5-turbo")
      .maxTokens(300)
      .temperature(1.1)
      .messages(List.of(
        ChatMessage.builder().content(
            "Tell me more about Paco de Lucia Where was he born?, give me his real first name and last name, and his date of birth")
          .role(Role.USER).build()
      ))
      .functions(List.of(
        Function.builder()
          .setName("get_artist_info")
          .setDescription("Get the artist information")
          .setParameters(
            Parameters.builder()
              .setType("object")
              .setProperties(properties)
              .setRequired(List.of("first_name", "last_name", "date_of_birth", "place_of_birth"))
              .build()
          ).build()
      )).build();

    Log.info(Json.encode(req));
    ChatCompletionResult result = client.createChatCompletion(req).await().indefinitely();
    Log.info(Json.encode(result));
    assertThat(result.getChoices().size(), is(greaterThan(0)));
    assertThat(result.getChoices().get(0).getFinishReason(), equalTo("function_call"));
    assertThat(result.getChoices().get(0).getMessage().getFunctionCall().getArguments(), notNullValue());

    var jsonObj = new JsonObject(result.getChoices().get(0).getMessage().getFunctionCall().getArguments());
    assertThat(jsonObj.getString("place_of_birth"), equalTo("Algeciras, Spain"));

  }

  private static HashMap<String, Object> getStringObjectHashMap() {
    var firstName = new HashMap<String, Object>();
    firstName.put("type", "string");
    firstName.put("description", "The first name of the artist");

    var lastName = new HashMap<String, Object>();
    lastName.put("type", "string");
    lastName.put("description", "The first name of the artist");

    var dateOfBirth = new HashMap<String, Object>();
    dateOfBirth.put("type", "string");
    dateOfBirth.put("description", "The date of birth of the artist");

    var placeOfBirth = new HashMap<String, Object>();
    placeOfBirth.put("type", "string");
    placeOfBirth.put("description", "The place of birth of the artist, city, country");

    var properties = new HashMap<String, Object>();
    properties.put("first_name", firstName);
    properties.put("last_name", placeOfBirth);
    properties.put("date_of_birth", dateOfBirth);
    properties.put("place_of_birth", placeOfBirth);
    return properties;
  }
}
