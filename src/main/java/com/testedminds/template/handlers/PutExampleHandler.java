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
    String name = req.params(":name");
    String type = req.params(":type");
    logger.info("updating the name for example with id: " + id);
    Example expectedExample;

    try {
        expectedExample = new Example(id, name, type);
    } catch (Exception e) {
      logger.warn(e.getMessage());
      res.status(400);
      return "Example is not valid";
    }

    Example updatedExample = dao.update(expectedExample);
    res.status(200);
    res.type("application/json");
    return new Gson().toJson(updatedExample);
  }
}
