package com.testedminds.template.internal.model;

public class Example {
  private final String name;
  private final String type;

  public Example(String name, String type) {
    this.name = name;
    this.type = type;
  }

  @Override
  public String toString() {
    return "Example{" +
        "name='" + name + '\'' +
        ", type='" + type + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Example example = (Example) o;

    if (!name.equals(example.name)) return false;
    return type.equals(example.type);

  }

  @Override
  public int hashCode() {
    int result = name.hashCode();
    result = 31 * result + type.hashCode();
    return result;
  }
}
