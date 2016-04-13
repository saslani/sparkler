package com.testedminds.template.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class VersionTest {
  @Test
  public void shouldGetProjectVersion() {
    assertTrue(Version.get().contains("sparkler"));
  }
}
