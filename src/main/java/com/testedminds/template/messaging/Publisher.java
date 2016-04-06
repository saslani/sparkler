package com.testedminds.template.messaging;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;

public class Publisher {
  private final ConnectionFactory factory = new ConnectionFactory();
  private Channel channel;
  private Connection connection;
  private AMQP.BasicProperties basicProperties;
  private java.lang.String exchange;
  private java.lang.String queueType;

  public Publisher(String exchange, String queueType, String uriString) {
    try{
      factory.setUri(uriString);
      connection = factory.newConnection();
      channel = connection.createChannel();
      basicProperties = new AMQP.BasicProperties().builder().build();
      this.exchange = exchange;
      this.queueType = queueType;
    } catch (Exception e) {
      throw new RuntimeException("Could not create publisher:", e);
    }
  }

  public void purge() {
    try {
      channel.queuePurge(queueType);
    } catch (IOException e) {
      throw new RuntimeException("Could not purge queueType: ", e);
    }
  }

  public void publish(String message){
    try {
      //Assume that the queueType and routingKey are the same, as in other parts of the project
      channel.basicPublish(exchange, queueType, basicProperties, message.getBytes("UTF-8"));
    } catch (Exception e) {
      throw new RuntimeException("Could not publish the message: ", e);
    }
  }

  public void close(){
    try {
      channel.close();
      connection.close();
    } catch (Exception e) {
      throw new RuntimeException("Could not close connection: ", e);
    }
  }
}
