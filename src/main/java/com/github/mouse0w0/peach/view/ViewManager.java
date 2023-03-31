package com.github.mouse0w0.peach.view;

import com.github.mouse0w0.peach.icon.IconManager;
import com.github.mouse0w0.peach.javafx.control.ViewPane;
import com.github.mouse0w0.peach.javafx.control.ViewTab;
import com.github.mouse0w0.peach.javafx.geometry.EightPos;
import com.github.mouse0w0.peach.l10n.AppL10n;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.window.ProjectWindow;
import javafx.scene.Node;
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
            throw new NullPointerException("window");
        }

        this.viewPane = window.getViewPane();

        for (ViewEP view : ViewEP.EXTENSION_POINT.getExtensions()) {
            String id = view.getId();
            if (id == null || id.isEmpty()) {
                LOGGER.error("The id of view is null or empty, skip initialize.");
                continue;
            }

            String text = AppL10n.localize("view." + id + ".text");
            Node icon = IconManager.getInstance().createNode(view.getIcon());
            Node content = view.getFactory().createViewContent(project);

            EightPos position = view.getPosition();
            if (position == null) {
                LOGGER.warn("The position of view \"{}\" is null, set to default \"LEFT_TOP\".", id);
                position = EightPos.LEFT_TOP;
            }

            ViewTab viewTab = new ViewTab(text, icon, content);
            viewTab.getProperties().put(ViewEP.class, view);

            viewTabMap.put(id, viewTab);

            viewPane.getViewGroup(position).getTabs().add(viewTab);
        }
    }
}
