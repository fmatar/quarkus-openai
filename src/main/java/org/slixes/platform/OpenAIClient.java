package org.slixes.platform;

import io.quarkus.logging.Log;
import jakarta.json.JsonObject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
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
	ListResponseWrapper<Model> models();


	@GET
	@Path("/models/{id}")
	Model model(@PathParam("id") String id);


	default String lookupAuth() {
		var token = "Bearer " + ConfigProvider.getConfig().getValue("openai.token", String.class);
		Log.info(token);
		return token;
	}


	record ListResponseWrapper<T>(String object, Set<T> data) {
	}
}