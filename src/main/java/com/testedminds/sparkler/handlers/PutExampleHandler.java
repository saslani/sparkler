package com.testedminds.sparkler.handlers;

import com.google.gson.Gson;
import com.testedminds.sparkler.models.Example;
import com.testedminds.sparkler.db.ExampleDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;

public class PutExampleHandler implements Route {
  private final static Logger logger = LoggerFactory.getLogger(PutExampleHandler.class);
  private final ExampleDao dao;

  public PutExampleHandler(ExampleDao dao) {
    this.dao = dao;
  }

  @Override
  public Object handle(Request req, Response res) throws Exception {
    // Check that we can GET the Example before we change it.
    Object result = new GetExampleHandler(dao).handle(req, res);
    if (res.raw().getStatus() != 200) return result;

    // Example with this id exists, so now attempt the update.
    long id = Long.parseLong(req.params(":id"));
    Example update;
    try {
      Example fromJson = new Gson().fromJson(req.body(), Example.class);
      fromJson.validate();
      update = new Example(fromJson, id);
    } catch (Exception e) {
      logger.warn(e.getMessage());
      res.status(400);
      return "Example is not valid";
    }

    logger.info("updating example with id: " + id);
    Example updatedExample = dao.update(update);
    res.status(200);
    res.type("application/json");
    return new Gson().toJson(updatedExample);
  }
}
