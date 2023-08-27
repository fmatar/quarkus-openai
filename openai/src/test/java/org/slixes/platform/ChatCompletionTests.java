package org.slixes.platform;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

import io.quarkus.logging.Log;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.core.json.Json;
import jakarta.inject.Inject;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.slixes.platform.openai.ChatMessage;
import org.slixes.platform.openai.Role;
import org.slixes.platform.openai.completion.chat.ChatCompletionRequest;
import org.slixes.platform.openai.completion.chat.ChatCompletionResult;
import org.slixes.platform.openai.completion.chat.Function;
import org.slixes.platform.openai.completion.chat.Parameters;

@QuarkusTest
class ChatCompletionTests {

  @Inject
  OpenAI client;

//  @Test
//  void testChatCompletion() {
//    var req = new ChatCompletionRequest();
//    req.setModel("gpt-4-0613");
//    req.setMaxTokens(300);
//    req.setTemperature(0.9D);
//    req.setMessages(List.of(
//      ChatMessage.builder().content("You are the best musician in the world.").role(Role.SYSTEM).build(),
//      ChatMessage.builder().content("What are the most complex jazz chord variations?").role(Role.SYSTEM).build(),
//      client.createChatCompletion(req).onItem().invoke(result -> {
//        Log.info(Json.encode(result));
//        assertThat(result.getChoices().size(), equalTo(1));
//        var chatCompletionChoice = result.getChoices().get(0);
//        assertThat(chatCompletionChoice.getMessage().getContent(), notNullValue());
//        assertThat(chatCompletionChoice.getMessage().getRole(), equalTo("assistant"));
//      }).await().indefinitely();
//    /* Let's test the same execution using the streaming approach */
//    req.setStream(true);
//    var chunks = client.createStreamedChatCompletion(req).collect().asList().await().indefinitely();
//    // Check if all elements have the same ID
//    var expectedId = chunks.get(0).getId();
//    assertTrue(chunks.stream().allMatch(element -> element.getId().equals(expectedId)));
//  }


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
    ChatCompletionResult result = client.createChatCompletion(req).await().indefinitely();
    assertThat(result.getChoices().size(), is(greaterThan(0)));

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
