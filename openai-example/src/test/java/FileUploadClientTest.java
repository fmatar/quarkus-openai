import static org.junit.jupiter.api.Assertions.assertEquals;

import com.slixes.platform.FileUploadClient;
import com.slixes.platform.FileUploadForm;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import jakarta.ws.rs.core.Response;
import java.io.File;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

@QuarkusTest
class FileUploadClientTest {

  @RestClient
  FileUploadClient fileUploadClient;

  @Test
  void testFileUpload() {
    var path = getClass().getClassLoader().getResource("test-file.txt").getFile();

    var file = new File(path);
    var metadata = new JsonObject().put("key", "value").put("owner", "fady");
    var temperature = 0.9;
    FileUploadForm form =  new FileUploadForm();
    form.setFile(file);
    form.setMetadata(metadata);
    form.setTemperature(temperature);

    Response response = fileUploadClient.uploadFile(form).await().indefinitely();

    assertEquals(200, response.getStatus());
    assertEquals(Json.encode("File uploaded successfully with metadata"), response.readEntity(String.class));
  }
}
