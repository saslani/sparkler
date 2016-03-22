package com.testedminds.template.handlers;

import com.testedminds.template.db.ExampleDao;
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
    Object id = req.params(":id");
    long exampleId = Long.parseLong(id.toString());
    logger.info("deleting example with id: " + id);
    dao.delete(exampleId);
    logger.info("example " + id + " deleted");
    res.status(200);
    return "example " + id + " deleted";
  }
}
