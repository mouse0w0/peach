package com.github.mouse0w0.peach.mcmod.element.editor;

import com.github.mouse0w0.peach.javafx.FXUtils;
import com.github.mouse0w0.peach.mcmod.element.impl.MEItemGroup;
import com.github.mouse0w0.peach.mcmod.ui.control.ItemPicker;
import com.github.mouse0w0.peach.mcmod.ui.control.ItemView;
import com.github.mouse0w0.peach.mcmod.ui.control.ResourcePicker;
import com.github.mouse0w0.peach.mcmod.util.ResourceUtils;
import com.github.mouse0w0.peach.project.Project;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.nio.file.Path;

import static com.github.mouse0w0.peach.mcmod.util.ResourceUtils.TEXTURES;

public class ItemGroupEditor extends ElementEditor<MEItemGroup> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemGroupEditor.class);

    @FXML
    private GridPane grid;

    @FXML
    private TextField identifier;
    @FXML
    private TextField displayName;
    @FXML
    private RadioButton hasSearchBar;

    private ResourcePicker background;
    private ItemView icon;

    public ItemGroupEditor(@Nonnull Project project, @Nonnull MEItemGroup element) {
        super(project, element);
    }

    @Override
    protected Node getContent() {
        FlowPane root = FXUtils.loadFXML(null, this, "ui/mcmod/ItemGroup.fxml");

        Path texturesPath = ResourceUtils.getResourcePath(getProject(), TEXTURES);
        background = new ResourcePicker(texturesPath, "png", "PNG File (*.png)");
        background.defaultFilePathProperty().bind(
                Bindings.createObjectBinding(() -> texturesPath.resolve("gui/" + identifier.getText().trim() + ".png"), identifier.textProperty()));
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
        background.setResourcePath(element.getBackground());
        icon.setItem(element.getIcon());
    }

    @Override
    protected void updateDataModel(MEItemGroup element) {
        element.setIdentifier(identifier.getText().trim());
        element.setDisplayName(displayName.getText());
        element.setHasSearchBar(hasSearchBar.isSelected());
        element.setBackground(background.getResourcePath());
        element.setIcon(icon.getItem());
    }
}
