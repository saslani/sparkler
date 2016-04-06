package com.testedminds.template.handlers;

import java.util.Map;

public interface MessageHandler {
  HandlingResult handle(String message, Map<String, Object> handlers);
}
