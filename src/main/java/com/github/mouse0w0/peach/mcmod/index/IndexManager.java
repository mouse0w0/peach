package com.github.mouse0w0.peach.mcmod.index;

import com.github.mouse0w0.peach.mcmod.project.ModProjectService;
import com.github.mouse0w0.peach.project.Project;

public interface IndexManager {
    static IndexManager getInstance(Project project) {
        return ModProjectService.getInstance(project).getIndexManager();
    }

    <K, V> Index<K, V> getIndex(IndexKey<K, V> key);
}
