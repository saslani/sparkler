package com.testedminds.template.db;

import org.sql2o.Sql2o;
import org.sql2o.quirks.PostgresQuirks;

import java.util.Map;

public class Sql2oFactory {
  public static Sql2o create(Map<String, String> params) {
    String url = JdbcUrl.build(params);
    return new Sql2o(url, params.get("user"), params.get("password"), new PostgresQuirks());
  }
}
