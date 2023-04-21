package com.vaadin.starter.skeleton;

import com.gitlab.mvysny.jdbiorm.JdbiOrm;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import static com.gitlab.mvysny.jdbiorm.JdbiOrm.jdbi;

/**
 * Boots up the application: configures the SQL database, starts up all services, etc.
 * Configures JDBI-ORM and creates the SQL table for the {@link Person} entity.
 * <p></p>
 * A standard servlet context listener, run by the servlet container such as Tomcat.
 */
@WebListener
public class Bootstrap implements ServletContextListener {
    public static String jdbcUrl = System.getenv("JDBC_URL");
    public static String jdbcUsername = System.getenv("JDBC_USERNAME");
    public static String jdbcPassword = System.getenv("JDBC_PASSWORD");

    /**
     * Initializes the application.
     * @param servletContextEvent unused
     */
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        log.info("Starting up");

        log.info("Initializing the database connection");
        if (jdbcUrl == null) {
            jdbcUrl = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
            jdbcUsername = "sa";
            jdbcPassword = "";
        }
        log.info("Connecting to " + jdbcUrl + " as " + jdbcUsername);
        // 1. Initialize the database.
        // JDBI-ORM requires a JDBC DataSource. We will use
        // the HikariCP connection pool which keeps certain amount of JDBC connections around since they're expensive
        // to construct.
        final HikariConfig hikariConfig = new HikariConfig();
        // We tell HikariCP to use the in-memory H2 database.
        hikariConfig.setJdbcUrl(jdbcUrl);
        hikariConfig.setUsername(jdbcUsername);
        hikariConfig.setPassword(jdbcPassword);
        hikariConfig.setMinimumIdle(0);
        // Let's create the DataSource and set it to JDBI-ORM
        JdbiOrm.setDataSource(new HikariDataSource(hikariConfig));
        // Done! The database layer is now ready to be used.

        log.info("Migrating database to newest version");
        // see https://flywaydb.org/ for more information. In short, Flyway will
        // apply scripts from src/main/resources/db/migration/, but only those that
        // haven't been applied yet.
        final boolean isPostgres = jdbcUrl.contains("postgres");
        final Flyway flyway = Flyway.configure()
                .dataSource(JdbiOrm.getDataSource())
                .locations("db/migration/common", isPostgres ? "db/migration/postgres" : "db/migration/h2")
                .load();
        flyway.migrate();

        log.info("Generating testing data");
        generateTestingData();

        log.info("Started");
    }

    public static void generateTestingData() {
        jdbi().useTransaction(handle -> {
            Person.dao.deleteAll();
            for (int i = 0; i < 200; i++) {
                Person.createDummy(i);
            }
        });
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        log.info("Shutting down");
        // Tear down the app. Simply close the JDBI-ORM, which will close the
        // underlying HikariDataSource, which will clean up the pool, close
        // all pooled JDBC connections, stop all threads etc.
        JdbiOrm.destroy();

        log.info("Shutdown complete");
    }

    private static final Logger log = LoggerFactory.getLogger(Bootstrap.class);
}
