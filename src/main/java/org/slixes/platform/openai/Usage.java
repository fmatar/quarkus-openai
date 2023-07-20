package org.slixes.platform.openai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * The OpenAI token usage per request
 *
 * @param promptTokens     The number of prompt tokens used.
 * @param completionTokens The number of completion tokens used.
 * @param totalTokens      The number of total tokens used
 */
public class Usage {
	@JsonProperty("prompt_tokens")
	long promptTokens;
	@JsonProperty("completion_tokens")
	long completionTokens;
	@JsonProperty("total_tokens")
	long totalTokens;
}
