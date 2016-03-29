package com.testedminds.template.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class VersionTest {
  @Test
  public void shouldGetProjectVersion() {
    assertEquals("test", Version.from(this.getClass()));
  }

  @Test
  public void shouldHandleDevCase() {
    assertEquals("development", Version.parseClassLocation("/tmp/foo/classes"));
  }

  @Test
  public void shouldHandleRunningInJar() {
    assertEquals("sparkler-1.0.0-standalone", Version.parseClassLocation("/tmp/foo/sparkler-1.0.0-standalone.jar"));
  }
}
