package com.testedminds.template.db;

public class ExampleRow {
  private final String e_type;
  private final String e_name;

  public ExampleRow(String e_type, String e_name) {
    this.e_type = e_type;
    this.e_name = e_name;
  }

  public String getType() {
    return e_type;
  }

  public String getName() {
    return e_name;
  }

  @Override
  public String toString() {
    return "ExampleRow{" +
        "e_type='" + e_type + '\'' +
        ", e_name='" + e_name + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ExampleRow that = (ExampleRow) o;

    if (!e_type.equals(that.e_type)) return false;
    return e_name.equals(that.e_name);

  }

  @Override
  public int hashCode() {
    int result = e_type.hashCode();
    result = 31 * result + e_name.hashCode();
    return result;
  }
}
