package com.vaadin.starter.skeleton.person;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * A dialog which edits given {@link #person}.
 * @author mavi
 */
public class CreateEditPersonDialog extends Dialog {
    @NotNull
    public final Person person;

    /**
     * A listener, invoked by this dialog when the user presses the "OK" button,
     * the new data passes the validation and has been written to the database.
     * The dialog will be closed automatically.
     */
    @NotNull
    public Runnable onSaveOrCreateListener = () -> {};

    /**
     * The form which actually edits a person.
     */
    private final PersonForm form = new PersonForm();

    public CreateEditPersonDialog(@NotNull Person person) {
        this.person = Objects.requireNonNull(person);

        final VerticalLayout content = new VerticalLayout();
        content.setMargin(true);
        content.add(form);

        final HorizontalLayout buttonBar = new HorizontalLayout();
        buttonBar.setSpacing(true);
        content.setAlignSelf(FlexComponent.Alignment.CENTER, buttonBar);
        final Button persist = new Button(isCreating() ? "Create" : "Save");
        persist.addClickListener(e -> okPressed());
        persist.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        final Button cancel = new Button("Cancel");
        cancel.addClickListener(e -> close());
        buttonBar.add(persist, cancel);
        content.add(buttonBar);

        add(content);

        form.binder.readBean(person);
    }

    private boolean isCreating() {
        return person.getId() == null;
    }

    private void okPressed() {
        if (!form.binder.validate().isOk() || !form.binder.writeBeanIfValid(person)) {
            Notification.show("There are errors in the form").addThemeVariants(NotificationVariant.LUMO_ERROR, NotificationVariant.LUMO_PRIMARY);
            return;
        }
        person.save();
        onSaveOrCreateListener.run();
        close();
    }
}
