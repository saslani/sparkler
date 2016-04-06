package com.testedminds.template.messaging;

import java.util.HashMap;
import java.util.Map;

public class QueueProperties {
  public static Map<String, Object> defaultProps() {
    Map<String, Object> props = new HashMap<>();
//        props.put("x-ha-policy", "all");
    return props;
  }
}
