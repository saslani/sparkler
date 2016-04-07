package com.testedminds.template.messaging;

import com.rabbitmq.client.*;
import com.testedminds.template.handlers.HandlingResults;
import com.testedminds.template.handlers.MessageHandler;
import de.svenjacobs.loremipsum.LoremIpsum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
    while (true) {
      QueueingConsumer.Delivery delivery = consumer.nextDelivery(timeout);
      if (delivery == null)
        throw new RuntimeException(String.format("Server did not respond in %d seconds.", timeout / 1000));
      if (delivery.getProperties().getCorrelationId().equals(corrId)) return new String(delivery.getBody());
    }
  }

  public void close() throws Exception {
    connection.close();
  }

//  TODO: not sure how the call works
//  TODO: this cold be useful
  public void call() {
    String message = new LoremIpsum().getWords(15);;
    String corrId = java.util.UUID.randomUUID().toString();

    try {
      AMQP.BasicProperties properties = new AMQP.BasicProperties()
              .builder()
              .priority(2)
              .correlationId(corrId)
              .replyTo(replyQueueName)
              .build();

      channel.basicPublish("", requestQueueName, properties, message.getBytes());

      // get a count of messages as the first message, then just wait for the rest
      int count = Integer.parseInt(getMessage(corrId));
      System.out.printf("Receiving %d messages\n", count);
      Map<String, Object> headers = new HashMap<>();
      HandlingResults results = new HandlingResults();
      for (int i=0; i<count; i++) {
        results.add(handler.handle(getMessage(corrId), headers));
        if (i % 1000 == 0) update(i, count);
      }
      System.out.println(results.printSummary());

    } catch (Exception e) {
      throw new RuntimeException("Could not download historic conversations. ", e);
    }
  }

  private void update(int processed, int count) {
    System.out.printf("Processed %d out of %d requests\n", processed, count);
  }
}
