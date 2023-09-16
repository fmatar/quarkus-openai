package org.slixes.platform.openai;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.quarkus.logging.Log;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import java.util.ArrayList;
import java.util.List;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.slixes.platform.openai.openai.audio.TranscriptionResponse;
import org.slixes.platform.openai.openai.common.Role;
import org.slixes.platform.openai.openai.common.Usage;
import org.slixes.platform.openai.openai.chat.ChatCompletionChoice;
import org.slixes.platform.openai.openai.chat.ChatCompletionResult;
import org.slixes.platform.openai.openai.chat.ChatMessage;
import org.slixes.platform.openai.openai.chat.Function;
import org.slixes.platform.openai.openai.chat.FunctionCall;
import org.slixes.platform.openai.openai.model.Model;
import org.slixes.platform.openai.openai.model.Permission;

class SerializationTest{

  @Test
  void chatMessageSerialization() {
    var chatMsgJson = """
      {
        "role": "user",
        "content": "content",
        "name": "name",
        "function_call": {
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
              "role": "assistant",
              "content" : "blah"
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
    MatcherAssert.assertThat(chatCompletionResult.getUsage(), notNullValue(Usage.class));

    assertThat(chatCompletionResult.getChoices().size(), equalTo(1));
    assertThat(chatCompletionResult.getChoices().get(0).getIndex(), equalTo(0));
    assertThat(chatCompletionResult.getChoices().get(0).getFinishReason(), equalTo("length"));
    assertThat(chatCompletionResult.getChoices().get(0).getMessage().getRole(), equalTo(Role.ASSISTANT));
    assertThat(chatCompletionResult.getChoices().get(0).getMessage().getContent(), equalTo("blah"));
    var retrievedUsage = chatCompletionResult.getUsage();
    MatcherAssert.assertThat(retrievedUsage.completionTokens(), equalTo(300L));
    MatcherAssert.assertThat(retrievedUsage.promptTokens(), equalTo(48L));
    MatcherAssert.assertThat(retrievedUsage.totalTokens(), equalTo(348L));

    var usageJson = """
      {
        "prompt_tokens": 48,
        "completion_tokens": 300,
        "total_tokens": 348
      }
      """;

    var decodedUsage = Json.decodeValue(usageJson, Usage.class);
    var usage = new Usage(48, 300, 348);

    assertThat(usage, equalTo(retrievedUsage));
    assertThat(usage, equalTo(decodedUsage));
  }

  @Test
  void testChatCompletionResultSerialization() {
    var result = ChatCompletionResult.builder().id("aaa").object("chat.completion").created(123L).model("gpt-4-0613")
      .build();

    var usage = new Usage(48, 300, 348);

    result.setUsage(usage);

    List<ChatCompletionChoice> choices = new ArrayList<>();

    var choice = ChatCompletionChoice.builder().finishReason("stop").build();

    choices.add(choice);

    var encoded = Json.encode(result);
    var decoded = Json.decodeValue(encoded, ChatCompletionResult.class);

    assertThat(decoded, equalTo(result));
  }


  @Test
  void testCompletionChoiceSerialization() {
    var json = """
            {
              "index": 0,
              "delta": {
                  "role": "assistant",
                  "content": "The"
              },
              "finish_reason": null
            }
      """;

    ChatCompletionChoice chatCompletionChoice = Json.decodeValue(json, ChatCompletionChoice.class);
    assertThat(chatCompletionChoice.getIndex(), equalTo(0));
    assertThat(chatCompletionChoice.getFinishReason(), nullValue());
    assertThat(chatCompletionChoice.getMessage().getContent(), equalTo("The"));
    assertThat(chatCompletionChoice.getMessage().getRole(), equalTo(Role.ASSISTANT));
  }


  @Test
  void testUsageObject() {
    var usage = new Usage(48, 300, 348);
    var decoded = Json.encode(usage);
    var encoded = Json.decodeValue(decoded, Usage.class);
    assertThat(encoded, equalTo(usage));
  }

  @Test
  void testRoleObject() {
    var role = Role.USER;
    var decoded = Json.encode(role);
    var encoded = Json.decodeValue(decoded, Role.class);
    assertThat(encoded, equalTo(role));

    Role user = Role.forValue("user");
    assertThat(user, equalTo(Role.USER));

    assertThrows(IllegalArgumentException.class, () -> {
      Role.forValue("blah");
    });
  }

  @Test
  void testModelObject() {
    var model = Model.builder().id("id").object("object").build();
    var decoded = Json.encode(model);
    var encoded = Json.decodeValue(decoded, Model.class);
    assertThat(encoded, equalTo(model));
  }

  @Test
  void testPermissionObject() {
    var json = """
       {
         "id": "modelperm-P7lby9Sdb6rLW8qqie46YnE0",
         "object": "model_permission",
         "created": 1693000468,
         "allow_create_engine": false,
         "allow_sampling": false,
         "allow_logprobs": false,
         "allow_search_indices": false,
         "allow_view": false,
         "allow_fine_tuning": false,
         "organization": "*",
         "group": null,
         "is_blocking": false
       }
      """;

    var permission = Json.decodeValue(json, Permission.class);
    assertThat(permission.getId(), equalTo("modelperm-P7lby9Sdb6rLW8qqie46YnE0"));

    Permission p = Permission.builder().id("modelperm-P7lby9Sdb6rLW8qqie46YnE0").object("model_permission")
      .created(1693000468L).ownedBy("openai").allowCreateEngine(true).allowSampling(false).build();
    var encoded = Json.encode(p);
    var decoded = Json.decodeValue(encoded, Permission.class);

    assertThat(decoded, equalTo(p));
    assertThat(decoded.toString(), notNullValue());
    assertThat(decoded.hashCode(), lessThanOrEqualTo(0));
  }


  @Test
  void testCompletionResultWithFunctionCall() {
    var json = """
      {
          "id": "chatcmpl-7sN98iBV0rOBFU9xli6fdJiPatCt9",
          "object": "chat.completion",
          "created": 1693193042,
          "model": "gpt-3.5-turbo-0613",
          "choices": [
              {
                  "index": 0,
                  "message": {
                      "role": "assistant",
                      "content": null,
                      "function_call": {
                          "name": "get_artist_info",
                          "arguments": "{\\n  \\"place_of_birth\\": \\"Algeciras, Spain\\",\\n  \\"date_of_birth\\": \\"December 21, 1947\\",\\n  \\"last_name\\": \\"de Lucia\\",\\n  \\"first_name\\": \\"Francisco\\"\\n}"
                      }
                  },
                  "finish_reason": "function_call"
              }
          ],
          "usage": {
              "prompt_tokens": 134,
              "completion_tokens": 56,
              "total_tokens": 190
          }
      }
      """;

    ChatCompletionResult chatCompletionResult = Json.decodeValue(json, ChatCompletionResult.class);

    assertThat(chatCompletionResult.getChoices().get(0).getMessage().getFunctionCall().getName(),
      equalTo("get_artist_info"));
    var jsonObj = new JsonObject(
      chatCompletionResult.getChoices().get(0).getMessage().getFunctionCall().getArguments());
    assertThat(jsonObj.getString("place_of_birth"), equalTo("Algeciras, Spain"));
  }

  @Test
  void functionObjectTest() {
    var json = """
      {
        "name": "get_artist_info",
        "description": "Get the artist information",
        "parameters": {
          "type": "object",
          "properties": {
            "place_of_birth": {
                "description": "The place of birth of the artist, city, country",
                "type": "string"
            },
            "date_of_birth": {
                "description": "The date of birth of the artist",
                "type": "string"
            },
            "last_name": {
                "description": "The place of birth of the artist, city, country",
                "type": "string"
            },
            "first_name": {
                "description": "The first name of the artist",
                "type": "string"
            }
          },
          "required": [
            "first_name",
            "last_name",
            "date_of_birth",
            "place_of_birth"
          ]
        }
      }
      """;

    var func = Json.decodeValue(json, Function.class);
    assertThat(func.toString(), notNullValue(String.class));
    assertThat(func.hashCode(), lessThanOrEqualTo(0));
    assertThat(func.getName(), equalTo("get_artist_info"));
    assertThat(func.getDescription(), equalTo("Get the artist information"));
    assertThat(func.getParameters().getType(), equalTo("object"));

    var properties = func.getParameters().getProperties();
    assertThat(properties.size(), equalTo(4));
    assertThat(func.getParameters().getRequired().size(), equalTo(4));
  }

  @Test
  void testTranscriptionVerboseJsonModel(){
    var json = """
      {
          "task": "transcribe",
          "language": "english",
          "duration": 3.16,
          "text": "My son has taught me the meaning of life.",
          "segments": [
              {
                  "id": 0,
                  "seek": 0,
                  "start": 0.0,
                  "end": 2.08,
                  "text": " My son has taught me the meaning of life.",
                  "tokens": [
                      50364,
                      1222,
                      1872,
                      575,
                      5928,
                      385,
                      264,
                      385,
                      8415,
                      295,
                      993,
                      13,
                      50468
                  ],
                  "temperature": 1.0,
                  "avg_logprob": -0.7123122215270996,
                  "compression_ratio": 0.8723404255319149,
                  "no_speech_prob": 0.006854991428554058
              }
          ]
      }
      """;

    var response = Json.decodeValue(json, TranscriptionResponse.class);

    Log.info(Json.encode(response));

  }
}
