package com.github.mouse0w0.peach.recentProject;

import com.github.mouse0w0.peach.action.Action;
import com.github.mouse0w0.peach.action.ActionEvent;
import com.github.mouse0w0.peach.action.ActionGroup;
import com.github.mouse0w0.peach.data.DataKeys;
import com.github.mouse0w0.peach.project.ProjectManager;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public final class RecentProjectsGroup extends ActionGroup {
    @Override
    public List<Action> getChildren(@Nullable ActionEvent event) {
        String currentProjectPath = null;
        if (event != null) {
            currentProjectPath = DataKeys.PROJECT.get(event).getPath().toString();
        }
        List<Action> children = new ArrayList<>();
        for (RecentProjectInfo recentProject : RecentProjectsManager.getInstance().getRecentProjects()) {
            if (!recentProject.getPath().equals(currentProjectPath)) {
                children.add(new OpenAction(recentProject.getPath(), recentProject.getName()));
            }
        }
        return children;
    }

    public static final class OpenAction extends Action {
        private final String path;

        public OpenAction(String path, String name) {
            this.path = path;
            setText(name);
        }

        @Override
        public void perform(ActionEvent event) {
            ProjectManager.getInstance().openProject(Paths.get(path));
        }
    }
}
