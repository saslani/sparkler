package com.testedminds.template.db;

import org.flywaydb.core.Flyway;

import java.util.Map;

public class Migrate {

  public Migrate(String url) throws Exception {
    Map<String, String> params = DatabaseUrl.params(url);
    String dataSourceUrl = JdbcUrl.build(params);
    Flyway flyway = new Flyway();
    flyway.setDataSource(dataSourceUrl, params.get("user"), params.get("password"));
    flyway.migrate();
  }

  public static void main(String[] args) throws Exception {
    String url = System.getenv("JDBC_DATABASE_URL");
    new Migrate(url);
  }
}
