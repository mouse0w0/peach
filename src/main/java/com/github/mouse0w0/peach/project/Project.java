package com.github.mouse0w0.peach.project;

import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.component.ComponentManagerImpl;
import com.github.mouse0w0.peach.component.ServiceDescriptor;
import com.github.mouse0w0.peach.ui.project.ProjectWindow;
import com.github.mouse0w0.peach.ui.project.WindowManager;
import com.github.mouse0w0.peach.util.FileUtils;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Project extends ComponentManagerImpl {

    public static final String STORE_FOLDER = ".peach";

    private static final String NAME_FILE = ".name";

    private final Path path;
    private final Path nameFile;

    private String name;

    public Project(@Nonnull Path path) throws IOException {
        super(Peach.getInstance());
        this.path = Validate.notNull(path);
        if (!Files.exists(path)) {
            throw new IllegalStateException("Project path must be exists");
        }
        if (!Files.isDirectory(path)) {
            throw new IllegalArgumentException("Project path must be a directory");
        }
        this.nameFile = path.resolve(STORE_FOLDER + "/" + NAME_FILE);
        loadNameFile();
        initServices(ServiceDescriptor.PROJECT_SERVICE.getExtensions());
    }

    @Nonnull
    public Path getPath() {
        return path;
    }

    @Nonnull
    public String getName() {
        if (name == null) {
            return path.getFileName().toString();
        }
        return name;
    }

    public void setName(@Nullable String name) {
        this.name = name;

        ProjectWindow window = WindowManager.getInstance().getWindow(this);
        if (window != null) {
            window.getStage().setTitle(getName());
        }
    }

    public boolean isOpen() {
        return ProjectManager.getInstance().isProjectOpened(this);
    }

    private void loadNameFile() throws IOException {
        if (Files.exists(nameFile)) {
            name = Files.readAllLines(nameFile).stream().findFirst().orElse(null);
        }
    }

    public void save() throws IOException {
        saveNameFile();
        saveComponents();
    }

    private void saveNameFile() throws IOException {
        if (name == null || name.equals(path.getFileName().toString())) {
            Files.deleteIfExists(nameFile);
        } else if (name != null) {
            FileUtils.createFileIfNotExists(nameFile);
            Files.write(nameFile, name.getBytes(StandardCharsets.UTF_8));
        }
    }

    @Override
    public void dispose() {
        disposeComponents();
    }
}
