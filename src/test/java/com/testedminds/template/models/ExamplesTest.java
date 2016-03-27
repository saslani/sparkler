package com.testedminds.template.models;

import com.testedminds.template.exceptions.ExampleException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ExamplesTest {

  @Test(expected = ExampleException.class)
  public void exampleNameShouldNotBeNull(){
    try{
      new Example(null, "type");
    } catch (ExampleException e) {
        assertEquals("example name is a required field", e.getMessage());
      throw e;
    }
  }

  @Test(expected = ExampleException.class)
  public void exampleTypeShouldNotBeNull(){
    try{
      new Example("name", null);
    } catch(ExampleException e){
      assertEquals("example type is a required field", e.getMessage());
      throw e;
    }
  }
}
