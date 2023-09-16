package org.slixes.platform.openai.audio;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import io.quarkus.logging.Log;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.core.json.Json;
import jakarta.inject.Inject;
import java.io.File;
import java.net.URL;
import org.junit.jupiter.api.Test;
import org.slixes.platform.openai.OpenAI;
import org.slixes.platform.openai.openai.audio.TranscriptionRequest;

@QuarkusTest
class TranscriptionTest {

  @Inject
  OpenAI client;

  @Test
  void createTranscription() {
    URL resource = getClass().getClassLoader().getResource("meaning-of-life.mp3");
    assertThat(resource, notNullValue());
    var file = new File(resource.getFile());
    var request = new TranscriptionRequest(file, "whisper-1", "json", 0.9, "en");

    var transcriptionResponse = client.createTranscription(request).await().indefinitely();
    assertThat(transcriptionResponse.text(), containsString("My son has taught me the meaning of life"));

    var verboseRequest = new TranscriptionRequest(file, "whisper-1", "verbose_json", 0.9, "en");
    var verboseTranscriptionResponse = client.createTranscription(verboseRequest).await().indefinitely();
    assertThat(verboseTranscriptionResponse.text(), containsString("My son has taught me the meaning of life"));
    assertThat(verboseTranscriptionResponse.task(), equalTo("transcribe"));
    assertThat(verboseTranscriptionResponse.language(), equalTo("english"));

    var vttRequest = new TranscriptionRequest(file, "whisper-1", "vtt", 0.9, "en");
    var vttResponse = client.createStringTranscription(vttRequest).await().indefinitely();
    Log.info(Json.encode(vttResponse));


    var srtRequest = new TranscriptionRequest(file, "whisper-1", "srt", 0.9, "en");
    var srtResponse = client.createStringTranscription(srtRequest).await().indefinitely();
    Log.info(Json.encode(srtResponse));
  }
}
