package com.vaadin.starter.skeleton.person;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import org.jetbrains.annotations.NotNull;

/**
 * The form, which edits a single {@link Person}.
 * <ul>
 *     <li>To populate the fields, just call <code>form.binder.readBean(person)</code></li>
 *     <li>To validate and save the data, just call <code>binder.validate().isOk && binder.writeBeanIfValid(person)</code></li>
 * </ul>
 */
public class PersonForm extends FormLayout {
    @NotNull
    public final Binder<Person> binder = new BeanValidationBinder<>(Person.class);

    public PersonForm() {
        final TextField nameField = new TextField("Name:");
        nameField.focus();
        binder.bind(nameField, "name");
        add(nameField);

        final TextField ageField = new TextField("Age:");
        binder.forField(ageField)
                .withConverter(new StringToIntegerConverter("not a number"))
                .bind("age");
        add(ageField);

        final DatePicker dateOfBirthField = new DatePicker("Date of birth:");
        binder.forField(dateOfBirthField)
                .bind("dateOfBirth");
        add(dateOfBirthField);

        final ComboBox<Person.MaritalStatus> maritalStatusComboBox = new ComboBox<>(
                "Marital status:", Person.MaritalStatus.values());
        binder.forField(maritalStatusComboBox)
                .bind("maritalStatus");
        add(maritalStatusComboBox);

        final Checkbox aliveCheckbox = new Checkbox("Alive");
        binder.forField(aliveCheckbox)
                .bind("alive");
        add(aliveCheckbox);
    }
}
