package io.slixes.platform.openai.openai.moderation;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ModerationResult(
  @JsonProperty("flagged") boolean flagged,
  @JsonProperty("categories") Categories categories,
  @JsonProperty("category_scores") CategoryScores category_scores
) {}
