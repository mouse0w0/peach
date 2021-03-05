package com.github.mouse0w0.peach.mcmod.element.editor;

import com.github.mouse0w0.peach.dialog.Alert;
import com.github.mouse0w0.peach.javafx.FXUtils;
import com.github.mouse0w0.peach.javafx.control.FilePicker;
import com.github.mouse0w0.peach.mcmod.element.impl.MEItemGroup;
import com.github.mouse0w0.peach.mcmod.ui.control.ItemPicker;
import com.github.mouse0w0.peach.mcmod.ui.control.ItemView;
import com.github.mouse0w0.peach.mcmod.util.ResourceUtils;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.project.service.FileChooserHelper;
import com.github.mouse0w0.peach.util.FileUtils;
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
import java.io.IOException;
import java.nio.file.Path;

import static com.github.mouse0w0.peach.mcmod.util.ResourceUtils.GUI_TEXTURES;
import static com.github.mouse0w0.peach.mcmod.util.ResourceUtils.TEXTURES;

public class ItemGroupEditor extends ElementEditor<MEItemGroup> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemGroupEditor.class);

    @FXML
    private GridPane content;

    @FXML
    private TextField identifier;
    @FXML
    private TextField displayName;
    @FXML
    private RadioButton hasSearchBar;
    @FXML
    private FilePicker background;

    private ItemView icon;

    public ItemGroupEditor(@Nonnull Project project, @Nonnull MEItemGroup element) {
        super(project, element);
    }

    @Override
    protected Node getContent() {
        FlowPane root = FXUtils.loadFXML(null, this, "ui/mcmod/ItemGroup.fxml");

        icon = new ItemPicker(32, 32);
        FXUtils.setFixedSize(icon, 32, 32);
        icon.setBackground(new Background(new BackgroundFill(Color.gray(0.8), null, null)));
        content.add(icon, 1, 4);

        FileChooserHelper.getInstance().register(background, "mcmod.itemGroupBackground");
        return root;
    }

    @Override
    protected void initialize(MEItemGroup element) {
        identifier.setText(element.getIdentifier());
        displayName.setText(element.getDisplayName());
        hasSearchBar.setSelected(element.isHasSearchBar());
        background.setText(element.getBackground());
        icon.setItem(element.getIcon());
    }

    @Override
    protected void updateDataModel(MEItemGroup element) {
        element.setIdentifier(identifier.getText());
        element.setDisplayName(displayName.getText());
        element.setHasSearchBar(hasSearchBar.isSelected());
        element.setBackground(background.getText());
        element.setIcon(icon.getItem());
    }

    @Override
    protected boolean validate() {
        return handleBackground();
    }

    private boolean handleBackground() {
        Path file = background.toPath();
        if (file == null || !file.isAbsolute()) return true;

        Path textures = ResourceUtils.getResourcePath(getProject(), TEXTURES);
        if (file.startsWith(textures)) {
            background.setText(ResourceUtils.relativize(textures, file));
        } else {
            try {
                Path newFile = ResourceUtils.copyToLowerCaseFile(file, resolveTextureFile(file));
                if (newFile == null) return false;
                background.setText(ResourceUtils.relativize(textures, newFile));
            } catch (IOException e) {
                LOGGER.error("Failed to copy file because an exception has occurred.", e);
                Alert.error("An exception has occurred!");
            }
        }
        return true;
    }

    private Path resolveTextureFile(Path source) {
        return ResourceUtils.getResourcePath(getProject(), GUI_TEXTURES).resolve(FileUtils.getFileName(source));
    }
}
