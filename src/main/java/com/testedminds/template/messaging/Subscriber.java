package com.testedminds.template.messaging;

import com.rabbitmq.client.*;
import com.testedminds.template.config.Environment;
import com.testedminds.template.handlers.HandlingResult;
import com.testedminds.template.handlers.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class Subscriber {
  private Logger logger = LoggerFactory.getLogger(Subscriber.class);
  private final boolean debugMessageHandling;
  private Channel channel;
  private QueueingConsumer consumer;
  private final MessageHandler handler;
  private final String amqpUrl;
  private final String exchange;
  private final String queue;
  private final String routingKey;
  private final ExecutorService executorService;

  public Subscriber(MessageHandler handler, String amqpUrl, String exchangeName, String exchangeType, String queue) {
    this.handler = handler;
    this.amqpUrl = amqpUrl;
    this.channel = channel(factory());
    this.exchange = exchangeName;
    this.queue = queue;
    this.routingKey = queue; //Assumes queue and routingKey are the same
    declareExchange(exchangeType);
    this.consumer = consumer(channel, queue(channel));
    bindQueueToExchange(channel);

    int nThreads = Runtime.getRuntime().availableProcessors();
    logger.info(String.format("Initializing subscriber with %d threads", nThreads));
    this.executorService = Executors.newFixedThreadPool(nThreads);
    this.debugMessageHandling = checkDebuggingFlag();
  }

  protected void log(HandlingResult result) {
    if (debugMessageHandling) logger.warn(result.print());
  }

  protected void setLogger(Logger logger) {
    this.logger = logger;
  }

  public void deliveryLoop() {
    logger.info("Waiting for messages...");
    while(true){
      final QueueingConsumer.Delivery delivery = delivery(consumer);
      if (delivery != null) {
        executorService.submit((Runnable) () -> {
          try {
            HandlingResult result = handler.handle(message(delivery), headers(delivery));
            log(result);
            acknowledge(channel, delivery);
          } catch (Exception ex) {
            reject(channel, delivery);
          }
        });
      }
    }
  }

  private void reject(Channel channel, QueueingConsumer.Delivery delivery) {
    try {
      long deliveryTag = delivery.getEnvelope().getDeliveryTag();
      channel.basicNack(deliveryTag, false, false);
      logger.warn(String.format("Rejected message: tag: %d body: %s ", deliveryTag, new String(delivery.getBody())));
    } catch (IOException e) {
      throw new RuntimeException("Failed to nack delivery:", e);
    }
  }

  private void acknowledge(Channel channel, QueueingConsumer.Delivery delivery) {
    try {
      channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
    } catch (IOException e) {
      throw new RuntimeException("Failed to acknowledge delivery:", e);
    }
  }

  private Map<String, Object> headers(QueueingConsumer.Delivery delivery) {
    AMQP.BasicProperties properties = delivery.getProperties();
    return properties.getHeaders();
  }

  private QueueingConsumer.Delivery delivery(QueueingConsumer consumer) {
    try {
      // handle shutdownsignalexception and attempt to reconnect.
      // let the subscriber die
      // kick off a new one and attempt to reconnect.
      return consumer.nextDelivery();
    } catch (InterruptedException e) {
      throw new RuntimeException("Consumer interrupted:", e);
    }
  }

  private String message(QueueingConsumer.Delivery delivery) {
    try {
      return new String(delivery.getBody(), "UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException("Failed to parse message:", e);
    }
  }


  private Channel channel(ConnectionFactory factory) {
    try {
      Connection connection = factory.newConnection();
      return connection.createChannel();
    } catch (Exception e) {
      throw new RuntimeException("Could not create channel: ", e);
    }
  }

  private ConnectionFactory factory() {
    try {
      ConnectionFactory factory = new ConnectionFactory();
      factory.setUri(amqpUrl);
      return factory;
    } catch (Exception e) {
      String message = String.format("Failed to initialize ConnectionFactory with %s. Is the %s environment variable set correctly? ", amqpUrl, Environment.AMQP_ENV_VAR);
      throw new RuntimeException(message, e);
    }
  }

  private QueueingConsumer consumer(Channel channel, String queueName) {
    try {
      QueueingConsumer consumer = new QueueingConsumer(channel);
      channel.basicConsume(queueName, false, consumer);
      return consumer;
    } catch (IOException e) {
      throw new RuntimeException("Could not create consumer: ", e);
    }
  }

  private boolean checkDebuggingFlag() {
    return System.getenv("DEBUG_MESSAGE_HANDLING").equals("true");
  }

  private void bindQueueToExchange(Channel channel) {
    try {
      channel.queueBind(queue, exchange, routingKey);
    } catch (IOException e) {
      throw new RuntimeException("Could not bind queue to exchange: ", e);
    }
  }

  private String queue(Channel channel) {
    try {
      boolean durable = true;
      channel.queueDeclare(queue, durable, false, false, QueueProperties.defaultProps());
    } catch (IOException e) {
      throw new RuntimeException("Could not declare exchange: ", e);
    }
    return queue;
  }

  private void declareExchange(String type) {
    try {
      channel.exchangeDeclare(exchange, type, true);
    } catch (Exception e) {
      throw new RuntimeException("Could not declare exchange: ", e);
    }
  }
}
