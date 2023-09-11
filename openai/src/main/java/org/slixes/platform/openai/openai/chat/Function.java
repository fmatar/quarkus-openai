package org.slixes.platform.openai.openai.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true, setterPrefix = "set")
@AllArgsConstructor
@NoArgsConstructor
public class Function {
  private String name;
  private String description;
  private Parameters parameters;
}
