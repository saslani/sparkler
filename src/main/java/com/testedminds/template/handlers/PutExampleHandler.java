package com.testedminds.template.handlers;

import com.google.gson.Gson;
import com.testedminds.template.db.ExampleDao;
import com.testedminds.template.models.Example;
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
    Object requestId = req.params(":id");
    long id = Long.parseLong(requestId.toString());

//    TODO: refactore / Get
    Example example = dao.get(id);
    if (example == null) {
      res.status(404);
      return String.format("Example with id %d does not exist.", id);
    }

    Example update;
    try {
      update = new Gson().fromJson(req.body(), Example.class);
      update.validate();
      logger.info("updating the name for example with id: " + id);
    } catch (Exception e) {
      logger.warn(e.getMessage());
      res.status(400);
      return "Example is not valid";
    }

    Example updatedExample = dao.update(update);
    if (updatedExample == null) {
      res.status(404);
      return String.format("Example with id %d does not exist.", id);
    }
    res.status(200);
    res.type("application/json");
    return new Gson().toJson(updatedExample);
  }
}
