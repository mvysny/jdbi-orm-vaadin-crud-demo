package com.vaadin.starter.skeleton.filters;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.ComboBoxVariant;

/**
 * Allows the user to select 'true'/'false'/null.
 * If the value is non-null, the filtering mechanism should apply appropriate filter;
 * when null, no filter should be applied.
 */
public class BooleanFilterField extends ComboBox<Boolean> {
    public BooleanFilterField() {
        setClearButtonVisible(true);
        setItems(true, false);
        addThemeVariants(ComboBoxVariant.LUMO_SMALL);
    }
}
