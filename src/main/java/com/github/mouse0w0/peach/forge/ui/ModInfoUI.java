package com.github.mouse0w0.peach.forge.ui;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.forge.ForgeModInfo;
import com.github.mouse0w0.peach.ui.util.FXUtils;
import com.github.mouse0w0.peach.util.JsonFile;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.StringConverter;

import java.util.Collections;
import java.util.Locale;

public class ModInfoUI extends BorderPane {

    private final JsonFile<ForgeModInfo> file;

    @FXML
    public TextField name;
    @FXML
    public TextField id;
    @FXML
    public TextField version;
    @FXML
    public ChoiceBox<String> mcVersion;
    @FXML
    public TextField author;
    @FXML
    public ChoiceBox<Locale> language;
    @FXML
    public TextArea description;

    public static void show(JsonFile<ForgeModInfo> file, Window window) {
        ModInfoUI modInfo = new ModInfoUI(file);
        Stage stage = new Stage();
        stage.setScene(new Scene(modInfo));
        stage.setTitle(I18n.translate("ui.mod_info.title"));
        stage.initOwner(window);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.show();
    }

    public ModInfoUI(JsonFile<ForgeModInfo> file) {
        this.file = file;
        FXUtils.loadFXML(this, "ui/forge/ModInfo.fxml");

        mcVersion.getItems().add("1.12.2");
        mcVersion.setValue("1.12.2");

        FXUtils.disableTextAreaBlur(description);

        language.setConverter(new StringConverter<Locale>() {
            @Override
            public String toString(Locale object) {
                return object.getDisplayName() + " (" + object.toLanguageTag() + ")";
            }

            @Override
            public Locale fromString(String string) {
                throw new UnsupportedOperationException();
            }
        });
        language.getItems().addAll(Locale.getAvailableLocales());

        doLoad();
    }

    public void doLoad() {
        ForgeModInfo info = file.get();
        name.setText(info.getName());
        id.setText(info.getId());
        version.setText(info.getVersion());
        mcVersion.setValue(info.getMcVersion());
        description.setText(info.getDescription());
        author.setText(info.getFirstAuthor());
        language.setValue(info.getLanguage());
    }

    @FXML
    public void doSave() {
        FXUtils.hideWindow(this);
        ForgeModInfo info = file.get();
        info.setName(name.getText());
        info.setId(id.getText());
        info.setVersion(version.getText());
        info.setMcVersion(mcVersion.getValue());
        info.setDescription(description.getText());
        info.setAuthors(Collections.singletonList(author.getText()));
        info.setLanguage(language.getValue());
        file.save();
    }

    @FXML
    public void onCancel() {
        FXUtils.hideWindow(this);
    }
}
