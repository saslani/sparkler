package com.testedminds.sparkler.db;

import java.util.Map;

public class H2Url {
  public static String build(Map<String, String> params) {
    String database = params.get("database").substring(1);
    return "jdbc:h2:" + params.get("host") + ":" + database;
  }
}
