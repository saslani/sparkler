package com.testedminds.template.handlers;

import com.google.gson.Gson;
import com.testedminds.template.db.DatabaseTestRunner;
import com.testedminds.template.db.ExampleDao;
import com.testedminds.template.models.Example;
import org.junit.Test;
import spark.Request;
import spark.RequestResponseFactory;
import spark.Response;

import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetExampleHandlerTest extends DatabaseTestRunner {

  @Test
  public void shouldReturnJsonForFoundExample() throws Exception {
    Example created = dao.create(new Example("foo", "bar"));
    Request request = mock(Request.class);
    when(request.params(":id")).thenReturn(String.valueOf(created.getId()));

    HttpServletResponse servletResponse = mock(HttpServletResponse.class);
    Response response = RequestResponseFactory.create(servletResponse);

    GetExampleHandler handler = new GetExampleHandler(dao);
    Object handled = handler.handle(request, response);
    Example found = new Gson().fromJson(handled.toString(), Example.class);
    assertEquals(created, found);
  }
}
