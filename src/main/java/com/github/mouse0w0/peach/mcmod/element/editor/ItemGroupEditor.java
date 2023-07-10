package com.github.mouse0w0.peach.mcmod.element.editor;

import com.github.mouse0w0.peach.javafx.control.FilePicker;
import com.github.mouse0w0.peach.javafx.util.ExtensionFilters;
import com.github.mouse0w0.peach.javafx.util.FXUtils;
import com.github.mouse0w0.peach.javafx.util.Validator;
import com.github.mouse0w0.peach.l10n.AppL10n;
import com.github.mouse0w0.peach.mcmod.element.impl.MEItemGroup;
import com.github.mouse0w0.peach.mcmod.ui.control.ItemPicker;
import com.github.mouse0w0.peach.mcmod.ui.control.ItemView;
import com.github.mouse0w0.peach.mcmod.util.ModUtils;
import com.github.mouse0w0.peach.mcmod.util.ResourceStore;
import com.github.mouse0w0.peach.mcmod.util.ResourceUtils;
import com.github.mouse0w0.peach.project.Project;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class ItemGroupEditor extends ElementEditor<MEItemGroup> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemGroupEditor.class);

    private final ResourceStore backgroundStore;

    @FXML
    private GridPane grid;

    @FXML
    private TextField identifier;
    @FXML
    private TextField displayName;
    @FXML
    private RadioButton hasSearchBar;

    private FilePicker background;
    private ItemView icon;

    public ItemGroupEditor(@NotNull Project project, @NotNull MEItemGroup element) {
        super(project, element);
        this.backgroundStore = new ResourceStore(
                ResourceUtils.getResourcePath(project, ResourceUtils.TEXTURES),
                ResourceUtils.getResourcePath(project, ResourceUtils.GUI_TEXTURES));
    }

    @Override
    protected Node getContent() {
        FlowPane root = FXUtils.loadFXML(null, this, "ui/mcmod/ItemGroup.fxml", AppL10n.getResourceBundle());

        Validator.register(identifier, AppL10n.localize("validate.invalidIdentifier"), ModUtils::validateIdentifier);

        background = new FilePicker();
        background.getExtensionFilters().add(ExtensionFilters.PNG);
        background.setConverter(new StringConverter<>() {
            @Override
            public String toString(File object) {
                File result = backgroundStore.store(object);
                return result != null ? backgroundStore.toRelative(result) : background.getValue();
            }

            @Override
            public File fromString(String string) {
                return backgroundStore.toAbsoluteFile(string);
            }
        });
        grid.add(background, 1, 3);

        icon = new ItemPicker(32, 32);
        FXUtils.setFixedSize(icon, 32, 32);
        icon.setBackground(new Background(new BackgroundFill(Color.gray(0.8), null, null)));
        grid.add(icon, 1, 4);

        return root;
    }

    @Override
    protected void initialize(MEItemGroup element) {
        identifier.setText(element.getIdentifier());
        displayName.setText(element.getDisplayName());
        hasSearchBar.setSelected(element.isHasSearchBar());
        background.setValue(element.getBackground());
        icon.setItem(element.getIcon());
    }

    @Override
    protected void updateDataModel(MEItemGroup element) {
        element.setIdentifier(identifier.getText().trim());
        element.setDisplayName(displayName.getText());
        element.setHasSearchBar(hasSearchBar.isSelected());
        element.setBackground(background.getValue());
        element.setIcon(icon.getItem());
    }

    @Override
    protected boolean validate() {
        return Validator.validate(identifier);
    }
}
