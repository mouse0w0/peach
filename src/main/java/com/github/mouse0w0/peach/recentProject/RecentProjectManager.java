package com.github.mouse0w0.peach.recentProject;

import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.project.ProjectLifecycleListener;
import com.github.mouse0w0.peach.service.PersistentService;
import com.github.mouse0w0.peach.service.Storage;
import com.github.mouse0w0.peach.util.JsonUtils;
import com.google.gson.JsonElement;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.reflect.TypeUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Storage("recentProjects.json")
public class RecentProjectManager implements PersistentService {

    private final Map<String, RecentProjectInfo> recentProjects = new HashMap<>();

    public static RecentProjectManager getInstance() {
        return Peach.getInstance().getService(RecentProjectManager.class);
    }

    public RecentProjectManager() {
    }

    public Collection<RecentProjectInfo> getRecentProjects() {
        return recentProjects.values();
    }

    public void removeRecentProject(String path) {
        recentProjects.remove(path);
    }

    private void updateRecentProjectInfo(Project project) {
        String path = project.getPath().toString();
        RecentProjectInfo info = recentProjects.computeIfAbsent(path, RecentProjectInfo::new);
        info.setName(project.getName());
        info.setLatestOpenTimestamp(System.currentTimeMillis());
    }

    @Override
    public JsonElement saveState() {
        return JsonUtils.toJson(recentProjects.values());
    }

    @Override
    public void loadState(JsonElement state) {
        JsonUtils.<List<RecentProjectInfo>>fromJson(state, TypeUtils.parameterize(List.class, RecentProjectInfo.class))
                .forEach(info -> recentProjects.put(info.getPath(), info));
    }

    public static final class Listener implements ProjectLifecycleListener {
        @Override
        public void projectOpened(Project project) {
            getInstance().updateRecentProjectInfo(project);
        }

        @Override
        public void canCloseProject(Project project, MutableBoolean cancelled) {

        }

        @Override
        public void projectClosingBeforeSave(Project project) {

        }

        @Override
        public void projectClosing(Project project) {

        }

        @Override
        public void projectClosed(Project project) {
            getInstance().updateRecentProjectInfo(project);
        }
    }
}
