package com.github.mouse0w0.peach.ui.forge;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.forge.ForgeProjectInfo;
import com.github.mouse0w0.peach.ui.util.FXUtils;
import com.github.mouse0w0.peach.util.JsonFile;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.StringConverter;

import java.util.Locale;

public class ModInfoUI extends BorderPane {

    private final JsonFile<ForgeProjectInfo> file;

    @FXML
    public TextField name;
    @FXML
    public TextField id;
    @FXML
    public TextField version;
    @FXML
    public ChoiceBox<String> mcVersion;
    @FXML
    public ChoiceBox<Locale> language;

    public static void show(JsonFile<ForgeProjectInfo> file, Window window) {
        ModInfoUI modInfo = new ModInfoUI(file);
        Stage stage = new Stage();
        stage.setScene(new Scene(modInfo));
        stage.setTitle(I18n.translate("ui.mod_info.title"));
        stage.initOwner(window);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.showAndWait();
    }

    public ModInfoUI(JsonFile<ForgeProjectInfo> file) {
        this.file = file;
        FXUtils.loadFXML(this, "ui/forge/ModInfo.fxml");

        language.setConverter(new StringConverter<Locale>() {
            @Override
            public String toString(Locale object) {
                return object.getDisplayLanguage() + " (" + object.toLanguageTag() + ")";
            }

            @Override
            public Locale fromString(String string) {
                return Locale.forLanguageTag(string.substring(string.indexOf('(') - 1, string.indexOf(')')));
            }
        });
        language.getItems().addAll(Locale.getAvailableLocales());

        doLoad();
    }

    public void doLoad() {
        ForgeProjectInfo info = file.get();
        name.setText(info.getName());
        id.setText(info.getId());
        version.setText(info.getVersion());
        mcVersion.setValue(info.getMcVersion());
        language.setValue(info.getLanguage());
    }

    @FXML
    public void doSave() {
        FXUtils.hideWindow(this);
        ForgeProjectInfo info = file.get();
        info.setName(name.getText());
        info.setId(id.getText());
        info.setVersion(version.getText());
        info.setMcVersion(mcVersion.getValue());
        info.setLanguage(language.getValue());
        file.save();
    }

    @FXML
    public void onCancel() {
        FXUtils.hideWindow(this);
    }
}
