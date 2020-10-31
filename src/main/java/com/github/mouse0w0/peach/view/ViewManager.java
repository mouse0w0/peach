package com.github.mouse0w0.peach.view;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.ui.icon.IconManager;
import com.github.mouse0w0.peach.ui.project.ProjectWindow;
import com.github.mouse0w0.viewpane.ViewPane;
import com.github.mouse0w0.viewpane.ViewTab;
import com.github.mouse0w0.viewpane.geometry.EightPos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ViewManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ViewManager.class);

    private final Project project;

    private ViewPane viewPane;

    public final Map<String, ViewTab> viewTabMap = new HashMap<>();

    public static ViewManager getInstance(Project project) {
        return project.getService(ViewManager.class);
    }

    public ViewManager(Project project) {
        this.project = project;
    }

    public Project getProject() {
        return project;
    }

    public ViewPane getViewPane() {
        return viewPane;
    }

    public ViewTab getViewTab(String id) {
        return viewTabMap.get(id);
    }

    public void initialize(ProjectWindow window) {
        if (viewPane != null) return;

        if (window == null) {
            throw new IllegalStateException("Cannot initialize ViewManager because window is not initialized");
        }

        this.viewPane = window.getViewPane();

        for (ViewDescriptor view : ViewDescriptor.EXTENSION_POINT.getExtensions()) {
            String id = view.id;
            if (id == null || id.isEmpty()) {
                LOGGER.error("The id of view is null or empty, skip initialize.");
                continue;
            }

            String text = I18n.translate("view." + id + ".text");
            Image icon = IconManager.getInstance().getImage(view.icon);
            Node content = view.factory.create();

            EightPos position = view.position;
            if (position == null) {
                LOGGER.warn("The position of view \"{}\" is null, set to default \"LEFT_TOP\".", id);
                position = EightPos.LEFT_TOP;
            }

            ViewTab viewTab = new ViewTab(text, new ImageView(icon), content);
            viewTab.getProperties().put(ViewDescriptor.class, view);

            viewTabMap.put(id, viewTab);

            viewPane.getViewGroup(position).getTabs().add(viewTab);
        }
    }
}
