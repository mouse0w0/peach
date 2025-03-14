package com.github.mouse0w0.peach.fileChooser;

import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.service.PersistentService;
import com.github.mouse0w0.peach.service.Storage;
import com.github.mouse0w0.peach.ui.control.FilePicker;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.StringProperty;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Storage("fileChooserState.json")
public class FileChooserHelper implements PersistentService {

    public static final String FILE_CHOOSER_ID = "FileChooserId";

    private final Map<String, File> lastInitialDirectories = new HashMap<>();

    private final InvalidationListener textInvalidationListener = this::onTextInvalidate;

    public static FileChooserHelper getInstance() {
        return Peach.getInstance().getService(FileChooserHelper.class);
    }

    public static FileChooserHelper getInstance(Project project) {
        return project.getService(FileChooserHelper.class);
    }

    public void register(FilePicker filePicker, String id) {
        filePicker.valueProperty().addListener(textInvalidationListener);
        filePicker.getProperties().put(FILE_CHOOSER_ID, id);
        File initialDirectory = lastInitialDirectories.get(id);
        if (initialDirectory != null) {
            filePicker.setInitialDirectory(initialDirectory);
        }
    }

    private void onTextInvalidate(Observable observable) {
        StringProperty textProperty = (StringProperty) observable;
        FilePicker filePicker = (FilePicker) textProperty.getBean();
        File file = filePicker.getFile();
        if (file != null) {
            String id = (String) filePicker.getProperties().get(FILE_CHOOSER_ID);
            File parentFile = file.getParentFile();
            lastInitialDirectories.put(id, parentFile);
            filePicker.setInitialDirectory(parentFile);
        }
    }

    public File open(Window owner, String id, String title, File initialDirectory, FileChooser.ExtensionFilter... filters) {
        Objects.requireNonNull(id);
        FileChooser fileChooser = createFileChooser(id, title, initialDirectory, filters);
        File file = fileChooser.showOpenDialog(owner);
        if (file != null) {
            lastInitialDirectories.put(id, file.getParentFile());
        }
        return file;
    }

    public List<File> openMultiple(Window owner, String title, String id, File initialDirectory, FileChooser.ExtensionFilter... filters) {
        Objects.requireNonNull(id);
        FileChooser fileChooser = createFileChooser(id, title, initialDirectory, filters);
        List<File> file = fileChooser.showOpenMultipleDialog(owner);
        if (file != null) {
            lastInitialDirectories.put(id, file.get(0).getParentFile());
        }
        return file;
    }

    public File save(Window owner, String id, String title, File initialDirectory, String initialFileName, FileChooser.ExtensionFilter... filters) {
        Objects.requireNonNull(id);
        FileChooser fileChooser = createFileChooser(id, title, initialDirectory, filters);
        fileChooser.setInitialFileName(initialFileName);
        File file = fileChooser.showSaveDialog(owner);
        if (file != null) {
            lastInitialDirectories.put(id, file.getParentFile());
        }
        return file;
    }

    private FileChooser createFileChooser(String id, String title, File initialDirectory, FileChooser.ExtensionFilter[] filters) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        File realInitialDirectory = lastInitialDirectories.getOrDefault(id, initialDirectory);
        if (realInitialDirectory != null && realInitialDirectory.exists()) {
            fileChooser.setInitialDirectory(realInitialDirectory);
        }
        fileChooser.getExtensionFilters().addAll(filters);
        return fileChooser;
    }

    public File openDirectory(Window owner, String id, String title, File initialDirectory) {
        Objects.requireNonNull(id);
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(title);
        File realInitialDirectory = lastInitialDirectories.getOrDefault(id, initialDirectory);
        if (realInitialDirectory != null && realInitialDirectory.exists()) {
            directoryChooser.setInitialDirectory(realInitialDirectory);
        }
        File file = directoryChooser.showDialog(owner);
        if (file != null) {
            lastInitialDirectories.put(id, file.getParentFile());
        }
        return file;
    }

    @Override
    public JsonElement saveState() {
        JsonObject object = new JsonObject();
        lastInitialDirectories.forEach((k, v) -> object.addProperty(k, v.getAbsolutePath()));
        return object;
    }

    @Override
    public void loadState(JsonElement state) {
        JsonObject object = state.getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
            lastInitialDirectories.put(entry.getKey(), new File(entry.getValue().getAsString()));
        }
    }
}
