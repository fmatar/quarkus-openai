package org.slixes.platform.openai.openai.audio;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AudioResponseFormat {
  JSON("json"), TEXT("text"), SRT("srt"), VERBOSE_JSON("verbose_json"), VTT("vtt");

  private final String format;

  AudioResponseFormat(String format) {
    this.format = format;
  }

  @JsonValue
  public String toValue() {
    return format;
  }

  @JsonCreator
  public static AudioResponseFormat forValue(String value) {
    for (AudioResponseFormat format : values()) {
      if (format.format.equalsIgnoreCase(value)) {
        return format;
      }
    }
    throw new IllegalArgumentException("Invalid value for " + AudioResponseFormat.class.getSimpleName() + ": " + value);
  }
}
