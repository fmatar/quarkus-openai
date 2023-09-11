package org.slixes.platform.openai.audio;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import java.io.File;
import java.net.URL;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slixes.platform.openai.OpenAI;
import org.slixes.platform.openai.openai.audio.AudioResponseFormat;
import org.slixes.platform.openai.openai.audio.TranscriptionRequest;
import org.slixes.platform.openai.openai.audio.TranscriptionResponse;

@QuarkusTest
@Disabled
class TranscriptionTest {

  @Inject
  OpenAI client;

  @Test
  void createTranscription() {

    URL resource = getClass().getClassLoader().getResource("meaning-of-life.mp3");
    assertThat(resource, notNullValue());

    var file = new File(resource.getFile());
    assertThat(file, notNullValue());

    var request = TranscriptionRequest.builder()
      .file(file)
      .temperature(0.9)
      .model("whisper-1")
      .responseFormat(AudioResponseFormat.JSON)
      .transcription("srt")
      .language("en").file(file).build();

    File file1 = request.getFile();
    assertThat(file1, equalTo(file));

    TranscriptionResponse indefinitely = client.createTranscription(request).await().indefinitely();


  }


}
