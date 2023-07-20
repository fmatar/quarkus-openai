package org.slixes.platform.openai.model;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.util.Set;

@Builder
@Data
public class Model {
	String id;
	String object;
	long created;
	String owned_by;
	Set<Permission> permission;
	String root;
	String parent;

}







