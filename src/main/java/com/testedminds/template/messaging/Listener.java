package com.testedminds.template.messaging;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.testedminds.template.handlers.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Listener {
  private final ConnectionFactory factory = new ConnectionFactory();
  private static final Logger logger = LoggerFactory.getLogger(Listener.class);
  private final Channel channel;
  private final MessageHandler handler;
  private final String requestQueueName;
  private final int timeout;
  private final String replyQueueName;
  private final QueueingConsumer consumer;
  private Connection connection;

  public Listener(MessageHandler handler, String uriString, String queueName) throws Exception {
    this(handler, uriString, queueName, 100000);
  }

  public Listener(MessageHandler handler, String uriString, String queueName, int timeoutMillis) throws Exception {
    factory.setUri(uriString);
    connection = factory.newConnection();
    channel = connection.createChannel();
    this.handler = handler;
    this.requestQueueName = queueName;
    this.timeout = timeoutMillis;

    replyQueueName = channel.queueDeclare().getQueue();
    consumer = new QueueingConsumer(channel);
    channel.basicConsume(replyQueueName, true, consumer);
  }

  private String getMessage(String corrId) throws InterruptedException {
    while(true){
      QueueingConsumer.Delivery delivery = consumer.nextDelivery(timeout);
      if(delivery == null) throw new RuntimeException(String.format("Server did not respond in %d seconds.", timeout/1000));
      if(delivery.getProperties().getCorrelationId().equals(corrId)) return new String(delivery.getBody());
    }
  }

  public void close() throws Exception {
    connection.close();
  }
}
