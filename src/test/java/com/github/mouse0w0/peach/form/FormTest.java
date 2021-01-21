package com.github.mouse0w0.peach.form;

import com.github.mouse0w0.peach.form.field.ComboBoxField;
import com.github.mouse0w0.peach.form.field.SpinnerField;
import com.github.mouse0w0.peach.form.field.TextFieldField;
import com.github.mouse0w0.peach.javafx.Check;
import com.github.mouse0w0.peach.javafx.util.NotificationLevel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FormTest extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Form form = new Form();

        Group properties = new Group();
        properties.setText("Properties");
        properties.setCollapsible(false);
        form.getGroups().add(properties);

        TextFieldField firstName = new TextFieldField();
        firstName.setText("First Name");
        firstName.setPromptText("First Name");
        firstName.getChecks().add(new Check<>(s -> s != null && s.length() > 0, NotificationLevel.ERROR, "Text cannot be empty."));
        firstName.setColSpan(ColSpan.HALF);

        TextFieldField lastName = new TextFieldField();
        lastName.setText("Last Name");
        lastName.setPromptText("Last Name");
        lastName.setColSpan(ColSpan.HALF);

        SpinnerField<Integer> age = new SpinnerField<>(0, 255, 1);
        age.setText("Age");
        age.setColSpan(ColSpan.HALF);

        ComboBoxField<String> sex = new ComboBoxField<>();
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