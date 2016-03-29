package com.testedminds.template;

import com.google.gson.Gson;
import com.testedminds.template.models.Example;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

// Specifies the public interface for the API.
public class RestfulApiFunctionalTest extends FunctionalTestSuite {

  @Test
  public void getRootIncludesVersionAndReturnsOK() throws Exception {
    String response = http.get(DEFAULT_HOST_URL, 200);
    assertTrue(response.contains("Version: development"));
  }

  @Test
  public void getJsonForExistingIdReturnsOK() throws Exception {
    Example seed = new Example("foo", "bar");
    Example saved = dao.create(seed);

    String response = http.get(DEFAULT_HOST_URL + "/examples/" + saved.getId(), 200);
    Example fromResponse = new Gson().fromJson(response, Example.class);
    assertEquals(saved, fromResponse);
  }

  @Test
  public void getForMissingIdReturnsNotFound() throws Exception {
    String response = http.get(DEFAULT_HOST_URL + "/examples/42", 404);
    assertTrue(response.contains("42"));
  }

  @Test
  public void getForInvalidIdReturnsBadRequest() throws Exception {
    http.get(DEFAULT_HOST_URL + "/examples/foo", 400);
  }

  @Test
  public void postWithValidJsonShouldReturnIdOfCreatedExample() throws Exception {
    String json = new Gson().toJson(new Example("foo", "bar"));
    String createResponse = http.postJson(DEFAULT_HOST_URL + "/examples", json, 201);
    long createdId = Long.valueOf(createResponse);
    assertTrue(createdId > 0);
  }

  @Test
  public void postWithInvalidJsonReturnsBadRequest() throws Exception {
    String response = http.postJson(DEFAULT_HOST_URL + "/examples", "bunk body", 400);
    assertEquals("Invalid JSON: bunk body", response);
  }

  @Test
  public void postWithInvalidExampleReturnsBadRequest() throws Exception {
    String incompleteExample = "{\"name\" : \"foo\"}";
    String response = http.postJson(DEFAULT_HOST_URL + "/examples", incompleteExample, 400);
    assertEquals("Invalid Example: type is a required field", response);
  }

  @Test
  public void putWithValidJsonAndIdReturnsUpdatedExample() throws Exception {
    Example saved = dao.create(new Example("foo", "bar"));
    String response = http.putJson(DEFAULT_HOST_URL + "/examples/" + saved.getId(), "junk", 400);
    assertEquals("Example is not valid", response);
  }

  @Test
   public void putWithMissingIdReturnsNotFound() throws Exception {
    Example saved = dao.create(new Example("foo", "bar"));
    Example update = new Example(saved.getId(), "foo", "baz");
    String response = http.putJson(DEFAULT_HOST_URL + "/examples/" + saved.getId()+1, new Gson().toJson(update), 404);
    assertEquals("Example with id " + saved.getId()+1 +" does not exist.", response);
  }

  @Test
   public void putWithInvalidIdReturnsBadRequest() throws Exception {
    Example saved = dao.create(new Example("foo", "bar"));
    String incompleteExample = "{\"name\" : \"foo\"}";

    String response = http.putJson(DEFAULT_HOST_URL + "/examples/" + saved.getId(), incompleteExample, 400);
    assertEquals("Example is not valid", response);
  }

  @Test
   public void putWithInvalidJsonReturnsBadRequest() throws Exception {
    Example saved = dao.create(new Example("foo", "bar"));
    String incompleteExample = "{\"id\":1,\"name\":\"foo\",\"type\":\"bar}";

    String response = http.putJson(DEFAULT_HOST_URL + "/examples/" + saved.getId(), incompleteExample, 400);
    assertEquals("Example is not valid", response);
  }

  // deleteOfMissingIdReturnsNotFound
  // deleteWithValidIdReturnsOK
  // deleteWithInvalidIdReturnsBadRequest
}
