package com.github.mouse0w0.peach.mcmod.element;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.dialog.Alert;
import com.github.mouse0w0.peach.fileEditor.FileEditorManager;
import com.github.mouse0w0.peach.mcmod.event.ElementEvent;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.util.FileUtils;
import com.github.mouse0w0.peach.util.JsonUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.LinkedHashSet;

public final class ElementManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElementManager.class);

    private final Project project;
    private final ElementRegistry elementRegistry;

    private final Path path;

    private final ObservableSet<Path> elements = FXCollections.observableSet(new LinkedHashSet<>());
    private final ObservableSet<Path> unmodifiableElements = FXCollections.unmodifiableObservableSet(elements);

    private ElementView elementView;

    public static ElementManager getInstance(Project project) {
        return project.getService(ElementManager.class);
    }

    public ElementManager(Project project, ElementRegistry elementRegistry) {
        this.project = project;
        this.elementRegistry = elementRegistry;
        this.path = project.getPath().resolve("sources");
        init();
    }

    private void init() {
        try {
            FileUtils.createDirectoriesIfNotExists(path);
            Iterator<Path> iterator = Files.walk(path).iterator();
            while (iterator.hasNext()) {
                Path file = iterator.next();
                ElementType<?> elementType = elementRegistry.getElementType(file);
                if (elementType != null) elements.add(file);
            }
        } catch (IOException e) {
            LOGGER.error("Failed to load elements.", e);
        }
    }

    public Project getProject() {
        return project;
    }

    public ObservableSet<Path> getElements() {
        return unmodifiableElements;
    }

    @SuppressWarnings("unchecked")
    public <T extends Element> T loadElement(Path file) {
        ElementType<?> type = elementRegistry.getElementType(file);
        if (type == null) {
            throw new IllegalArgumentException("Cannot load element");
        }
        try {
            Element element = JsonUtils.readJson(file, type.getType());
            Element.setFile(element, file);
            return (T) element;
        } catch (IOException e) {
            return (T) type.createElement(file);
        }
    }

    public void saveElement(Element element) {
        if (elements.add(element.getFile())) {
            Peach.getEventBus().post(new ElementEvent.Created(project, element));
        }

        try {
            JsonUtils.writeJson(element.getFile(), element);
        } catch (IOException e) {
            LOGGER.warn("Failed to save element.", e);
            //TODO: show dialog
        }

        Peach.getEventBus().post(new ElementEvent.Updated(project, element));
    }

    public void removeElement(Path file) {
        if (!elements.remove(file)) {
            LOGGER.warn("Try to remove an unmanaged element {}.", file);
            return;
        }

        try {
            Files.deleteIfExists(file);
        } catch (IOException e) {
            LOGGER.warn("Failed to delete element file.", e);
        }

        Peach.getEventBus().post(new ElementEvent.Deleted(project, file));
    }

    public void createAndEditElement(ElementType<?> type, String name) {
        Path file = path.resolve(name + "." + type.getName() + ".json");

        if (Files.exists(file)) {
            Alert.error(I18n.format("validate.existsFile", file.getFileName()));
            return;
        }

        saveElement(type.createElement(file));

        FileEditorManager.getInstance(project).open(file);
    }

    public ElementView getElementView() {
        if (elementView == null) {
            elementView = new ElementView(this);
        }
        return elementView;
    }
}
