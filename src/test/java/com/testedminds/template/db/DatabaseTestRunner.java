package com.testedminds.template.db;


import org.junit.Before;
import org.junit.BeforeClass;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.nio.file.Files;
import java.nio.file.Paths;

public class DatabaseTestRunner {
  private static String testDbPath = "/tmp/testedminds-test-db";
  private static String testDbUrl = "jdbc:h2:file:" + testDbPath;
  private static String testDbUser = "sa";

  protected Sql2o db = new Sql2o(testDbUrl, testDbUser, null);
  protected ExampleDao dao = new ExampleDao(db);

  @BeforeClass
  public static void setup() throws Exception {
    Files.deleteIfExists(Paths.get(testDbPath + ".mv.db"));
    new Migrate(testDbUrl, testDbUser);
  }

  @Before
  public void clearDatabase() throws Exception {
    try (Connection conn = db.beginTransaction()) {
      conn.createQuery("truncate table examples").executeUpdate();
      conn.commit();
    }
  }
}
