package com.testedminds.template;

import com.testedminds.template.db.DatabaseUrl;
import com.testedminds.template.db.ExampleDao;
import com.testedminds.template.db.Sql2oFactory;
import com.testedminds.template.messaging.Exchange;
import org.sql2o.Sql2o;

import java.util.Map;

public class Server {
  private static final String EXCHANGE = "sparkler.example";

  public static void main(String[] args) throws Exception {
    String rabbitmqUrl = System.getenv("RABBITMQ_URL");
    Exchange.declare(EXCHANGE, "fanout", rabbitmqUrl);

    String url = System.getenv("JDBC_DATABASE_URL");
    Map<String, String> params = DatabaseUrl.params(url);
    Sql2o db = Sql2oFactory.create(params);
    ExampleDao dao = new ExampleDao(db);

    int serverPort = Integer.parseInt(System.getenv("PORT"));
    new Routes(dao, serverPort, EXCHANGE, rabbitmqUrl);
  }
}
