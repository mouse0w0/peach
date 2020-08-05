package com.github.mouse0w0.peach.service;

import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.event.AppEvent;
import com.github.mouse0w0.peach.event.project.ProjectEvent;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.util.JsonUtils;
import com.google.common.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecentProjectsManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecentProjectsManager.class);

    private static final Path RECENT_PROJECTS_FILE = Peach.getInstance().getUserPropertiesPath().resolve("recentProjects.json");

    private final Map<String, RecentProjectInfo> recentProjects = new HashMap<>();

    public static RecentProjectsManager getInstance() {
        return Peach.getInstance().getService(RecentProjectsManager.class);
    }

    public RecentProjectsManager() {
        Peach.getEventBus().addListener(this::onOpenedProject);
        Peach.getEventBus().addListener(this::onClosedProject);
        Peach.getEventBus().addListener(this::onAppClosing);

        try {
            JsonUtils.readJson(RECENT_PROJECTS_FILE, new TypeToken<List<RecentProjectInfo>>() {
            })
                    .forEach(info -> recentProjects.put(info.getPath(), info));
        } catch (IOException e) {
            LOGGER.debug("Cannot load recent projects.", e);
        }
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

    private void onAppClosing(AppEvent.Closing event) {
        try {
            JsonUtils.writeJson(RECENT_PROJECTS_FILE, recentProjects.values());
        } catch (IOException e) {
            LOGGER.debug("Cannot save recent projects.", e);
        }
    }

    private void updateRecentProjectInfo(Project project) {
        String path = project.getPath().toString();
        RecentProjectInfo info = recentProjects.computeIfAbsent(path, RecentProjectInfo::new);
        info.setName(project.getName());
        info.setLatestOpenTimestamp(System.currentTimeMillis());
    }
}
