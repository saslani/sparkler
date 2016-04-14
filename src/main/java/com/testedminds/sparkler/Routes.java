package com.testedminds.sparkler;

import com.testedminds.sparkler.db.ExampleDao;
import com.testedminds.sparkler.handlers.DeleteExampleHandler;
import com.testedminds.sparkler.handlers.GetExampleHandler;
import com.testedminds.sparkler.handlers.PostExampleHandler;
import com.testedminds.sparkler.handlers.PutExampleHandler;
import com.testedminds.sparkler.util.Version;

import static spark.Spark.*;

public class Routes {

  public Routes(ExampleDao dao, int serverPort) {
    String version = Version.get();

    port(serverPort);

    get("/", (req, res) ->
        "It's time to sparkle and shine! See the README to get started. Version: " + version);

    get("/examples/:id", new GetExampleHandler(dao));
    post("/examples", new PostExampleHandler(dao));
    put("/examples/:id", new PutExampleHandler(dao));
    delete("/examples/:id", new DeleteExampleHandler(dao));
  }
}