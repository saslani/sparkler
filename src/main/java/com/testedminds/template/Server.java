package com.testedminds.template;

import com.testedminds.template.db.ExampleDao;
import org.sql2o.Sql2o;

// TODO: Check params for url and user
public class Server {

  public static void main(String[] args) {
    int serverPort = Integer.parseInt(args[0]);
    String url = args[1];
    String user = args[2];

    Sql2o db = new Sql2o(url, user, null);
    ExampleDao dao = new ExampleDao(db);
    new Routes(dao, serverPort);
  }
}
