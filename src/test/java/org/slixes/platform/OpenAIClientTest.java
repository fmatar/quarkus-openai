package org.slixes.platform;

import io.quarkus.logging.Log;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.core.json.Json;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;
import org.slixes.platform.openai.completion.CompletionRequest;
import org.slixes.platform.openai.completion.CompletionResult;
import org.slixes.platform.openai.model.Model;

import java.util.Set;

import static io.restassured.RestAssured.defaultParser;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class OpenAIClientTest {

	@RestClient
	OpenAIClient client;

	@Test
	public void testModelsEndpoint() {
		var models = client.models().await().indefinitely();
		assertThat(models.data(), is(not(empty())));
		assertThat(models.object(), is(equalTo("list")));

		var curieModel = client.model("curie").await().indefinitely();
		assertThat(curieModel.getId(), is(equalTo("curie")));
	}

	@Test
	public void testCompletionRequest() {
		var req = new CompletionRequest();
		req.setModel("text-davinci-003");
		req.setPrompt("Give me the name of a song by Iron Maiden that starts with I am a man who walks alone");
		req.setTemperature(1.9);
		req.setMaxTokens(100);
		req.setTopP(1D);
		req.setFrequencyPenalty(0D);
		req.setPresencePenalty(0D);
		Log.info(Json.encode(req));

		var result = client.createCompletion(req).await().indefinitely();
		Log.info(Json.encode(result));
	}
}