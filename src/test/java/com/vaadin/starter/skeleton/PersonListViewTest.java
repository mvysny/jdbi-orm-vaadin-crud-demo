package com.vaadin.starter.skeleton;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.textfield.TextField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
