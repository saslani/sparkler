package com.testedminds.template.handlers;

public class HandlingResult {

  public static final String SUCCESSFUL = "successful";
  private String type;
  private String message;

  public HandlingResult(String type, String message) {
    this.type = type;
    this.message = message;
  }

  public String print() {
    return isSuccessful() ? SUCCESSFUL : String.format("%s: %s", type, message);
  }

  public boolean isSuccessful() {
    return type.equals(SUCCESSFUL);
  }

  @Override
  public String toString() {
    return type;
  }

  public static HandlingResult failure(String result, String message) {
    return new HandlingResult(result, message);
  }

  public static HandlingResult success() {
    return new HandlingResult(SUCCESSFUL, "");
  }
}
