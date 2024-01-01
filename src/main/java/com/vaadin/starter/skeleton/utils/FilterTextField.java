package com.vaadin.starter.skeleton.utils;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;

public class FilterTextField extends TextField {
    public FilterTextField() {
        addThemeVariants(TextFieldVariant.LUMO_SMALL);
        setClearButtonVisible(true);
        setValueChangeMode(ValueChangeMode.LAZY);
    }
}
