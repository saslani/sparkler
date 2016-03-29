package com.testedminds.template.handlers;

import com.testedminds.template.db.ExampleDao;
import com.testedminds.template.models.Example;
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
    Object requestId = req.params(":id");
    long id;
    try {
     id = Long.parseLong(requestId.toString());
    } catch (Exception ex) {
      String message = String.format("example id must be a numeric value: '%s'", requestId);
      logger.warn(message);
      res.status(400);
      return message;
    }

//    TODO: refactore / Get
    Example example = dao.get(id);
    if (example == null) {
      res.status(404);
      return String.format("Example with id %d does not exist.", id);
    }

    logger.info("deleting example with id: " + id);
    dao.delete(id);
    logger.info("example " + id + " deleted");
    res.status(200);
    return "example " + id + " deleted";
  }
}
