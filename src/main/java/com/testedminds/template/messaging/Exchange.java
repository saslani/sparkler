package com.testedminds.template.messaging;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Exchange {
  public static void declare(String exchange, String type, String uri) {
    try {
      ConnectionFactory factory = new ConnectionFactory();
      factory.setUri(uri);
      Connection connection = factory.newConnection();
      Channel channel = connection.createChannel();
      channel.exchangeDeclare(exchange, type, true);
      channel.close();
      connection.close();
    } catch (Exception e) {
      throw new RuntimeException("Could not declare exchange:", e);
    }
  }
}
