package org.slixes.platform;

import io.quarkus.logging.Log;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.core.json.Json;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;
import org.slixes.platform.openai.model.Model;

import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
public class OpenAIClientTest {

	@RestClient
	OpenAIClient client;

	@Test
	public void testModelsEndpoint() {
		var models = client.models();
		assertThat(models.data(), is(not(empty())));
		assertThat(models.object(), is(equalTo("list")));

		var curieModel = client.model("curie");
		assertThat(curieModel.getId(), is(equalTo("curie")));
	}
}