package com.vaadin.starter.skeleton;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.lumo.Lumo;

/**
 * Configures the main html body tag. See {@link AppShellConfigurator} for more
 * details.
 */
@PWA(name = "Project Base for Vaadin", shortName = "Project Base")
@StyleSheet(Lumo.STYLESHEET)
public class AppShell implements AppShellConfigurator {
}
