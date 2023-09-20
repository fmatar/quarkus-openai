package io.slixes.platform.openai.moderation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import io.quarkus.test.junit.QuarkusTest;
import io.slixes.platform.openai.OpenAI;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import io.slixes.platform.openai.openai.moderation.ModerationRequest;

@QuarkusTest
class ModerationTest {

  @Inject
  OpenAI client;

  @Test
  void testModeration() {
    var input = new ModerationRequest("This is a test");
    var response = client.createModeration(input).await().indefinitely();
    assertThat(response.results().get(0).flagged(), equalTo(false));
  }
}
