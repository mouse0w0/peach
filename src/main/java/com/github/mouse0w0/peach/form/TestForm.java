package com.github.mouse0w0.peach.form;

import com.github.mouse0w0.peach.form.element.ComboBoxElement;
import com.github.mouse0w0.peach.form.element.SpinnerElement;
import com.github.mouse0w0.peach.form.element.TextFieldElement;
import com.github.mouse0w0.peach.ui.util.Check;
import com.github.mouse0w0.peach.ui.util.NotificationLevel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestForm extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Form form = new Form();

        Group properties = new Group();
        properties.setText("Properties");
        properties.setCollapsible(false);
        form.getGroups().add(properties);

        TextFieldElement firstName = new TextFieldElement();
        firstName.setText("First Name");
        firstName.setPromptText("First Name");
        firstName.getChecks().add(new Check<>(s -> s != null && s.length() > 0, NotificationLevel.ERROR, "Text cannot be empty."));
        firstName.setColSpan(ColSpan.HALF);

        TextFieldElement lastName = new TextFieldElement();
        lastName.setText("Last Name");
        lastName.setPromptText("Last Name");
        lastName.setColSpan(ColSpan.HALF);

        SpinnerElement<Integer> age = new SpinnerElement<>(0, 255, 1);
        age.setText("Age");
        age.setColSpan(ColSpan.HALF);

        ComboBoxElement<String> sex = new ComboBoxElement<>();
        sex.setText("Sex");
        sex.setValue("Man");
        sex.getItems().addAll("Man", "Woman");
        sex.setColSpan(ColSpan.HALF);

        properties.getElements().addAll(firstName, lastName, age, sex);

        primaryStage.setScene(new Scene(new FormView(form)));
        primaryStage.setWidth(600);
        primaryStage.setHeight(400);
        primaryStage.show();
    }
}
