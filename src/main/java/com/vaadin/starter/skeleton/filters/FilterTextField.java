package com.vaadin.starter.skeleton.filters;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;

/**
 * A text field that's modified for the use in the filter bar: it's smaller,
 * shows the clear button and fires value change events sooner.
 */
public class FilterTextField extends TextField {
    public FilterTextField() {
        addThemeVariants(TextFieldVariant.LUMO_SMALL);
        setClearButtonVisible(true);
        setValueChangeMode(ValueChangeMode.LAZY);
    }
}
