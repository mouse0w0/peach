package com.github.mouse0w0.peach.mcmod;

import com.github.mouse0w0.i18n.I18n;

public class AttributeModifier {
    public static final AttributeModifier[] EMPTY_ARRAY = new AttributeModifier[0];

    private final String attribute;
    private final double amount;
    private final Operation operation;

    public enum Operation {
        ADD, MULTIPLY_BASE, MULTIPLY_TOTAL
    }

    public AttributeModifier(String attribute, double amount, Operation operation) {
        this.attribute = attribute;
        this.amount = amount;
        this.operation = operation;
    }

    public String getAttribute() {
        return attribute;
    }

    public double getAmount() {
        return amount;
    }

    public Operation getOperation() {
        return operation;
    }

    public String toLocalizedText() {
        final String localizedAttribute = Attribute.getLocalizedName(attribute);
        final double value = operation == null || operation == Operation.ADD ? amount : amount * 100.0D;
        if (operation == Operation.MULTIPLY_BASE) {
            return I18n.format("attributeModifier.text.multiple_base", localizedAttribute, value);
        } else if (operation == Operation.MULTIPLY_TOTAL) {
            return I18n.format("attributeModifier.text.multiple_total", localizedAttribute, value);
        } else {
            return I18n.format("attributeModifier.text.add", localizedAttribute, value);
        }
    }

    @Override
    public String toString() {
        return "AttributeModifier{" +
                "attribute='" + attribute + '\'' +
                ", amount=" + amount +
                ", operation=" + operation +
                '}';
    }
}
