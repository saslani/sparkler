package com.testedminds.template.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class LoggingHandler implements MessageHandler {
  final static Logger logger = LoggerFactory.getLogger(LoggingHandler.class);
  @Override
  public HandlingResult handle(String message, Map<String, Object> handlers) {
    logger.info(String.format("Thread[%d]: Message Received: %s", Thread.currentThread().getId(), message));
    return null;
  }
}
