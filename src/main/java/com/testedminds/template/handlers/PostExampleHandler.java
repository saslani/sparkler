package com.testedminds.template.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.testedminds.template.db.ExampleDao;
import com.testedminds.template.exceptions.ExampleException;
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
      example.validate();
    } catch (ExampleException e) {
      res.status(400);
      String message = String.format("Invalid Example: %s", e.getMessage());
      logger.warn(message);
      return message;
    } catch (JsonSyntaxException e) {
      res.status(400);
      String message = String.format("Invalid JSON: %s", body);
      logger.warn(message);
      return message;
    }

    Example saved = dao.create(example);
    logger.info("example created with id: " + saved.getId());
    res.status(201);
    return String.valueOf(saved.getId());
  }
}
