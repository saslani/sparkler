package com.testedminds.template.config;

public class Environment {
  public static final String AMQP_ENV_VAR = "CLOUDAMQP_URL";
  public static final String AMQP_LOCALHOST = "amqp://guest:guest@localhost";

  public static String amqpUri() {
    String env = System.getenv(AMQP_ENV_VAR);
    return env == null ? AMQP_LOCALHOST : env;
  }
}
