package com.testedminds.template;

import com.testedminds.template.db.ExampleDao;
import org.sql2o.Sql2o;

public class Server {

  public static void main(String[] args) {
    int serverPort = Integer.parseInt(System.getenv("PORT"));
    String url = System.getenv("DB_URL");

    Sql2o db = new Sql2o(url, null, null);
    ExampleDao dao = new ExampleDao(db);
    new Routes(dao, serverPort);
  }
}
