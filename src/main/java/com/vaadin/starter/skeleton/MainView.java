package com.vaadin.starter.skeleton;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.NativeButtonRenderer;
import com.vaadin.flow.router.Route;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * The main view contains a button and a click listener.
 */
@Route("")
public class MainView extends VerticalLayout {

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
            .withLocale(UI.getCurrent().getLocale());

    public MainView() {
        final Grid<Person> personGrid = new Grid<>();
        // Don't forget to add database index to every sortable column.
        personGrid.addColumn(Person::getId)
                .setHeader("ID")
                .setSortable(true)
                .setKey("id");
        personGrid.addColumn(Person::getName)
                .setHeader("Name")
                .setSortable(true)
                .setKey("name");
        personGrid.addColumn(Person::getAge)
                .setHeader("Age")
                .setSortable(true)
                .setKey("age");
        personGrid.addColumn(Person::getAlive)
                .setHeader("Alive")
                .setSortable(true)
                .setKey("isAlive");
        personGrid.addColumn(it -> dateFormatter.format(it.getDateOfBirth()))
                .setHeader("Date Of Birth")
                .setSortable(true)
                .setKey("dateOfBirth");
        personGrid.addColumn(Person::getMaritalStatus)
                .setHeader("Marital Status")
                .setSortable(true)
                .setKey("maritalStatus");
        personGrid.addColumn(new NativeButtonRenderer<>("Edit", item -> {
            final CreateEditPersonDialog dialog = new CreateEditPersonDialog(item);
            dialog.onSaveOrCreateListener = () -> personGrid.getDataProvider().refreshAll();
            dialog.open();
        })).setKey("edit");

        personGrid.addColumn(new NativeButtonRenderer<>("Delete", item -> {
            item.delete();
            personGrid.getDataProvider().refreshAll();
        })).setKey("delete");

        personGrid.setItems(new EntityDataProvider<>(Person.dao));
        add(personGrid);
    }
}
