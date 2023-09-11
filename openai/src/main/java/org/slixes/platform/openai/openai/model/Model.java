package org.slixes.platform.openai.openai.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Set;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Builder
@Value
@Jacksonized
public class Model {

  String id;
  String object;
  long created;
  @JsonProperty("owned_by")
  String ownedBy;
  Set<Permission> permission;
  String root;
  String parent;
}







