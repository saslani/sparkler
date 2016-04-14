package com.testedminds.sparkler.db;

import java.util.Map;

public class JdbcUrl {
  public static String build(Map<String, String> params) {
    String type = params.getOrDefault("type", "unknown");

    if (type.equals("postgresql")) return PostgresUrl.build(params);
    if (type.equals("h2")) return H2Url.build(params);

    throw new RuntimeException("Unsupported database type: Must be h2 or postgresql, but was " + type);
  }
}
