package com.testedminds.template;

import com.testedminds.template.db.ExampleDao;
import com.testedminds.template.handlers.DeleteExampleHandler;
import com.testedminds.template.handlers.GetExampleHandler;
import com.testedminds.template.handlers.PostExampleHandler;
import com.testedminds.template.handlers.PutExampleHandler;
import com.testedminds.template.util.Version;

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