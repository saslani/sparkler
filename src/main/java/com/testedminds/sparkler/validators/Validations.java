package com.testedminds.sparkler.validators;

public class Validations {
  public static boolean empty(String val) {
    return (val == null || val.trim().isEmpty());
  }
}
