package org.slixes.platform.openai.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Builder
@Value
@Jacksonized
public class Permission {

  String id;
  String object;
  long created;
  @JsonProperty("owned_by")
  String ownedBy;
  @JsonProperty("allow_create_engine")
  boolean allowCreateEngine;
  @JsonProperty("allow_sampling")
  boolean allowSampling;
  @JsonProperty("allow_logprobs")
  boolean allowLogProbs;
  @JsonProperty("allow_search_indices")
  boolean allowSearchIndices;
  @JsonProperty("allow_view")
  boolean allowView;
  @JsonProperty("allow_fine_tuning")
  boolean allowFineTuning;
  String organization;
  String group;
  @JsonProperty("is_blocking")
  boolean blocking;
}