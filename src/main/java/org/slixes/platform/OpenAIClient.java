package org.slixes.platform;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestStreamElementType;
import org.slixes.platform.openai.completion.CompletionChunk;
import org.slixes.platform.openai.completion.CompletionRequest;
import org.slixes.platform.openai.completion.CompletionResult;
import org.slixes.platform.openai.model.Model;

import java.util.Set;

@Path("/v1")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RegisterRestClient(configKey = "openai-service")
@ClientHeaderParam(name = "Authorization", value = "{lookupAuth}")
public interface OpenAIClient {

	@GET
	@Path("/models")
	Uni<ListResponseWrapper<Model>> models();


	@GET
	@Path("/models/{id}")
	Uni<Model> model(@PathParam("id") String id);

	@POST
	@Path("/completions")
	Uni<CompletionResult> createCompletion(CompletionRequest request);

	@POST
	@Path("/completions")
	@Produces(MediaType.SERVER_SENT_EVENTS)
	@RestStreamElementType(MediaType.APPLICATION_JSON)
	Multi<CompletionChunk> createStreamedCompletion(CompletionRequest request);


	default String lookupAuth() {
		return "Bearer " + ConfigProvider.getConfig().getValue("openai.token", String.class);
	}

	record ListResponseWrapper<T>(String object, Set<T> data) {
	}
}