package com.testedminds.template.handlers;

import java.util.List;
import java.util.Map;

import static com.testedminds.template.handlers.HandlingResult.SUCCESSFUL;

public class ResultsSummaryPrinter {

  public static String printSummary(Map<String, List<HandlingResult>> summary) {
    StringBuilder builder = new StringBuilder();
    builder.append("Handling Results\n\t");
    builder.append(SUCCESSFUL);
    builder.append(": ");
    builder.append(summary.get(SUCCESSFUL).size());

    for (String result : summary.keySet()) {
      if (HandlingResult.isSuccess(result)) continue;
      builder.append("\n\tskipped ");
      builder.append(result);
      builder.append(": ");
      builder.append(summary.get(result).size());
    }
    return builder.toString();
  }
}
