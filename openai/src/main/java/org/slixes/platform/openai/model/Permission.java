package org.slixes.platform.openai.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Permission {
	String id;
	String object;
	long created;
	boolean allow_create_engine;
	boolean allow_sampling;
	boolean allow_logprobs;
	boolean allow_search_indices;
	boolean allow_view;
	boolean allow_fine_tuning;
	String organization;
	String group;
	boolean is_blocking;
}