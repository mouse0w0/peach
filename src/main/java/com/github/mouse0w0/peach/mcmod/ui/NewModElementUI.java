package com.github.mouse0w0.peach.mcmod.ui;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.mcmod.element.ElementDefinition;
import com.github.mouse0w0.peach.mcmod.element.ElementFile;
import com.github.mouse0w0.peach.mcmod.project.McModDataKeys;
import com.github.mouse0w0.peach.mcmod.service.McModService;
import com.github.mouse0w0.peach.mcmod.util.ModUtils;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.ui.project.ProjectWindow;
import com.github.mouse0w0.peach.ui.project.WindowManager;
import com.github.mouse0w0.peach.ui.util.FXUtils;
import com.github.mouse0w0.peach.ui.wizard.Wizard;
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

public class NewModElementUI extends BorderPane {

    @FXML
    private TextField name;
    @FXML
    private ChoiceBox<ElementDefinition<?>> type;
    @FXML
    private Text registerName;

    public static void show(Window window) {
        Stage stage = new Stage();
        stage.setScene(new Scene(new NewModElementUI()));
        stage.setTitle(I18n.translate("ui.new_mod_element.title"));
        stage.initOwner(window);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.showAndWait();
    }

    public NewModElementUI() {
        FXUtils.loadFXML(this, "ui/mcmod/NewModElement.fxml");

        name.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:
                    type.getSelectionModel().selectPrevious();
                    event.consume();
                    break;
                case DOWN:
                    type.getSelectionModel().selectNext();
                    event.consume();
                    break;
                case ENTER:
                    onFinish();
                    break;
            }
        });
        name.textProperty().addListener(observable ->
                registerName.setText(ModUtils.toRegisterName(name.getText())));

        type.setConverter(new StringConverter<ElementDefinition<?>>() {
            @Override
            public String toString(ElementDefinition<?> object) {
                return I18n.translate(object.getTranslationKey());
            }

            @Override
            public ElementDefinition<?> fromString(String string) {
                throw new UnsupportedOperationException();
            }
        });
        type.getItems().addAll(McModService.getInstance().getElementManager().getElements());
        type.setValue(type.getItems().get(0));
    }

    @FXML
    private void onFinish() {
        ProjectWindow window = WindowManager.getInstance().getFocusedWindow();
        Project project = window.getProject();
        ElementDefinition<?> definition = type.getValue();
        Path file = project.getData(McModDataKeys.SOURCES_PATH).resolve(name.getText() + "." + definition.getId() + ".json");
        ElementFile<?> elementFile = definition.load(file);
        Wizard wizard = definition.createWizard(elementFile);
        window.openTab(Wizard.createTab(wizard));
        FXUtils.hideWindow(this);
    }

    @FXML
    private void onCancel() {
        FXUtils.hideWindow(this);
    }
}
