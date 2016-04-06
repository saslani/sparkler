package com.testedminds.template.messaging;

import com.testedminds.template.config.Environment;
import com.testedminds.template.handlers.LoggingHandler;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

import static com.testedminds.template.handlers.HandlingResult.failure;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class SubscriberTest {
  @Test(expected = RuntimeException.class)
  public void shouldHandleIncorrectAmqpUrl(){
    new Subscriber(new LoggingHandler(), "baduser:", "", "", "");
  }

  @Test
  public void shouldNotPrintIfDebugFlagNotEnabled() throws Exception {
    Map<String, String> env = new HashMap<>();
    env.put("DEBUG_MESSAGE_HANDLING", "false");
    com.testedminds.template.utils.Environment.set(env);

    final Subscriber subscriber = new Subscriber(null, Environment.amqpUri(), "profile", "direct", "update");
    Logger mockLogger = mock(Logger.class);
    subscriber.setLogger(mockLogger);
    subscriber.log(failure("test failure", "message"));
    verify(mockLogger, never()).warn("test failure");
  }

  @Test
  public void shouldPrintFailureIfDebugFlagEnabled() throws Exception {
    Map<String, String> env = new HashMap<>();
    env.put("DEBUG_MESSAGE_HANDLING", "true");
    com.testedminds.template.utils.Environment.set(env);

    final Subscriber sub = new Subscriber(null, Environment.amqpUri(), "profile", "direct", "update");
    Logger mockLogger = mock(Logger.class);
    sub.setLogger(mockLogger);
    sub.log(failure("test failure", "message"));
    verify(mockLogger).warn("test failure: " + "message");
  }


}
