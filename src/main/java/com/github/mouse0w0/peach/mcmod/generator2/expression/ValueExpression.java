package com.github.mouse0w0.peach.mcmod.generator2.expression;

import freemarker.core.Environment;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public final class ValueExpression extends BaseExpression implements Expression {

    public ValueExpression(@NotNull String expression, @NotNull Configuration configuration) {
        super(expression, configuration);
    }

    @Override
    protected Template createTemplate() throws IOException {
        return new Template("ValueExpression", "${_(" + expression + ")}", configuration);
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
