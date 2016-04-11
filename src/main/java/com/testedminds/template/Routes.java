package com.testedminds.template;

import com.testedminds.template.db.ExampleDao;
import com.testedminds.template.handlers.*;
import com.testedminds.template.messaging.Publisher;
import com.testedminds.template.util.Version;

import static spark.Spark.*;

public class Routes {

  public Routes(ExampleDao dao, int serverPort, Publisher publisher) {
    port(serverPort);

    get("/", (req, res) ->
        "It's time to sparkle and shine! See the README to get started. Version: " +
            Version.from(this.getClass()));

    get("/examples/:id", new GetExampleHandler(dao));
    post("/examples", new PostExampleHandler(dao));
    put("/examples/:id", new PutExampleHandler(dao));
    delete("/examples/:id", new DeleteExampleHandler(dao));
    after(new RequestLogHandler(publisher));
  }
}