package com.testedminds.template.payloads;

import com.testedminds.template.model.Example;

/**
 * If you have several objects, models, coming in the json the ExamplePayload will be responsible to separate them and
 * hold a reference to each object, then call the appropriate XToModel on each model. If you only have 1 model then you
 * may use this class directly from the service class
 * */
public class ExamplePayloadToModel {
  public static Example convert(ExamplePayload examplePayload) {
    return null;
  }
}
