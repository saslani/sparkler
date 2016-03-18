package com.testedminds.template;

import com.testedminds.template.db.ExampleDao;
import com.testedminds.template.handlers.DeleteExampleHandler;
import com.testedminds.template.handlers.GetExampleHandler;
import com.testedminds.template.handlers.PostExampleHandler;
import com.testedminds.template.handlers.PutExampleHandler;
import org.sql2o.Sql2o;

import static spark.Spark.*;

// TODO: Dependency injection for the db
// TODO: Check params for url and user
public class Service {
  static Sql2o db;
  static ExampleDao exampleDao;

  public static void main(String[] args) {
    int serverPort = Integer.parseInt(args[0]);
    String url = args[1];
    String user = args[2];

    db = new Sql2o(url, user, null);
    exampleDao = new ExampleDao(db);

    port(serverPort);

    get("/", (req, res) -> "It's time to sparkle and shine! See the README to get started.");

    get("/example/:id", new GetExampleHandler(exampleDao));
    post("/example", new PostExampleHandler(exampleDao));
    put("/example/:id", new PutExampleHandler(exampleDao));
    delete("/example/:id", new DeleteExampleHandler(exampleDao));
  }
}
