package com.vaadin.starter.skeleton.person;

import com.vaadin.starter.skeleton.AbstractAppLauncher;
import org.junit.jupiter.api.Test;

public class PersonFormTest extends AbstractAppLauncher {
    @Test
    public void smoke() {
        new PersonForm();
    }
}
