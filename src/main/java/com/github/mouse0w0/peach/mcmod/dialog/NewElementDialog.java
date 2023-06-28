package com.github.mouse0w0.peach.mcmod.dialog;

import com.github.mouse0w0.peach.javafx.util.FXUtils;
import com.github.mouse0w0.peach.javafx.util.Validator;
import com.github.mouse0w0.peach.l10n.AppL10n;
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
    private ChoiceBox<ElementProvider<?>> type;
    @FXML
    private Text identifier;

    public static void show(Project project, Path path, Window window) {
        Stage stage = new Stage();
        stage.setScene(new Scene(new NewElementDialog(project, path)));
        stage.setTitle(AppL10n.localize("dialog.newElement.title"));
        stage.initOwner(window);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    public NewElementDialog(Project project, Path path) {
        this.project = project;
        this.path = path;
        FXUtils.loadFXML(this, "ui/mcmod/NewElement.fxml", AppL10n.getResourceBundle());

        getStylesheets().add("/style/style.css");

        Validator.error(name,
                FileUtils::validateFileNameWithoutExtension,
                AppL10n.localize("validate.invalidFileName"));
        name.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP -> {
                    type.getSelectionModel().selectPrevious();
                    event.consume();
                }
                case DOWN -> {
                    type.getSelectionModel().selectNext();
                    event.consume();
                }
            }
        });
        name.textProperty().addListener(observable ->
                identifier.setText(ModUtils.tryConvertToIdentifier(name.getText())));

        type.setConverter(new StringConverter<>() {
            @Override
            public String toString(ElementProvider<?> object) {
                return AppL10n.localize(object.getTranslationKey());
            }

            @Override
            public ElementProvider<?> fromString(String string) {
                throw new UnsupportedOperationException();
            }
        });
        type.getItems().addAll(ElementRegistry.getInstance().getElementProviders());
        type.setValue(type.getItems().get(0));
    }

    @FXML
    private void onFinish() {
        if (!Validator.test(name)) return;

        ElementManager.getInstance(project).createElement(path, type.getValue(), name.getText());

        FXUtils.hideWindow(this);
    }

    @FXML
    private void onCancel() {
        FXUtils.hideWindow(this);
    }
}
