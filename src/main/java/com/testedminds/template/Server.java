package com.testedminds.template;

import com.beust.jcommander.JCommander;
import com.testedminds.template.db.ExampleDao;
import org.sql2o.Sql2o;

public class Server {

  public static void main(String[] args) {
    CommandLineOptions opts = new CommandLineOptions();
    JCommander jc = new JCommander(opts, args);
    jc.setProgramName(Server.class.getName());

    if (opts.help) {
      jc.usage();
      System.exit(0);
    }

    int serverPort = opts.port;
    String url = opts.url;
    String user = opts.user;

    Sql2o db = new Sql2o(url, user, null);
    ExampleDao dao = new ExampleDao(db);
    new Routes(dao, serverPort);
  }
}
