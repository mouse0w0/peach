package com.github.mouse0w0.peach.recentProject;

import com.github.mouse0w0.peach.Peach;

import java.util.Collection;

public interface RecentProjectsManager {
    static RecentProjectsManager getInstance() {
        return Peach.getInstance().getService(RecentProjectsManager.class);
    }

    Collection<RecentProject> getRecentProjects();

    void removeRecentProject(String path);
}
