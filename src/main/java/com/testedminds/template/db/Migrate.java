package com.testedminds.template.db;

import org.flywaydb.core.Flyway;

public class Migrate {

  public Migrate(String url) {
    Flyway flyway = new Flyway();
    flyway.setDataSource(url, null, null);
    flyway.migrate();
  }

  public static void main(String[] args) {
    String url = System.getenv("DB_URL");
    new Migrate(url);
  }
}
