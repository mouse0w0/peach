package com.github.mouse0w0.peach.service;

import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.component.PersistentComponent;
import com.github.mouse0w0.peach.event.project.ProjectEvent;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.util.JsonUtils;
import com.google.common.reflect.TypeToken;
import com.google.gson.JsonElement;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecentProjectsManager implements PersistentComponent {

    private final Map<String, RecentProjectInfo> recentProjects = new HashMap<>();

    public static RecentProjectsManager getInstance() {
        return Peach.getInstance().getService(RecentProjectsManager.class);
    }

    public RecentProjectsManager() {
        Peach.getEventBus().addListener(this::onOpenedProject);
        Peach.getEventBus().addListener(this::onClosedProject);
    }

    public Collection<RecentProjectInfo> getRecentProjects() {
        return recentProjects.values();
    }

    public void removeRecentProject(String path) {
        recentProjects.remove(path);
    }

    private void onOpenedProject(ProjectEvent.Opened event) {
        updateRecentProjectInfo(event.getProject());
    }

    private void onClosedProject(ProjectEvent.Closed event) {
        updateRecentProjectInfo(event.getProject());
    }

    private void updateRecentProjectInfo(Project project) {
        String path = project.getPath().toString();
        RecentProjectInfo info = recentProjects.computeIfAbsent(path, RecentProjectInfo::new);
        info.setName(project.getName());
        info.setLatestOpenTimestamp(System.currentTimeMillis());
    }

    @Nonnull
    @Override
    public String getStorageFile() {
        return "recentProjects.json";
    }

    @Override
    public JsonElement serialize() {
        return JsonUtils.toJson(recentProjects.values());
    }

    @Override
    public void deserialize(JsonElement jsonElement) {
        JsonUtils.fromJson(jsonElement, new TypeToken<List<RecentProjectInfo>>() {
        }).forEach(info -> recentProjects.put(info.getPath(), info));
    }
}
