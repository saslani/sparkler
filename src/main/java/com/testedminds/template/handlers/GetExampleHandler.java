package com.testedminds.template.handlers;

import com.google.gson.Gson;
import com.testedminds.template.db.ExampleDao;
import com.testedminds.template.models.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;

public class GetExampleHandler implements Route {

  private final static Logger logger = LoggerFactory.getLogger(GetExampleHandler.class);
  private final ExampleDao dao;

  public GetExampleHandler(ExampleDao dao) {
    this.dao = dao;
  }

  @Override
  public Object handle(Request req, Response res) throws Exception {
    long id;
    Object requestId = req.params(":id");

    try {
      id = Long.parseLong(requestId.toString());
      logger.info(String.format("retrieving an example with id: %d", id));
    } catch (Exception ex) {
      String message = String.format("example id must be a numeric value: '%s'", requestId);
      logger.warn(message);
      res.status(400);
      return message;
    }

    Example example = dao.get(id);
    if (example == null) {
      res.status(404);
      return String.format("Example with id %d does not exist.", id);
    }
    res.status(200);
    res.type("application/json");
    return new Gson().toJson(example);
  }
}
