package com.github.mouse0w0.peach.mcmod;

public class AttributeModifier {
    public enum Operation {
        ADD, MULTIPLY_BASE, MULTIPLY_TOTAL
    }

    private String attribute;
    private double amount;
    private Operation operation;

    public AttributeModifier() {
    }

    public AttributeModifier(String attribute, double amount, Operation operation) {
        this.attribute = attribute;
        this.amount = amount;
        this.operation = operation;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
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
