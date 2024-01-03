package com.vaadin.starter.skeleton.person;

import com.gitlab.mvysny.jdbiorm.condition.Condition;
import com.gitlab.mvysny.jdbiorm.vaadin.EntityDataProvider;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.NativeButtonRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.starter.skeleton.Bootstrap;
import com.vaadin.starter.skeleton.filters.BooleanFilterField;
import com.vaadin.starter.skeleton.filters.EnumFilterField;
import com.vaadin.starter.skeleton.filters.FilterTextField;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * The main view contains a button and a click listener.
 */
@Route("")
public class PersonListView extends VerticalLayout {

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
            .withLocale(UI.getCurrent().getLocale());
    private final EntityDataProvider<Person> dataProvider = new EntityDataProvider<>(Person.class);
    private final FilterTextField nameFilter = new FilterTextField();
    private final BooleanFilterField aliveFilter = new BooleanFilterField();
    private final EnumFilterField<Person.MaritalStatus> maritalStatusFilter = new EnumFilterField<>(Person.MaritalStatus.class);

    public PersonListView() {
        setSizeFull();
        final Grid<Person> personGrid = new Grid<>();
        // append first header row: the column captions and the sorting indicator will appear here.
        personGrid.appendHeaderRow();
        // the second header row will host filter components.
        final HeaderRow filterBar = personGrid.appendHeaderRow();
        add(new Button("Re-generate test data", e -> {
            Bootstrap.generateTestingData();
            personGrid.getDataProvider().refreshAll();
        }));

        // Don't forget to add database index to every sortable column.
        personGrid.addColumn(Person::getId)
                .setHeader("ID")
                .setSortable(true)
                .setKey(Person.ID.getName().getName());
        final Grid.Column<Person> nameColumn = personGrid.addColumn(Person::getName)
                .setHeader("Name")
                .setSortable(true)
                .setKey(Person.NAME.getName().getName());
        nameFilter.setId("nameFilter");
        filterBar.getCell(nameColumn).setComponent(nameFilter);
        nameFilter.addValueChangeListener(e -> updateFilter());
        personGrid.addColumn(Person::getAge)
                .setHeader("Age")
                .setSortable(true)
                .setKey(Person.AGE.getName().getName());
        final Grid.Column<Person> aliveColumn = personGrid.addColumn(Person::getAlive)
                .setHeader("Alive")
                .setSortable(true)
                .setKey(Person.ISALIVE.getName().getName());
        aliveFilter.setId("aliveFilter");
        filterBar.getCell(aliveColumn).setComponent(aliveFilter);
        aliveFilter.addValueChangeListener(e -> updateFilter());
        personGrid.addColumn(it -> dateFormatter.format(it.getDateOfBirth()))
                .setHeader("Date Of Birth")
                .setSortable(true)
                .setKey(Person.DATEOFBIRTH.getName().getName());
        final Grid.Column<Person> maritalStatusColumn = personGrid.addColumn(Person::getMaritalStatus)
                .setHeader("Marital Status")
                .setSortable(true)
                .setKey(Person.MARITALSTATUS.getName().getName());
        maritalStatusFilter.setId("maritalStatusFilter");
        filterBar.getCell(maritalStatusColumn).setComponent(maritalStatusFilter);
        maritalStatusFilter.addValueChangeListener(e -> updateFilter());
        personGrid.addColumn(new NativeButtonRenderer<>("Edit", item -> {
            final CreateEditPersonDialog dialog = new CreateEditPersonDialog(item);
            dialog.onSaveOrCreateListener = () -> personGrid.getDataProvider().refreshAll();
            dialog.open();
        })).setKey("edit");

        personGrid.addColumn(new NativeButtonRenderer<>("Delete", item -> {
            item.delete();
            personGrid.getDataProvider().refreshAll();
        })).setKey("delete");

        personGrid.setDataProvider(dataProvider);
        personGrid.setWidthFull();
        addAndExpand(personGrid);
        updateFilter();
    }

    private void updateFilter() {
        Condition c = Condition.NO_CONDITION;
        if (!nameFilter.isEmpty()) {
            c = c.and(Person.NAME.likeIgnoreCase(nameFilter.getValue().trim() + "%"));
        }
        if (!aliveFilter.isEmpty()) {
            c = c.and(aliveFilter.getValue() ? Person.ISALIVE.isTrue() : Person.ISALIVE.isFalse());
        }
        if (!maritalStatusFilter.isAllOrNothingSelected()) {
            c = c.and(Person.MARITALSTATUS.in(maritalStatusFilter.getValue()));
        }
        dataProvider.setFilter(c);
    }
}
