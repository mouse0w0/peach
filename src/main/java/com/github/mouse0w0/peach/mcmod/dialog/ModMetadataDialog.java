package com.github.mouse0w0.peach.mcmod.dialog;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.javafx.FXUtils;
import com.github.mouse0w0.peach.javafx.Validator;
import com.github.mouse0w0.peach.mcmod.project.McModDescriptor;
import com.github.mouse0w0.peach.mcmod.project.McModMetadata;
import com.github.mouse0w0.peach.mcmod.util.ModUtils;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.StringConverter;

import java.util.Collections;
import java.util.Locale;

public class ModMetadataDialog extends BorderPane {

    private final McModDescriptor descriptor;

    @FXML
    public Accordion accordion;
    @FXML
    public TitledPane general;

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

    public static void show(McModDescriptor descriptor, Window window) {
        ModMetadataDialog modInfo = new ModMetadataDialog(descriptor);
        Stage stage = new Stage();
        stage.setScene(new Scene(modInfo));
        stage.setTitle(I18n.translate("dialog.modMetadata.title"));
        stage.initOwner(window);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    public ModMetadataDialog(McModDescriptor descriptor) {
        this.descriptor = descriptor;
        FXUtils.loadFXML(this, "ui/mcmod/ModMetadata.fxml");

        Validator.error(id, ModUtils::isValidIdentifier, I18n.translate("validate.illegalModId"));

        accordion.setExpandedPane(general);

        mcVersion.getItems().add("1.12.2");
        mcVersion.setValue("1.12.2");

        FXUtils.fixTextAreaBlur(description);

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
        McModMetadata metadata = descriptor.getMetadata();
        name.setText(metadata.getName());
        id.setText(metadata.getId());
        version.setText(metadata.getVersion());
        mcVersion.setValue(metadata.getMcVersion());
        description.setText(metadata.getDescription());
        author.setText(metadata.getFirstAuthor());
        language.setValue(metadata.getLanguage());
    }

    @FXML
    public void doSave() {
        if (Validator.test(id)) {
            FXUtils.hideWindow(this);
            McModMetadata metadata = descriptor.getMetadata();
            metadata.setName(name.getText());
            metadata.setId(id.getText());
            metadata.setVersion(version.getText());
            metadata.setMcVersion(mcVersion.getValue());
            metadata.setDescription(description.getText());
            metadata.setAuthors(Collections.singletonList(author.getText()));
            metadata.setLanguage(language.getValue());
            descriptor.saveMetadata();
        }
    }

    @FXML
    public void onCancel() {
        FXUtils.hideWindow(this);
    }
}
