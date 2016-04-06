package com.testedminds.template.db;

import com.testedminds.template.validators.Validations;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseUrl {

  private final static Logger logger = LoggerFactory.getLogger(DatabaseUrl.class);

  public static Map<String, String> params(String jdbcUrl) {
    Map<String, String> results = new HashMap<>();

    if (Validations.empty(jdbcUrl)) return results;
    if (!jdbcUrl.matches("jdbc:.*")) return results;

    String url = jdbcUrl.substring(5);

    URI dbUri;
    try {
      dbUri = new URI(url);
    } catch (URISyntaxException ex) {
      logger.warn("Invalid database url: " + jdbcUrl);
      return results;
    }

    results.put("database", dbUri.getPath());
    results.put("type", dbUri.getScheme());
    results.put("host", dbUri.getHost());
    results.put("port", String.valueOf(dbUri.getPort()));

    List<NameValuePair> queryParams = URLEncodedUtils.parse(dbUri, "UTF-8");
    queryParams.forEach(p -> results.put(p.getName(), p.getValue()));

    return results;
  }
}
