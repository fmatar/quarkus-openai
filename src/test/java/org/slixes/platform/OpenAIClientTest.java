package org.slixes.platform;

import io.quarkus.logging.Log;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Multi;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;
import org.slixes.platform.openai.ChatMessage;
import org.slixes.platform.openai.Role;
import org.slixes.platform.openai.completion.CompletionChunk;
import org.slixes.platform.openai.completion.CompletionRequest;
import org.slixes.platform.openai.completion.chat.ChatCompletionRequest;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
public class OpenAIClientTest {

	@Inject
	OpenAI client;

	@Test
	public void testModelsEndpoint() {
		var models = client.models().await().indefinitely();
		assertThat(models, is(not(empty())));

		var curieModel = client.model("curie").await().indefinitely();
		assertThat(curieModel.getId(), is(equalTo("curie")));
	}

	@Test
	public void testCompletionRequest() throws ExecutionException, InterruptedException {
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


		req.setStream(true);

		Multi<CompletionChunk> completionStage = client.createStreamedCompletion(req);

		List<CompletionChunk> indefinitely = completionStage.onItem().invoke(completionChunk -> {
			CompletionChunk chunk = Json.decodeValue(completionChunk.toString(), CompletionChunk.class);
			Log.info(Json.encode(completionChunk));
		}).collect().asList().await().indefinitely();

		System.out.println(indefinitely.size());
	}

	@Test
	public void testChatCompletion() {

		var req = new ChatCompletionRequest();
		req.setModel("gpt-4-32k");
		req.setMaxTokens(300);
		req.setTemperature(0.9D);
		req.setN(4);
		req.setMessages(List.of(
				new ChatMessage("I am the best musician in the world.", Role.SYSTEM, "toota", null),
				new ChatMessage("What are the most complex jazz chord variations?", Role.USER, "toota", null),
				new ChatMessage("Hmm let's ask the master", Role.ASSISTANT, "toota", null)
		));

		client.createChatCompletion(req).onItem().invoke(result -> {
			Log.info(Json.encode(result));
		}).await().indefinitely();

	}
}
