package com.testedminds.sparkler.db;

import com.testedminds.sparkler.models.Example;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.List;

public class ExampleDao {

  private final Sql2o db;

  public ExampleDao(final Sql2o db) {
    this.db = db;
  }

  public Example create(Example e) {
    long id;
    try (Connection conn = db.open()) {
      id = (long) conn.createQuery("insert into examples (e_type, e_name) values (:e_type, :e_name)", true)
          .addParameter("e_type", e.getType())
          .addParameter("e_name", e.getName())
          .executeUpdate()
          .getKey();
    }
    return new Example(e, id);
  }

  public Example get(long id) {
    try (Connection conn = db.open()) {
      List<Example> result = conn.createQuery("select * from examples where id=:id")
          .addParameter("id", id)
          .addColumnMapping("e_type", "type")
          .addColumnMapping("e_name", "name")
          .executeAndFetch(Example.class);

      return result.isEmpty() ? null : result.get(0);
    }
  }

  public Example update(Example e) {
    try (Connection conn = db.open()) {
      conn.createQuery("update examples set e_name=:e_name, e_type=:e_type where id=:id")
          .addParameter("e_name", e.getName())
          .addParameter("e_type", e.getType())
          .addParameter("id", e.getId())
          .executeUpdate();
      return get(e.getId());
    }
  }

  public void delete(long id) {
    try (Connection conn = db.open()) {
      conn.createQuery("delete from examples where id=:id")
          .addParameter("id", id)
          .executeUpdate();
    }
  }

  public List<Example> all() {
    try (Connection conn = db.open()) {
      return conn.createQuery("select * from examples")
          .addColumnMapping("e_type", "type")
          .addColumnMapping("e_name", "name")
          .executeAndFetch(Example.class);
    }
  }
}
