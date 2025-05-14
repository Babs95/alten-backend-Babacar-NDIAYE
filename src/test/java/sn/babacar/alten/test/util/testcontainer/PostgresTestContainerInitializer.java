package sn.babacar.alten.test.util.testcontainer;

import lombok.extern.slf4j.Slf4j;
import sn.babacar.alten.test.util.TestUtil;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers(disabledWithoutDocker = true)
@Slf4j
public abstract class PostgresTestContainerInitializer {
  private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(
      TestUtil.postgresDockerImageName).withDatabaseName(TestUtil.TEST_CONTAINER_DB_NAME).withUsername(
      TestUtil.TEST_CONTAINER_DB_USERNAME).withPassword(TestUtil.TEST_CONTAINER_DB_PASSWORD).withExposedPorts(
      5432).withReuse(true);

  static {
    try {
      postgreSQLContainer.start();
      log.info("PostgreSQL container started successfully at {}", postgreSQLContainer.getJdbcUrl());
    } catch (Exception e) {
      log.error("Failed to start PostgreSQL container: {}", e.getMessage(), e);
      throw new RuntimeException("Could not start PostgreSQL container", e);
    }
  }

  @DynamicPropertySource
  public static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
    registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    registry.add("spring.datasource.password", postgreSQLContainer::getPassword);

    // Flyway properties
    registry.add("spring.flyway.url", postgreSQLContainer::getJdbcUrl);
    registry.add("spring.flyway.user", postgreSQLContainer::getUsername);
    registry.add("spring.flyway.password", postgreSQLContainer::getPassword);

    // HikariCP settings
    registry.add("spring.datasource.hikari.maximum-pool-size", () -> 10);
    registry.add("spring.datasource.hikari.connection-timeout", () -> 30000);
    registry.add("spring.datasource.hikari.idle-timeout", () -> 600000);
    registry.add("spring.datasource.hikari.max-lifetime", () -> 600000); // Reduced to 10 minutes
    registry.add("spring.datasource.hikari.validation-timeout", () -> 5000);
    registry.add("spring.datasource.hikari.leak-detection-threshold", () -> 5000);
    registry.add("spring.datasource.hikari.auto-commit", () -> true);
    registry.add("spring.datasource.hikari.pool-name", () -> "HikariPool-1");
    registry.add("spring.datasource.hikari.minimum-idle", () -> 5);

    // Retry configuration
    registry.add(
        "spring.datasource.hikari.initialization-fail-timeout", () -> 0); // Retry indefinitely
    registry.add(
        "spring.datasource.hikari.connection-init-sql",
        () -> "SELECT 1"); // Simple validation query
  }
}

