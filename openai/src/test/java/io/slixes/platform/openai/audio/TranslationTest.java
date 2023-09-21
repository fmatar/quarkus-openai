package io.slixes.platform.openai.audio;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import io.quarkus.test.junit.QuarkusTest;
import io.slixes.platform.openai.OpenAI;
import io.slixes.platform.openai.openai.audio.TranslationRequest;
import jakarta.inject.Inject;
import java.io.File;
import java.net.URL;
import org.junit.jupiter.api.Test;

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
    assertThat(translationResponse.text(), containsString("I want to eat a banana"));

    var verboseRequest = new TranslationRequest(file, "whisper-1", "verbose_json", 0.9);
    var verboseTranscriptionResponse = client.createTranslation(verboseRequest).await().indefinitely();
    assertThat(verboseTranscriptionResponse.text(), containsString("I want to eat a banana"));
    assertThat(verboseTranscriptionResponse.task(), equalTo("translate"));

    var vttRequest = new TranslationRequest(file, "whisper-1", "vtt", 0.9);
    var vttResponse = client.createStringTranslation(vttRequest).await().indefinitely();
    assert (vttResponse.contains("I want to eat a banana"));

    var srtRequest = new TranslationRequest(file, "whisper-1", "srt", 0.9);
    var srtResponse = client.createStringTranslation(srtRequest).await().indefinitely();
  }
}
