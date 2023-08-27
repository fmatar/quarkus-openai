package org.slixes.platform;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import io.quarkus.logging.Log;
import io.vertx.core.json.Json;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.slixes.platform.openai.ChatMessage;
import org.slixes.platform.openai.Role;
import org.slixes.platform.openai.Usage;
import org.slixes.platform.openai.completion.chat.ChatCompletionChoice;
import org.slixes.platform.openai.completion.chat.ChatCompletionResult;
import org.slixes.platform.openai.completion.chat.FunctionCall;

class SerializationTest {

  @Test
  void chatMessageSerialization() {
    var chatMsgJson = """
      {
        "role": "user",
        "content": "content",
        "name": "name",
        "functionCall": {
          "name": "name",
          "arguments": "args"
        }
      }
      """;
    var decodedCm = Json.decodeValue(chatMsgJson, ChatMessage.class);
    assertThat(decodedCm.getRole(), equalTo(Role.USER));
    assertThat(decodedCm.getContent(), equalTo("content"));

    var cm = ChatMessage.builder().role(Role.USER).content("content").name("name")
      .functionCall(FunctionCall.builder().name("name").arguments("args").build()).build();
    assertThat(cm, equalTo(decodedCm));
  }

  @Test
  void testSerialization() {
    var json = """
      {
        "id": "chatcmpl-7sAGOEYrOzAb3uUh5RnBRc2ReBLIo",
        "object": "chat.completion",
        "created": 1693143520,
        "model": "gpt-4-0613",
        "choices": [
          {
            "index": 0,
            "message": {
              "role": "assistant"
            },
            "finish_reason": "length"
          }
        ],
        "usage": {
          "prompt_tokens": 48,
          "completion_tokens": 300,
          "total_tokens": 348
        }
      }
      """;

    ChatCompletionResult chatCompletionResult = Json.decodeValue(json, ChatCompletionResult.class);
    assertThat(chatCompletionResult.getId(), notNullValue(String.class));
    assertThat(chatCompletionResult.getObject(), equalTo("chat.completion"));
    assertThat(chatCompletionResult.getModel(), notNullValue(String.class));
    assertThat(chatCompletionResult.getUsage(), notNullValue(Usage.class));

    var retrievedUsage = chatCompletionResult.getUsage();
    assertThat(retrievedUsage.getCompletionTokens(), equalTo(300L));
    assertThat(retrievedUsage.getPromptTokens(), equalTo(48L));
    assertThat(retrievedUsage.getTotalTokens(), equalTo(348L));

    var usageJson = """
      {
        "prompt_tokens": 48,
        "completion_tokens": 300,
        "total_tokens": 348
      }
      """;

    var decodedUsage = Json.decodeValue(usageJson, Usage.class);
    var usage = Usage.builder().promptTokens(48).completionTokens(300).totalTokens(348).build();

    assertThat(usage, equalTo(retrievedUsage));
    assertThat(usage, equalTo(decodedUsage));
  }

  @Test
  void testChatCompletionResultSerialization() {
    var result =  ChatCompletionResult.builder().id("aaa").object("chat.completion").created(123L).model("gpt-4-0613").build();

    var usage = Usage.builder().promptTokens(48).completionTokens(300).totalTokens(348).build();

    result.setUsage(usage);

    List<ChatCompletionChoice> choices = new ArrayList<>();

    var choice = ChatCompletionChoice.builder()
      .finishReason("stop")
      .build();

    choices.add(choice);

    var encoded = Json.encode(result);
    var decoded = Json.decodeValue(encoded, ChatCompletionResult.class);

    assertThat(decoded, equalTo(result));

    Log.info("encoded: " + encoded);
  }


  @Test
  void testCompletionChoiceSerialization() {
    var completionChoice = ChatCompletionChoice.builder()
      .index(0)
      .finishReason("stop")
      .message(ChatMessage.builder().content("test").role(Role.SYSTEM).build())
      .build();

    var encoded = Json.encode(completionChoice);

    var decoded = Json.decodeValue(encoded, ChatCompletionChoice.class);

    assertThat(decoded, equalTo(completionChoice));
    Log.info(encoded);
  }

}
