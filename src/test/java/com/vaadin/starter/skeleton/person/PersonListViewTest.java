package com.vaadin.starter.skeleton.person;

import com.gitlab.mvysny.jdbiorm.vaadin.filter.BooleanFilterField;
import com.gitlab.mvysny.jdbiorm.vaadin.filter.FilterTextField;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.starter.skeleton.AbstractAppLauncher;
import com.vaadin.starter.skeleton.Bootstrap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.IntStream;

import static com.github.mvysny.kaributesting.v10.GridKt.*;
import static com.github.mvysny.kaributesting.v10.LocatorJ.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Uses the Karibu-Testing framework: https://github.com/mvysny/karibu-testing/tree/master/karibu-testing-v10
 *
 * @author mavi
 */
@SuppressWarnings("unchecked")
public class PersonListViewTest extends AbstractAppLauncher {
    @BeforeEach
    public void navigateToMainView() {
        Person.dao.deleteAll();
        UI.getCurrent().navigate("");
    }

    @Test
    public void smokeTest() {
        _assertOne(PersonListView.class);
    }

    @Test
    public void testGridInitialContents() {
        Bootstrap.generateTestingData();
        final Grid<Person> grid = _get(Grid.class);
        expectRows(grid, 200);
    }

    @Test
    public void testDeletePerson() {
        Person.createDummy(0);
        assertNotNull(Person.dao.findByName("Jon Lord0"));
        final Grid<Person> grid = _get(Grid.class);
        _clickRenderer(grid, 0, "delete");
        expectRows(grid, 0);
        assertNull(Person.dao.findByName("Jon Lord0"));
    }

    @Test
    public void testFiltering() {
        Person.createDummy(0);
        final Grid<Person> grid = _get(Grid.class);

        final FilterTextField nameFilter = _get(FilterTextField.class, spec -> spec.withId("nameFilter"));
        _setValue(nameFilter, "foo");
        expectRows(grid, 0);
        _setValue(nameFilter, "Jon Lord");
        expectRows(grid, 1);
        _setValue(nameFilter, "");
        expectRows(grid, 1);

        final BooleanFilterField aliveFilter = _get(BooleanFilterField.class, spec -> spec.withId("aliveFilter"));
        _setValue(aliveFilter, true);
        expectRows(grid, 1);
        _setValue(aliveFilter, false);
        expectRows(grid, 0);
        _setValue(aliveFilter, true);
        _setValue(nameFilter, "foo");
        expectRows(grid, 0);
    }

    @Test
    public void testGridSorting() {
        Bootstrap.generateTestingData();
        final Grid<Person> grid = _get(Grid.class);
        _sortByKey(grid, Person.ID.toExternalString(), SortDirection.DESCENDING);
        final List<Integer> gridRows = new ArrayList<>(new LinkedHashSet<>(_findAll(grid).stream().map(Person::getAge).toList()));
        final List<Integer> expected = new ArrayList<>(IntStream.range(15, 82).boxed().toList());
        Collections.reverse(expected);
        assertEquals(expected, gridRows);

        // smoke tests, to make sure sorting doesn't blow up
        _sortByKey(grid, Person.NAME.toExternalString(), SortDirection.DESCENDING);
        _findAll(grid);
        _sortByKey(grid, Person.AGE.toExternalString(), SortDirection.DESCENDING);
        _findAll(grid);
        _sortByKey(grid, Person.ISALIVE.toExternalString(), SortDirection.DESCENDING);
        _findAll(grid);
        _sortByKey(grid, Person.DATEOFBIRTH.toExternalString(), SortDirection.DESCENDING);
        _findAll(grid);
        _sortByKey(grid, Person.MARITALSTATUS.toExternalString(), SortDirection.DESCENDING);
        _findAll(grid);
    }

    /**
     * Tests a simple person edit test case.
     */
    @Test
    public void testEditPerson() {
        Person.createDummy(0);
        Grid<Person> grid = _get(Grid.class);
        _clickRenderer(grid, 0, "edit");
        // the dialog is shown
        _assertOne(PersonForm.class);
        _setValue(_get(TextField.class, spec -> spec.withCaption("Name:")), "Vladimir Harkonnen");
        _click(_get(Button.class, spec -> spec.withCaption("Save")));

        // the dialog is gone
        _assertNone(PersonForm.class);
        assertEquals("Vladimir Harkonnen", Person.dao.findAll().get(0).getName());

        // check that the grid has been refreshed
        grid = _get(Grid.class);
        final String formattedRow = String.join(", ", _getFormattedRow(grid, 0));
        assertTrue(formattedRow.contains("Vladimir Harkonnen") &&
                !formattedRow.contains("Jon Lord"), "row: " + formattedRow);
    }
}
