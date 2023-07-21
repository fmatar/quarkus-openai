package org.slixes.platform;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.RestStreamElementType;
import org.slixes.platform.openai.completion.CompletionChunk;
import org.slixes.platform.openai.completion.CompletionRequest;
import org.slixes.platform.openai.completion.CompletionResult;
import org.slixes.platform.openai.model.Model;

import java.util.Set;

@ApplicationScoped
public class OpenAI {

	@RestClient
	OpenAIClient client;

	Uni<Set<Model>> models() {
		return client.models().onItem().transform(OpenAIClient.ListResponseWrapper::data);
	}

	Uni<Model> model(String id) {
		return client.model(id);
	}


	Uni<CompletionResult> createCompletion(CompletionRequest request) {
		return client.createCompletion(request);
	}

	Multi<CompletionChunk> createStreamedCompletion(CompletionRequest request) {
		return client.createStreamedCompletion(request)
				.filter(chunk -> !chunk.getId().equals("[DONE]"));
	}
};