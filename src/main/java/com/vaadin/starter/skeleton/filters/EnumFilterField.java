package com.vaadin.starter.skeleton.filters;

import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBoxVariant;
import org.jetbrains.annotations.NotNull;

public class EnumFilterField<E extends Enum<E>> extends MultiSelectComboBox<E> {
    public EnumFilterField(@NotNull Class<E> enumClass) {
        setClearButtonVisible(true);
        addThemeVariants(MultiSelectComboBoxVariant.LUMO_SMALL);
        setItems(enumClass.getEnumConstants());
    }

    public boolean isAllSelected() {
        return getSelectedItems().size() == getListDataView().getItemCount();
    }

    public boolean isAllOrNothingSelected() {
        return getSelectedItems().isEmpty() || isAllSelected();
    }
}
