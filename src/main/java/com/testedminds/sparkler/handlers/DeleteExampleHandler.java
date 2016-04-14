package com.testedminds.sparkler.handlers;

import com.testedminds.sparkler.db.ExampleDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;

public class DeleteExampleHandler implements Route {
  private final static Logger logger = LoggerFactory.getLogger(GetExampleHandler.class);
  private final ExampleDao dao;

  public DeleteExampleHandler(ExampleDao dao) {
    this.dao = dao;
  }

  @Override
  public Object handle(Request req, Response res) throws Exception {
    // Check that we can GET the Example before we change it.
    Object result = new GetExampleHandler(dao).handle(req, res);
    if (res.raw().getStatus() != 200) return result;

    // Example with this id exists, so now attempt the delete.
    long id = Long.parseLong(req.params(":id"));
    logger.info("deleting example with id: " + id);
    dao.delete(id);

    res.status(200);
    res.type("text/plain");
    return "example " + id + " deleted";
  }
}
