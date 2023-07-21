package org.slixes.platform.openai;

import io.vertx.core.json.Json;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyReader;
import jakarta.ws.rs.ext.Provider;
import org.slixes.platform.openai.completion.CompletionChunk;
import org.slixes.platform.openai.completion.chat.ChatCompletionChunk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
@Consumes(MediaType.APPLICATION_JSON)
public class ChatCompletionChunkBodyReader implements MessageBodyReader<ChatCompletionChunk> {

	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return type == ChatCompletionChunk.class;
	}

	@Override
	public ChatCompletionChunk readFrom(Class<ChatCompletionChunk> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(entityStream))) {
			var json = reader.readLine();
			if (json.equals("[DONE]")) {
				entityStream.close();
				var result = new ChatCompletionChunk();
				result.setId(json);
				return result;
			}
			var x = Json.decodeValue(json, ChatCompletionChunk.class);
			return x;
		}
	}
}


