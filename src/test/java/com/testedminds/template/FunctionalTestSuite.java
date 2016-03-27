package com.testedminds.template;

import com.testedminds.template.db.DatabaseTestRunner;
import com.testedminds.template.db.ExampleDao;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import spark.Spark;

public class FunctionalTestSuite {
  protected static final String HOST = "localhost";
  protected static final int PORT = 4551;
  protected static final String DEFAULT_HOST_URL = String.format("http://%s:%d", HOST, PORT);
  private static CloseableHttpClient httpClient = HttpClientBuilder.create().build();
  protected static HttpTestUtils http = new HttpTestUtils(httpClient);

  // NB: Composing rather than inheriting here to make BeforeClass behavior
  // in this class behave as expected. JUnit would normally run the
  private static DatabaseTestRunner dbTest = new DatabaseTestRunner();

  protected static ExampleDao dao = dbTest.getExampleDao();

  @BeforeClass
  public static void setup() throws Exception {
    DatabaseTestRunner.migrateDatabase();
    new Routes(dao, PORT);
    Spark.awaitInitialization();
  }

  @AfterClass
  public static void stopServer() throws Exception {
    Spark.stop();
    httpClient.close();
  }

  @Before
  public void before() throws Exception {
    dbTest.clearDatabase();
  }
}
