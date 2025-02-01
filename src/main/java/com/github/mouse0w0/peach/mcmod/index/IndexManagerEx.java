package com.github.mouse0w0.peach.mcmod.index;

import com.github.mouse0w0.peach.project.Project;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

@ApiStatus.Internal
public interface IndexManagerEx extends IndexManager {
    static IndexManagerEx getInstance(Project project) {
        return (IndexManagerEx) IndexManager.getInstance(project);
    }

    <K, V> IndexEx<K, V> getIndexEx(IndexKey<K, V> key);

    void indexNonProjectEntries(List<IndexProvider> providers);
}
