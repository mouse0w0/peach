package com.github.mouse0w0.peach.mcmod.generator.task;

import com.github.mouse0w0.peach.mcmod.generator.Context;
import com.github.mouse0w0.peach.util.FileUtils;
import com.google.common.collect.ImmutableSet;

import java.nio.file.Path;
import java.util.Set;

public class Delete implements Task {
    private final Set<Path> paths;

    public Delete(Path... paths) {
        this.paths = ImmutableSet.copyOf(paths);
    }

    public Set<Path> getPaths() {
        return paths;
    }

    @Override
    public void run(Context context) throws Exception {
        for (Path path : paths) {
            FileUtils.delete(path);
        }
    }
}
