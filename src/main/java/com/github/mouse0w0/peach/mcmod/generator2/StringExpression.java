package com.github.mouse0w0.peach.mcmod.generator2;

import freemarker.core.Environment;
import freemarker.template.*;
import org.apache.commons.io.output.StringBuilderWriter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

public final class StringExpression implements Expression {
    private final String expression;
    private final Configuration configuration;

    private volatile Template template;

    public StringExpression(@NotNull String expression, @NotNull Configuration configuration) {
        this.expression = Objects.requireNonNull(expression, "expression");
        this.configuration = Objects.requireNonNull(configuration, "configuration");
    }

    @Override
    public String eval(Object dataModel) throws ExpressionException {
        return eval(wrapDataModel(dataModel));
    }

    @Override
    public String eval(TemplateHashModel dataModel) throws ExpressionException {
        try {
            Template template = getTemplate();
            StringBuilderWriter sbw = new StringBuilderWriter();
            new Environment(template, dataModel, sbw).process();
            return sbw.toString();
        } catch (TemplateException | IOException e) {
            throw new ExpressionException("Failed to evaluate expression: " + expression, e);
        }
    }

    private TemplateHashModel wrapDataModel(Object dataModel) throws ExpressionException {
        if (dataModel instanceof TemplateHashModel) {
            return (TemplateHashModel) dataModel;
        }
        ObjectWrapper objectWrapper = getObjectWrapper();
        if (dataModel == null) {
            return new SimpleHash(objectWrapper);
        }
        TemplateModel wrappedDataModel;
        try {
            wrappedDataModel = objectWrapper.wrap(dataModel);
        } catch (TemplateModelException e) {
            throw new ExpressionException(
                    objectWrapper.getClass().getName() + " failed to wrap " + dataModel.getClass().getName(), e);
        }
        if (wrappedDataModel instanceof TemplateHashModel) {
            return (TemplateHashModel) wrappedDataModel;
        } else if (wrappedDataModel == null) {
            throw new ExpressionException(
                    objectWrapper.getClass().getName() + " converted " + dataModel.getClass().getName() + " to null.");
        } else {
            throw new ExpressionException(
                    objectWrapper.getClass().getName() + " didn't convert " + dataModel.getClass().getName() + " to a TemplateHashModel.");
        }
    }

    private ObjectWrapper getObjectWrapper() {
        return configuration.getObjectWrapper();
    }

    private Template getTemplate() throws IOException {
        if (template == null) {
            synchronized (this) {
                if (template == null) {
                    template = new Template("template", expression, configuration);
                }
            }
        }
        return template;
    }
}
