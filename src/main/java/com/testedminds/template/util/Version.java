package com.testedminds.template.util;

import java.io.File;

public class Version {

  public static String from(Class clazz) {
    return parseClassLocation(clazz.getProtectionDomain().getCodeSource().getLocation().getPath());
  }

  static String parseClassLocation(String codePath) {
    String maybeJar = new File(codePath).getName();
    if (maybeJar.contains("test-classes")) return "test";
    int dot = maybeJar.lastIndexOf(".");
    if (dot < 0) return "development";
    return maybeJar.substring(0, dot);
  }
}
