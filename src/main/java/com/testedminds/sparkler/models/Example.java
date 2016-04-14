package com.testedminds.sparkler.models;

import com.testedminds.sparkler.validators.Validations;
import com.testedminds.sparkler.exceptions.ExampleException;

public class Example {
  private final long id;
  private final String name;
  private final String type;

  public Example(String name, String type) {
    this(0, name, type);
  }

  public Example(long id, String name, String type) {
    this.id = id;
    this.name = name;
    this.type = type;
    validate();
  }

  public void validate() {
    if (Validations.empty(name)) throw new ExampleException("name is a required field");
    if (Validations.empty(type)) throw new ExampleException("type is a required field");
  }

  public Example(Example e, long id) {
    this(id, e.name, e.type);
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }

  @Override
  public String toString() {
    return "Example{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", type='" + type + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Example example = (Example) o;

    if (id != example.id) return false;
    if (!name.equals(example.name)) return false;
    return type.equals(example.type);
  }

  @Override
  public int hashCode() {
    int result = (int) (id ^ (id >>> 32));
    result = 31 * result + name.hashCode();
    result = 31 * result + type.hashCode();
    return result;
  }
}
