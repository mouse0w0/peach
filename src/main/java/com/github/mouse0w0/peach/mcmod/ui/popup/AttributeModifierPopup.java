package com.github.mouse0w0.peach.mcmod.ui.popup;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.mcmod.Attribute;
import com.github.mouse0w0.peach.mcmod.AttributeModifier;
import com.github.mouse0w0.peach.ui.control.TagCell;
import javafx.geometry.Insets;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import org.controlsfx.control.PopOver;

public class AttributeModifierPopup extends PopOver {
    private final ChoiceBox<Attribute> attribute;
    private final ChoiceBox<AttributeModifier.Operation> operation;
    private final Spinner<Double> amount;

    private TagCell<AttributeModifier> cell;

    public AttributeModifierPopup() {
        getRoot().minHeightProperty().unbind();
        setArrowLocation(ArrowLocation.TOP_CENTER);
        setAnimated(false);

        GridPane grid = new GridPane();
        grid.setHgap(9);
        grid.setVgap(9);
        grid.setPadding(new Insets(9));
        setContentNode(grid);

        attribute = new ChoiceBox<>();
        attribute.setPrefWidth(150);
        attribute.getItems().addAll(Attribute.getAttributes());
        attribute.setConverter(new StringConverter<Attribute>() {
            @Override
            public String toString(Attribute object) {
                return object.getLocalizedName();
            }

            @Override
            public Attribute fromString(String string) {
                throw new UnsupportedOperationException();
            }
        });
        grid.addRow(0, new Text(I18n.translate("attributeModifier.attribute")), attribute);

        operation = new ChoiceBox<>();
        operation.setPrefWidth(150);
        operation.getItems().addAll(AttributeModifier.Operation.values());
        grid.addRow(1, new Text(I18n.translate("attributeModifier.operation")), operation);

        amount = new Spinner<>(Double.MIN_VALUE, Double.MAX_VALUE, 0);
        amount.setPrefWidth(150);
        amount.setEditable(true);
        grid.addRow(2, new Text(I18n.translate("attributeModifier.amount")), amount);

        setOnHiding(event -> cell.commitEdit(new AttributeModifier(attribute.getValue().getName(), amount.getValue(), operation.getValue())));
    }

    public void edit(TagCell<AttributeModifier> cell) {
        this.cell = cell;

        AttributeModifier attributeModifier = cell.getItem();
        attribute.setValue(Attribute.of(attributeModifier.getAttribute()));
        operation.setValue(attributeModifier.getOperation());
        amount.getValueFactory().setValue(attributeModifier.getAmount());

        show(cell);
    }
}
