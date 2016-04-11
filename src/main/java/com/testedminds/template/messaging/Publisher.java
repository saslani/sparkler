package com.testedminds.template.messaging;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public abstract class Publisher {

  protected final String exchangeName;
  protected Channel channel;
  private Connection connection;

  public Publisher(String exchangeName, String uriString) {
    try {
      ConnectionFactory factory = new ConnectionFactory();
      factory.setUri(uriString);
      this.connection = factory.newConnection();
      this.channel = connection.createChannel();
      this.exchangeName = exchangeName;
    } catch (Exception e) {
      throw new RuntimeException("Could not create publisher:", e);
    }
  }

  public abstract void publish(String message);

  public void close() {
    try {
      channel.close();
      connection.close();
    } catch (Exception e) {
      throw new RuntimeException("Could not close connection: ", e);
    }
  }
}