package com.testedminds.template.db;

import com.beust.jcommander.JCommander;
import com.testedminds.template.CommandLineOptions;
import org.flywaydb.core.Flyway;

public class Migrate {

  public static void main(String[] args) {
    CommandLineOptions opts = new CommandLineOptions();
    new JCommander(opts, args);
    new Migrate(opts.url, opts.user);
  }

  public Migrate(String url, String user) {
    Flyway flyway = new Flyway();
    flyway.setDataSource(url, user, null); // TODO: Handle password for a prod-like db.
    flyway.migrate();
  }
}
