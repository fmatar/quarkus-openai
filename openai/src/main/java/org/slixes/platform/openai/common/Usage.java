package org.slixes.platform.openai.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Usage(@JsonProperty("prompt_tokens") long promptTokens,
                    @JsonProperty("completion_tokens") long completionTokens,
                    @JsonProperty("total_tokens") long totalTokens) {

}
