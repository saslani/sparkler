package com.testedminds.template.handlers;

import com.google.gson.Gson;
import com.testedminds.template.db.ExampleDao;
import com.testedminds.template.models.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;

public class PostExampleHandler implements Route {

  final static Logger logger = LoggerFactory.getLogger(PostExampleHandler.class);
  private final ExampleDao dao;

  public PostExampleHandler(ExampleDao dao) {
    this.dao = dao;
  }

  @Override
  public Object handle(Request req, Response res) throws Exception {
    String body = req.body();
    logger.info("creating an example: " + body);
    Example example;
    try {
      example = new Gson().fromJson(body, Example.class);
    } catch (Exception e) {
      logger.warn(e.getMessage());
      res.status(400);
      return String.format("%s - Example is not valid", body);
    }

    Example saved = dao.create(example);
    logger.info("example created with id: " + saved.getId());
    res.status(201);
    return String.valueOf(saved.getId());
  }
}
