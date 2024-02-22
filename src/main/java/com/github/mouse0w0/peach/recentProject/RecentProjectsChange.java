package com.github.mouse0w0.peach.recentProject;

import com.github.mouse0w0.peach.message.BroadcastDirection;
import com.github.mouse0w0.peach.message.Topic;

public interface RecentProjectsChange {
    Topic<RecentProjectsChange> TOPIC = new Topic<>("RecentProjectsChange", RecentProjectsChange.class, BroadcastDirection.NONE);

    void onChanged();
}
