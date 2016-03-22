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
    Object id = req.params(":id");
    long exampleId = Long.parseLong(id.toString());
    logger.info("retrieving an example with id: " + id);
    Example example = dao.get(exampleId);
    res.status(200);
    res.type("application/json");
    return new Gson().toJson(example);
  }
}
