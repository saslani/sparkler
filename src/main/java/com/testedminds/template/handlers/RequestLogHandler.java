package com.testedminds.template.handlers;

import com.google.gson.Gson;
import com.testedminds.template.messaging.PersistentFanoutPublisher;
import com.testedminds.template.messaging.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Filter;
import spark.Request;
import spark.Response;

public class RequestLogHandler implements Filter {
  private final static Logger logger = LoggerFactory.getLogger(RequestLogHandler.class);
  private final Publisher publisher;

  public RequestLogHandler(String exchangeName, String rabbitmqUrl) {
    this.publisher = new PersistentFanoutPublisher(exchangeName, rabbitmqUrl);
  }

  private String print(Request req) {
    RequestMetadata metadata = new RequestMetadata();
    metadata.ip = req.ip();
    metadata.forwardedFor = req.headers("X-Forwarded-For");
    metadata.userAgent = req.userAgent();
    return metadata.toJson();
  }

  @Override
  public void handle(Request request, Response response) throws Exception {
    String metadata = print(request);
    publisher.publish(metadata);
//    publisher.close();
    logger.debug("sent " + metadata);
  }

  private class RequestMetadata {
    String ip;
    String forwardedFor;
    String userAgent;

    String toJson() {
      return new Gson().toJson(this);
    }
  }
}
