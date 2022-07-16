package com.github.mouse0w0.peach.mcmod.dialog;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.javafx.FXUtils;
import com.github.mouse0w0.peach.javafx.Validator;
import com.github.mouse0w0.peach.mcmod.element.ElementManager;
import com.github.mouse0w0.peach.mcmod.element.ElementRegistry;
import com.github.mouse0w0.peach.mcmod.element.provider.ElementProvider;
import com.github.mouse0w0.peach.mcmod.util.ModUtils;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.util.FileUtils;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.StringConverter;

import java.nio.file.Path;

public class NewElementDialog extends BorderPane {

    private final Project project;
    private final Path path;

    @FXML
    private TextField name;
    @FXML
    private ChoiceBox<ElementProvider<?>> provider;
    @FXML
    private Text identifier;

    public static void show(Project project, Path path, Window window) {
        Stage stage = new Stage();
        stage.setScene(new Scene(new NewElementDialog(project, path)));
        stage.setTitle(I18n.translate("dialog.newElement.title"));
        stage.initOwner(window);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    public NewElementDialog(Project project, Path path) {
        this.project = project;
        this.path = path;
        FXUtils.loadFXML(this, "ui/mcmod/NewElement.fxml");

        getStylesheets().add("/style/style.css");

        Validator.error(name,
                FileUtils::validateFileNameWithoutExtension,
                I18n.translate("validate.illegalFileName"));
        name.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:
                    provider.getSelectionModel().selectPrevious();
                    event.consume();
                    break;
                case DOWN:
                    provider.getSelectionModel().selectNext();
                    event.consume();
                    break;
            }
        });
        name.textProperty().addListener(observable ->
                identifier.setText(ModUtils.tryConvertToIdentifier(name.getText())));

        provider.setConverter(new StringConverter<ElementProvider<?>>() {
            @Override
            public String toString(ElementProvider<?> object) {
                return I18n.translate(object.getTranslationKey());
            }

            @Override
            public ElementProvider<?> fromString(String string) {
                throw new UnsupportedOperationException();
            }
        });
        provider.getItems().addAll(ElementRegistry.getInstance().getElementProviders());
        provider.setValue(provider.getItems().get(0));
    }

    @FXML
    private void onFinish() {
        if (!Validator.test(name)) return;

        ElementManager.getInstance(project).createElement(path, provider.getValue(), name.getText());

        FXUtils.hideWindow(this);
    }

    @FXML
    private void onCancel() {
        FXUtils.hideWindow(this);
    }
}
