package com.testedminds.sparkler.models;

import com.testedminds.sparkler.exceptions.ExampleException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ExamplesTest {

  @Test
  public void exampleNameShouldNotBeNull(){
    try{
      new Example(null, "type");
      fail("Expected ExampleException");
    } catch (ExampleException e) {
      assertEquals("name is a required field", e.getMessage());
    }
  }

  @Test
  public void exampleTypeShouldNotBeNull(){
    try{
      new Example("name", null);
      fail("Expected ExampleException");
    } catch(ExampleException e){
      assertEquals("type is a required field", e.getMessage());
    }
  }
}
