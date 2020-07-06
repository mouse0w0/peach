package com.github.mouse0w0.peach.ui.forge;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.forge.ForgeModService;
import com.github.mouse0w0.peach.forge.element.Element;
import com.github.mouse0w0.peach.ui.util.FXUtils;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.StringConverter;

public class NewModElementUI extends BorderPane {

    @FXML
    private TextField name;
    @FXML
    private ChoiceBox<Element> type;

    public static void show(Window window) {
        Stage stage = new Stage();
        stage.setScene(new Scene(new NewModElementUI()));
        stage.setTitle(I18n.translate("ui.new_mod_element.title"));
        stage.initOwner(window);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.showAndWait();
    }

    public NewModElementUI() {
        FXUtils.loadFXML(this, "ui/forge/NewModElement.fxml");

        type.setConverter(new StringConverter<Element>() {
            @Override
            public String toString(Element object) {
                return I18n.translate(object.getTranslationKey());
            }

            @Override
            public Element fromString(String string) {
                throw new UnsupportedOperationException();
            }
        });
        type.getItems().addAll(ForgeModService.getInstance().getElementManager().getElements());
        type.setValue(type.getItems().get(0));
    }

    @FXML
    private void onFinish() {

    }

    @FXML
    private void onCancel() {
        FXUtils.hideWindow(this);
    }
}
