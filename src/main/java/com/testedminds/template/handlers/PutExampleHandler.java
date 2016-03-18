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
  private final ExampleDao dao;
  private final static Logger logger = LoggerFactory.getLogger(PutExampleHandler.class);

  public PutExampleHandler(ExampleDao dao) {
    this.dao = dao;
  }

  @Override
  public Object handle(Request req, Response res) throws Exception {
      Object id = req.params(":id");
      long exampleId = Long.parseLong(id.toString());
      String name = req.params(":name");
      logger.info("updating the name for example with id: " + id);
      Example home = dao.putExample(exampleId, name);
      res.status(200);
      res.type("application/json");
      return new Gson().toJson(home);
  }
}
