package org.slixes.platform.openai.openai.common;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {

  SYSTEM("system"), USER("user"), ASSISTANT("assistant"), FUNCTION("function");

  private String roleValue;

  Role(String roleValue) {
    this.roleValue = roleValue;
  }

  @JsonValue
  public String toValue() {
    return roleValue;
  }

  @JsonCreator
  public static Role forValue(String value) {
    for (Role role : values()) {
      if (role.roleValue.equalsIgnoreCase(value)) {
        return role;
      }
    }
    throw new IllegalArgumentException("Invalid value for CustomRole: " + value);
  }
}