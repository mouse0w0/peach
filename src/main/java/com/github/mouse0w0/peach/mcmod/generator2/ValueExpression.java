package com.github.mouse0w0.peach.mcmod.generator2;

import freemarker.core.Environment;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Objects;

public final class ValueExpression implements Expression {
    private final String expression;
    private final Configuration configuration;

    private volatile Template template;

    public ValueExpression(@NotNull String expression, @NotNull Configuration configuration) {
        this.expression = Objects.requireNonNull(expression, "expression");
        this.configuration = Objects.requireNonNull(configuration, "configuration");
    }

    @Override
    public Object eval(Object dataModel) throws ExpressionException {
        return eval(wrapDataModel(dataModel));
    }

    @Override
    public Object eval(TemplateHashModel dataModel) throws ExpressionException {
        try {
            Template template = getTemplate();
            ObjectWrapper objectWrapper = getObjectWrapper();
            if (!(objectWrapper instanceof BeansWrapper)) {
                throw new ExpressionException("ObjectWrapper must be BeansWrapper (was " +
                        objectWrapper.getClass().getName() + ")");
            }
            ResultCollector collector = new ResultCollector((BeansWrapper) objectWrapper);
            TemplateHashModel rootDataModel = new ExpressionDataModel(dataModel, collector);

            new Environment(template, rootDataModel, Writer.nullWriter()).process();
            return collector.get();
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
                    template = new Template("expression", "${_(" + expression + ")}", configuration);
                }
            }
        }
        return template;
    }

    private static final class ResultCollector implements TemplateMethodModelEx {
        private final BeansWrapper beansWrapper;
        private Object result;

        public ResultCollector(BeansWrapper beansWrapper) {
            this.beansWrapper = beansWrapper;
        }

        public Object get() {
            return result;
        }

        @Override
        public Object exec(List arguments) throws TemplateModelException {
            if (arguments == null || arguments.isEmpty()) {
                throw new TemplateModelException("_() called without arguments");
            }
            result = beansWrapper.unwrap((TemplateModel) arguments.get(0));
            return TemplateModel.NOTHING;
        }
    }

    private static final class ExpressionDataModel implements TemplateHashModel {
        private final TemplateHashModel delegate;
        private final ResultCollector collector;

        public ExpressionDataModel(TemplateHashModel delegate, ResultCollector collector) {
            this.delegate = delegate;
            this.collector = collector;
        }

        @Override
        public TemplateModel get(String key) throws TemplateModelException {
            return "_".equals(key) ? collector : delegate.get(key);
        }

        @Override
        public boolean isEmpty() {
            return false;
        }
    }
}
