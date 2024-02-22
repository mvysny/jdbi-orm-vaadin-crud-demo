package com.vaadin.starter.skeleton;

import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * Run PostgreSQL in testcontainers if Maven test is run with the `test.postgresql`
 * property (passed via `mvn -DargLine="-Dtest.postgresql" test`).
 */
public class PostgreSQLUtils {
    public static boolean isTestWithPGSQL() {
        return System.getProperties().containsKey("test.postgresql") && DockerClientFactory.instance().isDockerAvailable();
    }

    private PostgreSQLContainer<?> container;

    public void start() {
        if (isTestWithPGSQL()) {
            System.out.println("Running tests with PostgreSQL");
            container = new PostgreSQLContainer<>("postgres:16.2");
            container.start();
            Bootstrap.jdbcUrl = container.getJdbcUrl();
            Bootstrap.jdbcUsername = container.getUsername();
            Bootstrap.jdbcPassword = container.getPassword();
        }
    }

    public void stop() {
        if (container != null) {
            container.stop();
            container = null;
        }
    }
}
