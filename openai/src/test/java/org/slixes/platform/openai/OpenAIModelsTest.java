package org.slixes.platform.openai;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.jboss.resteasy.reactive.ClientWebApplicationException;
import org.junit.jupiter.api.Test;

@QuarkusTest
class OpenAIModelsTest {

  @Inject
  OpenAI client;

  @Test
  void testModels() {
    var models = client.models().await().indefinitely();
    assertThat(models, is(not(empty())));

    var curieModel = client.model("curie").await().indefinitely();
    assertThat(curieModel.getId(), is(equalTo("curie")));

    assertThrows(ClientWebApplicationException.class, () -> client.model("invalid").await().indefinitely());
  }

}
