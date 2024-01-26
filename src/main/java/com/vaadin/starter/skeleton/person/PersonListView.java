package com.vaadin.starter.skeleton.person;

import com.gitlab.mvysny.jdbiorm.condition.Condition;
import com.gitlab.mvysny.jdbiorm.vaadin.EntityDataProvider;
import com.gitlab.mvysny.jdbiorm.vaadin.filter.*;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.NativeButtonRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.starter.skeleton.Bootstrap;
import org.jetbrains.annotations.NotNull;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * The main view contains a Grid which shows the list of {@link Person}s. The Grid supports
 * sorting and filtering.
 */
@Route("")
public class PersonListView extends VerticalLayout {

    private transient DateTimeFormatter dateFormatter;

    /**
     * Cached date formatter, used to format {@link Person#DATEOFBIRTH}.
     * @return the date formatter, not null.
     */
    @NotNull
    private DateTimeFormatter getDateFormatter() {
        if (dateFormatter == null) {
            dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
                    .withLocale(UI.getCurrent().getLocale());
        }
        return dateFormatter;
    }

    /**
     * Loads {@link Person} from the database. Grid calls this.
     */
    @NotNull
    private final EntityDataProvider<Person> dataProvider = new EntityDataProvider<>(Person.class);

    /**
     * Filter bar components follow. Whenever a filter component is changed, the SQL WHERE Condition
     * is reconstructed in {@link #updateFilter()} and set to {@link #dataProvider} via {@link EntityDataProvider#setFilter(Condition)}.
     * That forces the Grid to refresh itself and reload the data it shows, using
     * the newly set Condition.
     */
    private final NumberRangePopup idFilter = new NumberRangePopup();
    private final FilterTextField nameFilter = new FilterTextField();
    private final NumberRangePopup ageFilter = new NumberRangePopup();
    private final BooleanFilterField aliveFilter = new BooleanFilterField();
    private final EnumFilterField<Person.MaritalStatus> maritalStatusFilter = new EnumFilterField<>(Person.MaritalStatus.class);
    private final DateRangePopup dateOfBirthFilter = new DateRangePopup();

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

        final Grid.Column<Person> idColumn = personGrid.addColumn(Person::getId)
                .setHeader("ID")
                .setSortable(true)
                .setKey(Person.ID.toExternalString());
        idFilter.setId("idFilter");
        idFilter.addValueChangeListener(e -> updateFilter());
        filterBar.getCell(idColumn).setComponent(idFilter);

        final Grid.Column<Person> nameColumn = personGrid.addColumn(Person::getName)
                .setHeader("Name")
                .setSortable(true)
                .setKey(Person.NAME.toExternalString());
        nameFilter.setId("nameFilter");
        filterBar.getCell(nameColumn).setComponent(nameFilter);
        nameFilter.addValueChangeListener(e -> updateFilter());

        final Grid.Column<Person> ageColumn = personGrid.addColumn(Person::getAge)
                .setHeader("Age")
                .setSortable(true)
                .setKey(Person.AGE.toExternalString());
        ageFilter.setId("ageFilter");
        ageFilter.addValueChangeListener(e -> updateFilter());
        filterBar.getCell(ageColumn).setComponent(ageFilter);

        final Grid.Column<Person> aliveColumn = personGrid.addColumn(Person::getAlive)
                .setHeader("Alive")
                .setSortable(true)
                .setKey(Person.ISALIVE.toExternalString());
        aliveFilter.setId("aliveFilter");
        filterBar.getCell(aliveColumn).setComponent(aliveFilter);
        aliveFilter.addValueChangeListener(e -> updateFilter());

        final Grid.Column<Person> dobColumn = personGrid.addColumn(it -> getDateFormatter().format(it.getDateOfBirth()))
                .setHeader("Date Of Birth")
                .setSortable(true)
                .setKey(Person.DATEOFBIRTH.toExternalString());
        dateOfBirthFilter.setId("dateOfBirthFilter");
        dateOfBirthFilter.addValueChangeListener(e -> updateFilter());
        filterBar.getCell(dobColumn).setComponent(dateOfBirthFilter);

        final Grid.Column<Person> maritalStatusColumn = personGrid.addColumn(Person::getMaritalStatus)
                .setHeader("Marital Status")
                .setSortable(true)
                .setKey(Person.MARITALSTATUS.toExternalString());
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

    /**
     * Recalculates the filter Condition and sets it to the {@link #dataProvider}
     * via {@link EntityDataProvider#setFilter(Condition)}.
     */
    private void updateFilter() {
        // poll all filter components and calculate the new SQL WHERE Condition.
        Condition c = Condition.NO_CONDITION;
        c = c.and(idFilter.getValue().asLongInterval().contains(Person.ID));
        if (!nameFilter.isEmpty()) {
            c = c.and(Person.NAME.likeIgnoreCase(nameFilter.getValue().trim() + "%"));
        }
        c = c.and(ageFilter.getValue().asIntegerInterval().contains(Person.AGE));
        if (!aliveFilter.isEmpty()) {
            c = c.and(Person.ISALIVE.is(aliveFilter.getValue()));
        }
        if (!maritalStatusFilter.isAllOrNothingSelected()) {
            c = c.and(Person.MARITALSTATUS.in(maritalStatusFilter.getValue()));
        }
        c = c.and(dateOfBirthFilter.getValue().contains(Person.DATEOFBIRTH, ZoneId.systemDefault()));
        // Set the new filter. This forces the associated Grid to refresh itself and reload the data it shows, using
        // the newly set Condition.
        dataProvider.setFilter(c);
    }
}
