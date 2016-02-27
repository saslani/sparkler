package com.testedminds.template;

import com.google.gson.Gson;
import com.testedminds.template.db.ExampleDao;
import com.testedminds.template.internal.model.Example;
import com.testedminds.template.payloads.ExamplePayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Sql2o;

import static spark.Spark.*;

public class Service {

  final static Logger logger = LoggerFactory.getLogger(Service.class);
  static Sql2o db;
  static ExampleDao exampleDao;

  public static void main(String[] args) {
    int serverPort = Integer.parseInt(args[0]);
    String url = args[1];
    String user = args[2];

    db = new Sql2o(url, user, null);
    exampleDao = new ExampleDao(db);
    port(serverPort);
    get("/", (req, res) -> "Welcome to Java Spark with H2 SQL2O Template!");

    get("/example/:id", (req, res) -> {
      Object id = req.params(":id");
      long exampleId = Long.parseLong(id.toString());
      logger.info("retrieving an example with id: " + id);
      Example home = exampleDao.getExample(exampleId);
      res.status(200);
      res.type("application/json");
      return new Gson().toJson(home);
    });

    post("/example", (req, res) -> {
      String body = req.body();
      logger.info("creating an example: " + body);
      ExamplePayload examplePayload = new Gson().fromJson(body, ExamplePayload.class);
      long exampleId = exampleDao.createRoom(examplePayload.getType(), examplePayload.getName());
      logger.info("example created with id: " + exampleId);
      res.status(201);
      return String.valueOf(exampleId);
    });

    put("/home/:id/name/:name", (req, res) -> {
      Object id = req.params(":id");
      long exampleId = Long.parseLong(id.toString());

      String name = req.params(":name");

      logger.info("updating the name for example with id: " + id);
      Example home = exampleDao.putExample(exampleId, name);
      res.status(200);
      res.type("application/json");
      return new Gson().toJson(home);
    });

    delete("/home/:id", (req, res) -> {
      Object id = req.params(":id");
      long exampleId = Long.parseLong(id.toString());
      logger.info("deleting example with id: " + id);
      exampleDao.deleteHome(exampleId);
      logger.info("example " + id + " deleted");
      res.status(200);
      return "example " + id + " deleted";
    });
  }
}
