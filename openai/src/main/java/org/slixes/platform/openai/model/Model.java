package org.slixes.platform.openai.model;

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
  String owned_by;
  Set<Permission> permission;
  String root;
  String parent;
}







