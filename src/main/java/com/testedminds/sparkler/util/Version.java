package com.testedminds.sparkler.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class Version {

  private final static Logger logger = LoggerFactory.getLogger(Version.class);

  public static String get() {
    try {
      Properties prop = new Properties();
      prop.load(Version.class.getResourceAsStream("/build.properties"));
      String name = prop.getProperty("name");
      String version = prop.getProperty("version");
      return name + "-" + version;
    } catch (Exception ex) {
      logger.error("Could not load build.properties from project resources.");
      return "";
    }
  }
}
