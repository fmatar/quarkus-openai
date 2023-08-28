package org.slixes.platform;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

import io.quarkus.logging.Log;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.core.json.Json;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.slixes.platform.openai.completion.CompletionRequest;

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


}
