package com.testedminds.template.payloads;


/**
 * This class wil lbe useful if you have a combination of objects, models, inside the json;
 * otherwise you may use the XPayloadToModel directly from the service class
 * */
public class ExamplePayload {
  private final String type;
  private final String name;

  public ExamplePayload(final String type, final String name) {
    this.type = type;
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public String getName() {
    return name;
  }
}
