package com.github.mouse0w0.peach.mcmod.generator2.expression;

import freemarker.template.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

public abstract class BaseExpression implements Expression {
    protected final String expression;
    protected final Configuration configuration;

    private volatile Template template;

    public BaseExpression(@NotNull String expression, @NotNull Configuration configuration) {
        this.expression = Objects.requireNonNull(expression, "expression");
        this.configuration = Objects.requireNonNull(configuration, "configuration");
    }

    protected abstract Template createTemplate() throws IOException;

    protected final Template getTemplate() throws IOException {
        if (template == null) {
            synchronized (this) {
                if (template == null) {
                    template = createTemplate();
                }
            }
        }
        return template;
    }

    protected final ObjectWrapper getObjectWrapper() {
        return configuration.getObjectWrapper();
    }

    protected final TemplateHashModel wrapDataModel(Object dataModel) throws ExpressionException {
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
}
