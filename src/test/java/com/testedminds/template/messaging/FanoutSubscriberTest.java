package com.testedminds.template.messaging;

import com.testedminds.template.config.Environment;
import com.testedminds.template.handlers.LoggingHandler;
import com.testedminds.template.handlers.MessageHandler;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static com.testedminds.template.handlers.HandlingResult.success;
import static org.junit.Assert.assertEquals;

public class FanoutSubscriberTest {
  private final static String MESSAGE = "This is a fanout test.";
  private final static String EXCHANGE_NAME = "direct_profile";
  private final static String EXCHANGE_TYPE = "fanout";
  @Test
  public void shouldProcessesMessagesWithMultipleThreads() throws Exception {
    Map<String, String> env = new HashMap<>();
    env.put("DEBUG_MESSAGE_HANDLING", "false");
    com.testedminds.template.utils.Environment.set(env);

    int numMessages = 1000;
    final AtomicReference<String> message = new AtomicReference<>();
    final CountDownLatch counter = new CountDownLatch(numMessages);

    MessageHandler successHandler = successHandler(message, counter);
    // This must be declared before the publisher in order to set up state in Rabbit.
    final Subscriber sub = new Subscriber(successHandler, Environment.amqpUri(), EXCHANGE_NAME, EXCHANGE_TYPE, "");
    final Publisher messenger = messenger(sub);

    // publish messages to the consumer we just declared.
    List<Callable<Object>> tasks = new ArrayList<>();
    for (int i = 0; i < numMessages; i++) {
      tasks.add(
              Executors.callable(() -> {
                messenger.publish(MESSAGE);
              }));
    }

    // Send messages using only two threads so as not to monopolize the threads in the subscriber.
    Executors.newFixedThreadPool(2).invokeAll(tasks);
    messenger.close();
    counter.await(500, TimeUnit.MILLISECONDS);   // 500 ms should be more than enough time
    assertEquals(MESSAGE, message.get());
  }

  private MessageHandler successHandler(final AtomicReference<String> message, final CountDownLatch counter) {
    return (delivery, headers) -> {
      message.set(delivery);
      counter.countDown();
      return success();
    };
  }

  private Publisher messenger(final Subscriber sub) {
    final Publisher messenger = new Publisher(EXCHANGE_NAME, "", Environment.AMQP_LOCALHOST);
    // Empty the queue from previous test runs
    messenger.publish(MESSAGE);

    new Thread(sub::deliveryLoop).start();
    return messenger;
  }


}
