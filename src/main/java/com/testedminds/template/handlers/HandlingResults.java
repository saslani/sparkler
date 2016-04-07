package com.testedminds.template.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HandlingResults {
  private Map<String, List<HandlingResult>> summary = new HashMap<>();

  public void add(HandlingResult result) {
    List<HandlingResult> value = get(result);
    value.add(result);
    summary.put(result.type(), value);
  }

  @Override
  public String toString() {
    return "HandlingResults{" +
            "summary=" + summary +
            '}';
  }

  public String printSummary() {
    return ResultsSummaryPrinter.printSummary(summary);
  }

  private List<HandlingResult> get(HandlingResult result) {
    return summary.containsKey(result.type()) ? summary.get(result.type()) : new ArrayList<>();
  }

}
