package com.vaadin.starter.skeleton;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.renderer.NativeButtonRenderer;
import com.vaadin.flow.router.Route;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Stream;

/**
 * The main view contains a button and a click listener.
 */
@Route("")
public class MainView extends VerticalLayout {

    public MainView() {

        // @todo mavi sorting doesn't work, fix!
        DataProvider<Person, Void> dp = createPersonDataProvider();

        final Grid<Person> personGrid = new Grid<>(Person.class);
        personGrid.setDataProvider(dp);
        add(personGrid);

        personGrid.addColumn(new NativeButtonRenderer<>("Edit", item -> {
            final CreateEditPersonDialog dialog = new CreateEditPersonDialog(item);
            dialog.onSaveOrCreateListener = () -> personGrid.getDataProvider().refreshAll();
            dialog.open();
        })).setKey("edit");

        personGrid.addColumn(new NativeButtonRenderer<>("Delete", item -> {
            item.delete();
            personGrid.getDataProvider().refreshAll();
        })).setKey("delete");
    }

    @NotNull
    private static CallbackDataProvider<Person, Void> createPersonDataProvider() {
        return DataProvider.fromCallbacks(new CallbackDataProvider.FetchCallback<Person, Void>() {
            @Override
            public Stream<Person> fetch(Query<Person, Void> query) {
                log.info("Fetching Person: " + query.getOffset() + " " + query.getLimit());
                return Person.dao.findAll((long) query.getOffset(), (long) query.getLimit()).stream();
            }
        }, new CallbackDataProvider.CountCallback<Person, Void>() {
            @Override
            public int count(Query<Person, Void> query) {
                return (int) Person.dao.count();
            }
        });
    }

    private static final Logger log = LoggerFactory.getLogger(MainView.class);
}
