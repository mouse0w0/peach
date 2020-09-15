package com.github.mouse0w0.peach.mcmod.ui;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.mcmod.element.ElementManager;
import com.github.mouse0w0.peach.mcmod.element.ElementRegistry;
import com.github.mouse0w0.peach.mcmod.element.ElementType;
import com.github.mouse0w0.peach.mcmod.util.ModUtils;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.ui.util.FXUtils;
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

public class NewModElementUI extends BorderPane {

    private final Project project;

    @FXML
    private TextField name;
    @FXML
    private ChoiceBox<ElementType<?>> type;
    @FXML
    private Text registerName;

    public static void show(Project project, Window window) {
        Stage stage = new Stage();
        stage.setScene(new Scene(new NewModElementUI(project)));
        stage.setTitle(I18n.translate("ui.new_mod_element.title"));
        stage.initOwner(window);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    public NewModElementUI(Project project) {
        this.project = project;
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

        type.setConverter(new StringConverter<ElementType<?>>() {
            @Override
            public String toString(ElementType<?> object) {
                return I18n.translate(object.getTranslationKey());
            }

            @Override
            public ElementType<?> fromString(String string) {
                throw new UnsupportedOperationException();
            }
        });
        type.getItems().addAll(ElementRegistry.getInstance().getElementTypes());
        type.setValue(type.getItems().get(0));
    }

    @FXML
    private void onFinish() {
        ElementManager.getInstance(project).createElement(type.getValue(), name.getText());
        FXUtils.hideWindow(this);
    }

    @FXML
    private void onCancel() {
        FXUtils.hideWindow(this);
    }
}
