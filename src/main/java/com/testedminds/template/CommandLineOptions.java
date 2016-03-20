package com.testedminds.template;

import com.beust.jcommander.Parameter;

public class CommandLineOptions {

  @Parameter(names="--help", help=true, description="Print this help")
  public boolean help;

  @Parameter(names="--port")
  public int port = 8081;

  @Parameter(names="--url", required=true)
  public String url;

  @Parameter(names="--user", required=true)
  public String user;

}