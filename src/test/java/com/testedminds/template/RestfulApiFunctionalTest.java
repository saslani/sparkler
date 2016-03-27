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
  public void getJsonForExistingIdReturnsOK() {
    Example seed = new Example("foo", "bar");
    Example saved = dao.create(seed);

    String response = http.get(DEFAULT_HOST_URL + "/examples/" + saved.getId(), 200);
    Example fromResponse = new Gson().fromJson(response, Example.class);
    assertEquals(saved, fromResponse);
  }

  @Test
  public void getForMissingIdReturnsNotFound() {
    String response = http.get(DEFAULT_HOST_URL + "/examples/42", 404);
    assertTrue(response.contains("42"));
  }

  @Test
  public void getForInvalidIdReturnsBadRequest() {
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
    String response = http.postJson(DEFAULT_HOST_URL + "/examples", "invalid", 400);
    assertEquals("invalid - Example is not valid", response);
  }

  @Test
  public void putWithValidJsonAndIdReturnsUpdatedExample() throws Exception {
    Example saved = dao.create(new Example("foo", "bar"));
    String response = http.putJson(DEFAULT_HOST_URL + "/examples/"+saved.getId(), "junk", 400);
    assertEquals("Example is not valid", response);
  }

  // putWithMissingIdReturnsNotFound
  // putWithInvalidIdReturnsBadRequest
  // putWithInvalidJsonReturnsBadRequest

  // deleteOfMissingIdReturnsNotFound
  // deleteWithValidIdReturnsOK
  // deleteWithInvalidIdReturnsBadRequest
}
