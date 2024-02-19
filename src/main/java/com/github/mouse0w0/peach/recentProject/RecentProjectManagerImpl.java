package com.github.mouse0w0.peach.recentProject;

import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.dispose.Disposable;
import com.github.mouse0w0.peach.message.MessageBusConnection;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.project.ProjectLifecycleListener;
import com.github.mouse0w0.peach.service.PersistentService;
import com.github.mouse0w0.peach.service.Storage;
import com.github.mouse0w0.peach.util.JsonUtils;
import com.google.gson.JsonElement;
import org.apache.commons.lang3.reflect.TypeUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Storage("recentProjects.json")
public final class RecentProjectManagerImpl implements RecentProjectManager, PersistentService, Disposable {

    private final Map<String, RecentProjectInfo> recentProjects = new HashMap<>();
    private final MessageBusConnection connection;

    public RecentProjectManagerImpl() {
        connection = Peach.getInstance().getMessageBus().connect();
        connection.subscribe(ProjectLifecycleListener.TOPIC, new ProjectLifecycleListener() {
            @Override
            public void projectOpened(Project project) {
                updateRecentProject(project);
            }
        });
    }

    @Override
    public Collection<RecentProjectInfo> getRecentProjects() {
        return recentProjects.values();
    }

    @Override
    public void removeRecentProject(String path) {
        recentProjects.remove(path);
    }

    private void updateRecentProject(Project project) {
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

    @Override
    public void dispose() {
        connection.disconnect();
    }
}
