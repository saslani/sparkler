package com.testedminds.sparkler;

import com.testedminds.sparkler.db.DatabaseUrl;
import com.testedminds.sparkler.db.ExampleDao;
import com.testedminds.sparkler.db.Sql2oFactory;
import org.sql2o.Sql2o;

import java.util.Map;

public class Server {

  public static void main(String[] args) throws Exception {
    int serverPort = Integer.parseInt(System.getenv("PORT"));
    String url = System.getenv("JDBC_DATABASE_URL");

    Map<String, String> params = DatabaseUrl.params(url);
    Sql2o db = Sql2oFactory.create(params);
    ExampleDao dao = new ExampleDao(db);
    new Routes(dao, serverPort);
  }
}
