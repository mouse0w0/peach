package com.github.mouse0w0.peach.mcmod.element;

import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.ui.project.WindowManager;
import com.github.mouse0w0.peach.util.FileUtils;
import com.github.mouse0w0.peach.wizard.Wizard;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.scene.control.Tab;
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

    private final ObservableSet<Element<?>> elements = FXCollections.observableSet(new LinkedHashSet<>());
    private final ObservableSet<Element<?>> unmodifiableElements = FXCollections.unmodifiableObservableSet(elements);

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
                if (elementType != null) {
                    elements.add(elementType.createElement(file));
                }
            }
        } catch (IOException e) {
            LOGGER.error("Failed to load elements.", e);
        }
    }

    public Project getProject() {
        return project;
    }

    public ObservableSet<Element<?>> getElements() {
        return unmodifiableElements;
    }

    public void saveElement(Element<?> element) {
        elements.add(element);
        element.save();
    }

    public void removeElement(Element<?> element) {
        if (!elements.remove(element)) return;

        try {
            Files.deleteIfExists(element.getFile());
        } catch (IOException e) {
            LOGGER.warn("Failed to delete element.", e);
        }
    }

    public void createElement(ElementType<?> type, String name) {
        editElement(type.createElement(path.resolve(name + "." + type.getName() + ".json")));
    }

    public void editElement(Element<?> element) {
        Wizard wizard = element.getType().createWizard(project, element);
        Tab tab = Wizard.createTab(wizard);
        WindowManager.getInstance().getWindow(project).openTab(tab);
    }

    public ElementView getElementView() {
        if (elementView == null) {
            elementView = new ElementView(this);
        }
        return elementView;
    }
}
