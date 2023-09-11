import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import io.quarkus.test.junit.QuarkusTest;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import jakarta.ws.rs.core.MediaType;
import java.io.File;
import java.net.URL;
import org.junit.jupiter.api.Test;

@QuarkusTest
class FileUploadTest {

  @Test
  void testFileUploadEndpoint() {
    // Prepare a test file and metadata
    URL resource = getClass().getClassLoader().getResource("test-file.txt");
    if (resource == null) {
      throw new IllegalArgumentException("File not found!");
    }
    File testFile = new File(resource.getFile());
    var meta = new JsonObject().put("key", "value").put("owner", "fady");
    String metadata = "{\"key\": \"value\"}";

    given()
      .multiPart("file", testFile)
      .multiPart("metadata", meta, MediaType.APPLICATION_JSON)
      .contentType(MediaType.MULTIPART_FORM_DATA)
      .when()
      .post("/feed/upload")
      .then()
      .statusCode(200)
      .body(is(Json.encode("File uploaded successfully with metadata")));
  }

}
