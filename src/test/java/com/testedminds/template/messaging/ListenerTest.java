package com.testedminds.template.messaging;

import com.rabbitmq.client.*;
import com.testedminds.template.config.Environment;
import com.testedminds.template.handlers.HandlingResult;
import com.testedminds.template.handlers.LoggingHandler;
import com.testedminds.template.handlers.MessageHandler;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static com.testedminds.template.config.Environment.amqpUri;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ListenerTest {
  private static final String QUEUE_NAME = "fanout_queue";

  @Test
  public void shouldThrowExceptionIfHistoricConversationsAreNotDownloaded() {
    try {
      Listener listener = new Listener(new LoggingHandler(), amqpUri(), QUEUE_NAME, 1);
      listener.call();
      fail("Should have timed out");
    } catch (Exception e) {
      assertTrue(e.getMessage().contains("Could not download historic conversations. "));
    }
  }

  @Ignore
  @Test
  public void shouldHandleReceivingConversions() throws Exception {
    final AtomicReference<String> message = new AtomicReference<>();
    final CountDownLatch counter = new CountDownLatch(1);

    MessageHandler testHandler = (delivery, headers) -> {
      message.set("ack");
      counter.countDown();
      return HandlingResult.success();
    };

    ConnectionFactory factory = new ConnectionFactory();
    factory.setUri(amqpUri());
    Connection connection = factory.newConnection();
    final Channel channel = connection.createChannel();
    channel.queueDeclare(QUEUE_NAME, false, true, false, QueueProperties.defaultProps());

    final QueueingConsumer consumer = new QueueingConsumer(channel);
    channel.basicConsume(QUEUE_NAME, false, consumer);

    new Thread(() -> {
      try {
        QueueingConsumer.Delivery delivery = consumer.nextDelivery();
        String message1 = new String(delivery.getBody(), "UTF-8");
        assertEquals("send conversions", message1);
        AMQP.BasicProperties properties = delivery.getProperties();
        String corrId = properties.getCorrelationId();
        String replyTo = properties.getReplyTo();

        AMQP.BasicProperties props = new AMQP.BasicProperties()
                .builder()
                .correlationId(corrId)
                .build();

        channel.basicPublish("", replyTo, props, "1".getBytes());
//        String conversion = ConversionFactory.validTfaStart();
//        channel.basicPublish("", replyTo, props, conversion.getBytes());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }).start();

    new Listener(testHandler, amqpUri(), QUEUE_NAME).call();
    counter.await(500, MILLISECONDS);// 500 ms should be more than enough time
    assertEquals("ack", message.get());
  }

}
