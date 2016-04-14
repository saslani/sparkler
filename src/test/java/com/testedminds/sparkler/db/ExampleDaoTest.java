package com.testedminds.sparkler.db;

import com.testedminds.sparkler.models.Example;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ExampleDaoTest extends DatabaseTestRunner {

  @Test
  public void shouldCreateExample() {
    Example notSaved = new Example("foo", "bar");
    Example saved = dao.create(notSaved);
    assertTrue(saved.getId() > 0);
    assertTrue(!saved.equals(notSaved));
  }

  @Test
  public void shouldGetExampleById() {
    Example saved = dao.create(new Example("foo", "bar"));
    Example read = dao.get(saved.getId());
    assertEquals(saved, read);
  }

  @Test
  public void shouldReturnNullForMissingEntry() {
    assertNull(dao.get(-1));
  }

  @Test
  public void shouldUpdateExample() {
    Example saved = dao.create(new Example("foo", "bar"));
    Example update = new Example(saved.getId(), "foo", "baz");
    Example updated = dao.update(update);
    assertEquals(updated, update);
    assertEquals(updated, dao.get(saved.getId()));
  }

  @Test
  public void shouldDeleteExample() {
    Example saved = dao.create(new Example("foo", "bar"));
    dao.delete(saved.getId());
    assertNull(dao.get(saved.getId()));
  }

  @Test
  public void shouldGetAllExamples() {
    dao.create(new Example("foo", "bar"));
    dao.create(new Example("baz", "quux"));
    List<Example> examples = dao.all();
    assertEquals(2, examples.size());
  }
}
