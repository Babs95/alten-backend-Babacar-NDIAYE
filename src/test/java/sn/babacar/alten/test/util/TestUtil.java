package sn.babacar.alten.test.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUtil {
  public static final ObjectMapper objectMapper = new ObjectMapper().disable(
      DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).disable(
      DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE).findAndRegisterModules();

  public static String postgresDockerImageName = "postgres:16.3-alpine";
  public static String TEST_CONTAINER_DB_NAME = "alten_test_db";
  public static String TEST_CONTAINER_DB_USERNAME = "terranga";
  public static String TEST_CONTAINER_DB_PASSWORD = "terranga";

  // Utility method to convert object to JSON string
  public static String asJsonString(final Object obj) {
    try {
      return objectMapper.writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
