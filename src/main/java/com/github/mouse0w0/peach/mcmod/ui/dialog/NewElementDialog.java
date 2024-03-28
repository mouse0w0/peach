package com.github.mouse0w0.peach.mcmod.ui.dialog;

import com.github.mouse0w0.peach.icon.AppIcon;
import com.github.mouse0w0.peach.l10n.AppL10n;
import com.github.mouse0w0.peach.mcmod.element.ElementManager;
import com.github.mouse0w0.peach.mcmod.element.ElementRegistry;
import com.github.mouse0w0.peach.mcmod.element.provider.ElementProvider;
import com.github.mouse0w0.peach.mcmod.util.IdentifierUtils;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.ui.control.ButtonType;
import com.github.mouse0w0.peach.ui.util.Validator;
import com.github.mouse0w0.peach.util.FileUtils;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.StringConverter;

import java.nio.file.Path;

public class NewElementDialog extends Stage {
    public static void show(Project project, Path path, Window window) {
        new NewElementDialog(project, path, window).showAndWait();
    }

    public NewElementDialog(Project project, Path path, Window owner) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(12));

        GridPane grid = new GridPane(12, 12);
        grid.getColumnConstraints().addAll(
                new ColumnConstraints(60, 60, Double.MAX_VALUE),
                new ColumnConstraints(180, 180, Double.MAX_VALUE)
        );
        root.setCenter(grid);

        grid.add(new Text(AppL10n.localize("dialog.newElement.name")), 0, 0);

        TextField name = new TextField();
        Validator.of(name, AppL10n.localize("validate.invalidFileName"), FileUtils::validateFileNameWithoutExtension);
        grid.add(name, 1, 0);

        grid.add(new Text(AppL10n.localize("dialog.newElement.type")), 0, 1);

        ComboBox<ElementProvider<?>> type = new ComboBox<>();
        type.setMaxWidth(10000);
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
        type.getSelectionModel().selectFirst();
        name.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
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
        grid.add(type, 1, 1);

        grid.add(new Text(AppL10n.localize("dialog.newElement.identifier")), 0, 2);

        Text identifier = new Text();
        identifier.textProperty().bind(name.textProperty().map(IdentifierUtils::tryConvertToIdentifier));
        grid.add(identifier, 1, 2);

        Button finishButton = ButtonType.FINISH.createButton();
        finishButton.setOnAction(event -> {
            if (!Validator.validate(name)) return;

            ElementManager.getInstance(project).createElement(path, type.getValue(), name.getText());

            hide();
        });

        Button cancelButton = ButtonType.CANCEL.createButton();
        cancelButton.setOnAction(event -> hide());

        ButtonBar buttonBar = new ButtonBar();
        buttonBar.setPadding(new Insets(12, 0, 0, 0));
        buttonBar.getButtons().addAll(finishButton, cancelButton);
        root.setBottom(buttonBar);

        setScene(new Scene(root));
        setTitle(AppL10n.localize("dialog.newElement.title"));
        getIcons().add(AppIcon.Peach.getImage());
        setResizable(false);
        initModality(Modality.APPLICATION_MODAL);
        initOwner(owner);
    }
}
