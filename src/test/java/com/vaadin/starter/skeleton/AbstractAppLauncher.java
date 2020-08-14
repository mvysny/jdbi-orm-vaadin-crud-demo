package com.vaadin.starter.skeleton;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

/**
 * Makes sure the application is up-and-running and Vaadin is mocked (using the
 * Karibu-Testing library), so that everything is prepared for testing.
 * @author mavi
 */
public abstract class AbstractAppLauncher {
    private static Routes routes;

    @BeforeAll
    public static void setup() {
        // Typically we would have to laborously mock out the database in order to test the UI,
        // but we really don't have to: it's very easy to bootstrap the application
        // including the database. And so we can simply perform a full system testing right away very fast.
        new Bootstrap().contextInitialized(null);
        // initialize routes only once, to avoid view auto-detection before every test and to speed up the tests
        routes = new Routes().autoDiscoverViews("com.vaadin.starter.skeleton");
    }

    @AfterAll
    public static void teardown() {
        new Bootstrap().contextDestroyed(null);
    }

    @BeforeEach
    public void setupVaadin() {
        MockVaadin.setup(routes);
    }

    @AfterEach
    public void teardownVaadin() {
        MockVaadin.tearDown();
    }
}
