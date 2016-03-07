package com.testedminds.template.db;

import com.testedminds.template.models.Example;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

public class ExampleDao {

  private final Sql2o db;

  public ExampleDao(final Sql2o db) {
    this.db = db;
  }

//  Notice the underscores in the variable names
//  When to use db.open
//  When to use db.beginTransaction

  public long createRoom(String e_type, String e_name) {
    long exampleId;
    try (Connection conn = db.beginTransaction()) {
      exampleId = (long) conn.createQuery("insert into examples (e_type, e_name) values (:e_type, :e_name)")
          .addParameter("e_type", e_type)
          .addParameter("e_name", e_name)
          .executeUpdate()
          .getKey();
      conn.commit();
    }
    return exampleId;
  }

  public Example getExample(long id) {
    try (Connection conn = db.open()) {
      ExampleRow exampleRow = conn.createQuery("select * from homes where id=:id")
          .addParameter("id", id)
          .executeAndFetch(ExampleRow.class)
          .get(0);

      return new Example(exampleRow.getName(), exampleRow.getType());
    }
  }

  public Example putExample(long id, String e_name) {
    try (Connection conn = db.open()) {
      conn.createQuery("update examples set e_name=:e_name where id=:id")
          .addParameter("e_name", e_name)
          .addParameter("id", id)
          .executeUpdate();
      return getExample(id);
    }
  }

  public void deleteHome(long id) {
    try (Connection conn = db.beginTransaction()){
      conn.createQuery("delete from examples where id=:id")
          .addParameter("id", id)
          .executeUpdate();
      conn.commit();
    }
  }

}
