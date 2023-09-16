package org.slixes.platform.openai.audio;

import static org.hamcrest.MatcherAssert.assertThat;
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
import org.slixes.platform.openai.openai.audio.TranslationRequest;

@QuarkusTest
class TranslationTest {

  @Inject
  OpenAI client;

  @Test
  void createTranslation() {
    URL resource = getClass().getClassLoader().getResource("banane.mp3");
    assertThat(resource, notNullValue());
    var file = new File(resource.getFile());
    var request = new TranslationRequest(file, "whisper-1", "json", 0.9);

    var translationResponse = client.createTranslation(request).await().indefinitely();
    assertThat(translationResponse.text(), equalTo("I want to eat a banana."));

    var verboseRequest = new TranslationRequest(file, "whisper-1", "verbose_json", 0.9);
    var verboseTranscriptionResponse = client.createTranslation(verboseRequest).await().indefinitely();
    assertThat(verboseTranscriptionResponse.text(), equalTo("I want to eat a banana."));
    assertThat(verboseTranscriptionResponse.task(), equalTo("translate"));

    var vttRequest = new TranslationRequest(file, "whisper-1", "vtt", 0.9);
    var vttResponse = client.createStringTranslation(vttRequest).await().indefinitely();
    Log.info(Json.encode(vttResponse));

    var srtRequest = new TranslationRequest(file, "whisper-1", "srt", 0.9);
    var srtResponse = client.createStringTranslation(srtRequest).await().indefinitely();
    Log.info(Json.encode(srtResponse));
  }
}
