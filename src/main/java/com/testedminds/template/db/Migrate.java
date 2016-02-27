package com.testedminds.template.db;

import org.flywaydb.core.Flyway;

public class Migrate {

  public static void main(String[] args) {
    // TODO: Validate args
    String url = args[0];
    String user = args[1];
    run(url, user);
  }

  static void run(String url, String user) {
    Flyway flyway = new Flyway();
    flyway.setDataSource(url, user, null);
    flyway.migrate();
  }
}
