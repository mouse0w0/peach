package com.github.mouse0w0.peach.mcmod.generator2.expression;

import freemarker.template.TemplateHashModel;

public interface Expression {
    Object eval(Object dataModel) throws ExpressionException;

    Object eval(TemplateHashModel dataModel) throws ExpressionException;
}
