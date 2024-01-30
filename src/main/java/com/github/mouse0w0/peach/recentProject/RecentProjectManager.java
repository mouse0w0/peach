package com.github.mouse0w0.peach.recentProject;

import com.github.mouse0w0.peach.Peach;

import java.util.Collection;

public interface RecentProjectManager {
    static RecentProjectManager getInstance() {
        return Peach.getInstance().getService(RecentProjectManager.class);
    }

    Collection<RecentProjectInfo> getRecentProjects();

    void removeRecentProject(String path);
}
