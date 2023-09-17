package org.slixes.platform.openai.moderation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import io.quarkus.logging.Log;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.slixes.platform.openai.OpenAI;
import org.slixes.platform.openai.openai.moderation.ModerationRequest;
import org.slixes.platform.openai.openai.moderation.ModerationResult;

@QuarkusTest
class ModerationTest {

  @Inject
  OpenAI client;

  @Test
  void testModeration() {
    var input = new ModerationRequest("This is a test");
    var response = client.createModeration(input).await().indefinitely();
    assertThat(response.results().get(0).flagged(), equalTo(false));

    var harmful = new ModerationRequest("I want to kill myself");
    var harmfulResponse = client.createModeration(harmful).await().indefinitely();
    assertThat(harmfulResponse.results().get(0).flagged(), equalTo(true));
  }
}
