package com.testedminds.template;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class HttpTestUtils {

  private final HttpClient client;

  public HttpTestUtils(HttpClient client) {
    this.client = client;
  }

  public String get(String url, int expectedStatusCode) {
    return responseBodyWithCode(expectedStatusCode, new HttpGet(url));
  }

  public String postJson(String host, String body, int expectedStatusCode) throws Exception {
    StringEntity input = new StringEntity(body);
    input.setContentType("application/json");
    HttpPost post = new HttpPost(host);
    post.setEntity(input);
    return responseBodyWithCode(expectedStatusCode, post);
  }

  public String putJson(String host, String body, int expectedStatusCode) throws Exception {
    StringEntity input = new StringEntity(body);
    input.setContentType("application/json");
    HttpPut put = new HttpPut(host);
    put.setEntity(input);
    return responseBodyWithCode(expectedStatusCode, put);
  }

  private String responseBodyWithCode(int expectedStatusCode, HttpRequestBase request) {
    String responseBody = "";
    try {
      HttpResponse response = client.execute(request);
      assertEquals(expectedStatusCode, response.getStatusLine().getStatusCode());
      responseBody = EntityUtils.toString(response.getEntity());
    } catch (Exception ex) {
      fail(ex.toString());
    } finally {
      request.releaseConnection();
    }
    return responseBody;
  }
}
