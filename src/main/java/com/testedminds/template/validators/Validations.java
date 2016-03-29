package com.testedminds.template.validators;

public class Validations {
  public static boolean empty(String val) {
    return (val == null || val.trim().isEmpty());
  }
}
