package com.github.mouse0w0.peach.mcmod.wizard.step;

import com.github.mouse0w0.peach.dialog.Alert;
import com.github.mouse0w0.peach.mcmod.element.impl.ItemGroup;
import com.github.mouse0w0.peach.mcmod.ui.control.ItemPicker;
import com.github.mouse0w0.peach.mcmod.ui.control.ItemView;
import com.github.mouse0w0.peach.mcmod.util.ModUtils;
import com.github.mouse0w0.peach.mcmod.util.ResourceUtils;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.project.service.FileChooserHelper;
import com.github.mouse0w0.peach.ui.control.FilePicker;
import com.github.mouse0w0.peach.ui.util.FXUtils;
import com.github.mouse0w0.peach.util.FileUtils;
import com.github.mouse0w0.peach.wizard.WizardStep;
import com.google.common.base.Strings;
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

import java.io.IOException;
import java.nio.file.Path;

import static com.github.mouse0w0.peach.mcmod.util.ResourceUtils.GUI_TEXTURES;
import static com.github.mouse0w0.peach.mcmod.util.ResourceUtils.TEXTURES;

public class ItemGroupStep extends FlowPane implements WizardStep {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemGroupStep.class);

    private final Project project;
    private final ItemGroup element;

    @FXML
    private GridPane content;

    @FXML
    private TextField registerName;
    @FXML
    private TextField displayName;
    @FXML
    private RadioButton hasSearchBar;
    @FXML
    private FilePicker background;

    private ItemView icon;

    public ItemGroupStep(Project project, ItemGroup element) {
        this.project = project;
        this.element = element;

        FXUtils.loadFXML(this, "ui/mcmod/ItemGroup.fxml");

        icon = new ItemPicker(32, 32);
        FXUtils.setFixedSize(icon, 32, 32);
        icon.setBackground(new Background(new BackgroundFill(Color.gray(0.8), null, null)));
        content.add(icon, 1, 4);

        FileChooserHelper.getInstance().register(background, "mcmod.itemGroupBackground");
    }

    @Override
    public Node getContent() {
        return this;
    }

    @Override
    public void init() {
        registerName.setText(Strings.isNullOrEmpty(element.getRegisterName()) ?
                ModUtils.toRegisterName(element.getFileName()) : element.getRegisterName());
        displayName.setText(Strings.isNullOrEmpty(element.getDisplayName()) ?
                element.getFileName() : element.getDisplayName());
        hasSearchBar.setSelected(element.isHasSearchBar());
        background.setText(element.getBackground());
        icon.setItem(element.getIcon());
    }

    @Override
    public boolean validate() {
        return handleBackground();
    }

    private boolean handleBackground() {
        Path file = background.toPath();
        if (file == null || !file.isAbsolute()) return true;

        Path textures = ResourceUtils.getResourcePath(project, TEXTURES);
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
        return ResourceUtils.getResourcePath(project, GUI_TEXTURES).resolve(FileUtils.getFileName(source));
    }

    @Override
    public void updateDataModel() {
        element.setRegisterName(registerName.getText());
        element.setDisplayName(displayName.getText());
        element.setHasSearchBar(hasSearchBar.isSelected());
        element.setBackground(background.getText());
        element.setIcon(icon.getItem());
    }

    @Override
    public void dispose() {

    }
}
