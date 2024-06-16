package com.github.mouse0w0.peach.recentProject;

import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.dispose.Disposable;
import com.github.mouse0w0.peach.message.MessageBus;
import com.github.mouse0w0.peach.message.MessageBusConnection;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.project.ProjectLifecycleListener;
import com.github.mouse0w0.peach.service.PersistentService;
import com.github.mouse0w0.peach.service.Storage;
import com.github.mouse0w0.peach.util.JsonUtils;
import com.github.mouse0w0.peach.util.TypeUtils;
import com.google.gson.JsonElement;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Storage("recentProjects.json")
public final class RecentProjectsManagerImpl implements RecentProjectsManager, PersistentService, Disposable {

    private final Map<String, RecentProject> recentProjects = new HashMap<>();
    private final RecentProjectsChange publisher;
    private final MessageBusConnection connection;

    public RecentProjectsManagerImpl() {
        MessageBus messageBus = Peach.getInstance().getMessageBus();
        publisher = messageBus.getPublisher(RecentProjectsChange.TOPIC);
        connection = messageBus.connect();
        connection.subscribe(ProjectLifecycleListener.TOPIC, new ProjectLifecycleListener() {
            @Override
            public void projectOpened(Project project) {
                updateRecentProject(project);
            }
        });
    }

    @Override
    public Collection<RecentProject> getRecentProjects() {
        return recentProjects.values();
    }

    @Override
    public void removeRecentProject(String path) {
        recentProjects.remove(path);
        publisher.onChanged();
    }

    private void updateRecentProject(Project project) {
        String path = project.getPath().toString();
        RecentProject info = recentProjects.computeIfAbsent(path, RecentProject::new);
        info.setName(project.getName());
        info.setLatestOpenTimestamp(System.currentTimeMillis());
        publisher.onChanged();
    }

    @Override
    public JsonElement saveState() {
        return JsonUtils.toJson(recentProjects.values());
    }

    @Override
    public void loadState(JsonElement state) {
        JsonUtils.<List<RecentProject>>fromJson(state, TypeUtils.list(RecentProject.class))
                .forEach(info -> recentProjects.put(info.getPath(), info));
    }

    @Override
    public void dispose() {
        connection.disconnect();
    }
}
