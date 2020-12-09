package com.github.mouse0w0.peach.form;

import com.github.mouse0w0.peach.form.skin.FormViewSkin;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

public class FormView extends Control {

    private ObjectProperty<Form> form;

    public FormView() {
        getStyleClass().setAll("form-view");
    }

    public FormView(Form form) {
        this();
        setForm(form);
    }

    public ObjectProperty<Form> formProperty() {
        if (form == null) {
            form = new SimpleObjectProperty<>(this, "form");
        }
        return form;
    }

    public Form getForm() {
        return form != null ? form.get() : null;
    }

    public void setForm(Form form) {
        formProperty().set(form);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new FormViewSkin(this);
    }

    @Override
    public String getUserAgentStylesheet() {
        return FormView.class.getResource("FormView.css").toExternalForm();
    }
}
