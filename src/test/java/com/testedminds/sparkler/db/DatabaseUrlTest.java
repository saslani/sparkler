package com.testedminds.sparkler.db;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DatabaseUrlTest {
  @Test
  public void shouldParsePostgresJdbcUrl() throws Exception {
    String url = "jdbc:postgresql://server.domain.com:5432/dbname?user=theuser&password=thepass&sslmode=require";
    Map<String, String> params = DatabaseUrl.params(url);
    assertEquals("postgresql", params.get("type"));
    assertEquals("server.domain.com", params.get("host"));
    assertEquals("5432", params.get("port"));
    assertEquals("/dbname", params.get("database"));
    assertEquals("theuser", params.get("user"));
    assertEquals("thepass", params.get("password"));
    assertEquals("require", params.get("sslmode"));
  }

  @Test
  public void shouldParseRelativeH2Url() throws Exception {
    String url = "jdbc:h2://file/./db/sparkler";
    Map<String, String> params = DatabaseUrl.params(url);
    assertEquals("h2", params.get("type"));
    assertEquals("file", params.get("host"));
    assertEquals("/./db/sparkler", params.get("database"));
  }

  @Test
  public void shouldParseAbsoluteH2Url() throws Exception {
    String url = "jdbc:h2://file//tmp/db/sparkler";
    Map<String, String> params = DatabaseUrl.params(url);
    assertEquals("h2", params.get("type"));
    assertEquals("file", params.get("host"));
    assertEquals("//tmp/db/sparkler", params.get("database"));
  }


  @Test
  public void shouldHandleEmptyUrlWithEmptyParams() throws Exception {
    Map<String, String> params = DatabaseUrl.params("");
    assertTrue(params.isEmpty());
  }

  @Test
  public void shouldHandleMalformedUrl() throws Exception {
    String url = "h2://file/./db/sparkler";
    Map<String, String> params = DatabaseUrl.params(url);
    assertTrue(params.isEmpty());
  }
}
