package com.github.mouse0w0.peach.mcmod.generator2.expression;

import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateHashModel;
import org.apache.commons.io.output.StringBuilderWriter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class StringExpression extends BaseExpression implements Expression {
    public StringExpression(@NotNull String expression, @NotNull Configuration configuration) {
        super(expression, configuration);
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

    @Override
    protected Template createTemplate() throws IOException {
        return new Template("StringExpression", expression, configuration);
    }
}
