package com.testedminds.template.db;

import org.flywaydb.core.Flyway;

public class Migrate {

  public static void main(String[] args) {
    String url = args[0];
    String user = args[1];
//    String password = arg[2] // if you have a password for your database
    run(url, user);
  }

  static void run(String url, String user) {
    Flyway flyway = new Flyway();
    flyway.setDataSource(url, user, null); //the third parameter is the password
    flyway.migrate();
  }
}
