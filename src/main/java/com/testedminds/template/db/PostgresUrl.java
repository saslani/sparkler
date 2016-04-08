package com.testedminds.template.db;

import java.util.Map;

public class PostgresUrl {
  public static String build(Map<String, String> params) {
    return "jdbc:postgresql://" + params.get("host") + ":" + params.get("port") + params.get("database") + "?sslmode=require";
  }
}
