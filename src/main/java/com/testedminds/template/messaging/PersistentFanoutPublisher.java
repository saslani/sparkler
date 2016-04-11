package com.testedminds.template.messaging;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.BasicProperties;

public class PersistentFanoutPublisher extends Publisher {
  private BasicProperties basicProperties;

  public PersistentFanoutPublisher(String exchangeName, String uriString) {
    super(exchangeName, uriString);
    // delivery mode 2 is persistent
    this.basicProperties = new AMQP.BasicProperties().builder().deliveryMode(2).build();
  }

  @Override
  public void publish(String message) {
    try {
      // The second parameter is a routingKey, which we'll leave blank for a Fanout.
      channel.basicPublish(exchangeName, "", basicProperties, message.getBytes("UTF-8"));
    } catch (Exception e) {
      throw new RuntimeException("Could not publish the message: ", e);
    }
  }
}