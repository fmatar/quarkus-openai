package io.slixes.platform.openai;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

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
}
