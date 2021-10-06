package com.github.mouse0w0.peach.mcmod.ui.form;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.form.field.ValueField;
import com.github.mouse0w0.peach.mcmod.Identifier;
import com.github.mouse0w0.peach.mcmod.util.ResourceStore;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;

public class ModelField extends ValueField<Identifier> {
    public static final Identifier CUSTOM = new Identifier("buildin", "custom");

    private final ResourceStore resourceStore;

    public ModelField(ResourceStore resourceStore) {
        this.resourceStore = resourceStore;
    }

    private final ObjectProperty<Identifier> value = new SimpleObjectProperty<>(this, "value");

    @Override
    public ObjectProperty<Identifier> valueProperty() {
        return value;
    }

    @Override
    public Identifier getValue() {
        return value.get();
    }

    @Override
    public void setValue(Identifier value) {
        valueProperty().setValue(value);
    }

    private final ObservableList<Identifier> items = FXCollections.observableArrayList();

    public ObservableList<Identifier> getItems() {
        return items;
    }

    @Override
    protected Node createDefaultEditor() {
        ComboBox<Identifier> comboBox = new ComboBox<>(items);
        comboBox.setPrefWidth(10000);
        comboBox.valueProperty().bindBidirectional(valueProperty());
        comboBox.disableProperty().bind(disableProperty());
        comboBox.setConverter(new StringConverter<Identifier>() {
            @Override
            public String toString(Identifier object) {
                if ("custom".equals(object.getNamespace())) {
                    return I18n.translate("model.custom", object.getName());
                }
                return I18n.translate("model." + object.getNamespace() + "." + object.getName());
            }

            @Override
            public Identifier fromString(String string) {
                throw new UnsupportedOperationException();
            }
        });

        Button plusButton = new Button("...");

        return new HBox(9, comboBox, plusButton);
    }
}
