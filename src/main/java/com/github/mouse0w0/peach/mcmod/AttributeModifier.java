package com.github.mouse0w0.peach.mcmod;

import com.github.mouse0w0.peach.l10n.AppL10n;
import com.google.common.base.CaseFormat;

public class AttributeModifier {
    public static final AttributeModifier[] EMPTY_ARRAY = new AttributeModifier[0];

    private final String attribute;
    private final double amount;
    private final Operation operation;

    public enum Operation implements Localizable {
        ADD, MULTIPLY_BASE, MULTIPLY_TOTAL;

        private final String translationKey;

        Operation() {
            translationKey = "operation." + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name());
        }

        @Override
        public String getLocalizedText() {
            return AppL10n.localize(translationKey);
        }
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
            return AppL10n.localize("attributeModifier.text.multiple_base", localizedAttribute, value);
        } else if (operation == Operation.MULTIPLY_TOTAL) {
            return AppL10n.localize("attributeModifier.text.multiple_total", localizedAttribute, value);
        } else {
            return AppL10n.localize("attributeModifier.text.add", localizedAttribute, value);
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
