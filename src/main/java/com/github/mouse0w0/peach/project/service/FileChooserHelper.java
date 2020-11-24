package com.github.mouse0w0.peach.project.service;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.component.PersistentComponent;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.util.JsonUtils;
import com.google.common.reflect.TypeToken;
import com.google.gson.JsonElement;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileChooserHelper implements PersistentComponent {

    private final Map<String, File> initialDirectories = new HashMap<>();

    public static FileChooserHelper getInstance() {
        return Peach.getInstance().getService(FileChooserHelper.class);
    }

    public static FileChooserHelper getInstance(Project project) {
        return project.getService(FileChooserHelper.class);
    }

    public File open(Window owner, String id, File initialDirectory, FileChooser.ExtensionFilter... filters) {
        Validate.notNull(id);
        FileChooser fileChooser = createFileChooser(id, initialDirectory, filters);
        File file = fileChooser.showOpenDialog(owner);
        if (file != null) {
            initialDirectories.put(id, file.getParentFile());
        }
        return file;
    }

    public List<File> openMultiple(Window owner, String id, File initialDirectory, FileChooser.ExtensionFilter... filters) {
        Validate.notNull(id);
        FileChooser fileChooser = createFileChooser(id, initialDirectory, filters);
        List<File> file = fileChooser.showOpenMultipleDialog(owner);
        if (file != null) {
            initialDirectories.put(id, file.get(0).getParentFile());
        }
        return file;
    }

    public File save(Window owner, String id, File initialDirectory, FileChooser.ExtensionFilter... filters) {
        Validate.notNull(id);
        FileChooser fileChooser = createFileChooser(id, initialDirectory, filters);
        File file = fileChooser.showSaveDialog(owner);
        if (file != null) {
            initialDirectories.put(id, file.getParentFile());
        }
        return file;
    }

    private FileChooser createFileChooser(String id, File initialDirectory, FileChooser.ExtensionFilter[] filters) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(I18n.translate("fileChooser." + id + ".title"));
        fileChooser.setInitialDirectory(initialDirectories.getOrDefault(id, initialDirectory));
        fileChooser.getExtensionFilters().addAll(filters);
        return fileChooser;
    }

    public File openDirectory(String id) {
        return openDirectory(null, id, null);
    }

    public File openDirectory(Window owner, String id, File initialDirectory) {
        Validate.notNull(id);
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(I18n.translate("fileChooser." + id + ".title"));
        directoryChooser.setInitialDirectory(initialDirectories.getOrDefault(id, initialDirectory));
        File file = directoryChooser.showDialog(owner);
        if (file != null) {
            initialDirectories.put(id, file.getParentFile());
        }
        return file;
    }

    @Nonnull
    @Override
    public String getStorageFile() {
        return "fileChooserState.json";
    }

    @Override
    public JsonElement serialize() {
        return JsonUtils.toJson(initialDirectories);
    }

    @Override
    public void deserialize(JsonElement jsonElement) {
        initialDirectories.putAll(JsonUtils.fromJson(jsonElement, new TypeToken<Map<String, File>>() {
        }));
    }
}
