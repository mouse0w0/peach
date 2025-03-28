package com.github.mouse0w0.peach.project;

import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.message.CompositeMessageBus;
import com.github.mouse0w0.peach.message.MessageBusImpl;
import com.github.mouse0w0.peach.plugin.ListenerDescriptor;
import com.github.mouse0w0.peach.plugin.Plugin;
import com.github.mouse0w0.peach.service.ServiceDescriptor;
import com.github.mouse0w0.peach.service.ServiceManagerImpl;
import com.github.mouse0w0.peach.service.store.ProjectServiceStore;
import com.github.mouse0w0.peach.window.ProjectWindow;
import com.github.mouse0w0.peach.window.WindowManager;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

@ApiStatus.Internal
public final class ProjectImpl extends ServiceManagerImpl implements Project {
    private final Path path;

    public ProjectImpl(@NotNull Path path) {
        super(Peach.getInstance());
        this.path = Objects.requireNonNull(path);
        if (!Files.exists(path)) {
            throw new IllegalStateException("Project path must be exists");
        }
        if (!Files.isDirectory(path)) {
            throw new IllegalArgumentException("Project path must be a directory");
        }
        initialize();
        preloadServices();
    }

    @NotNull
    @Override
    public Path getPath() {
        return path;
    }

    @NotNull
    @Override
    public String getName() {
        String name = getServiceStore().getProjectName();
        if (name != null) {
            return name;
        }
        return path.getFileName().toString();
    }

    @Override
    public void setName(@Nullable String name) {
        getServiceStore().setProjectName(name);

        ProjectWindow window = WindowManager.getInstance().getWindow(this);
        if (window != null) {
            window.getStage().setTitle(getName());
        }
    }

    @Override
    public boolean isOpened() {
        return ProjectManager.getInstance().isProjectOpened(this);
    }

    @Override
    public void save() {
        saveServices();
    }

    @Override
    protected ProjectServiceStore getServiceStore() {
        return (ProjectServiceStore) super.getServiceStore();
    }

    @Override
    protected MessageBusImpl createMessageBus() {
        return new MessageBusImpl((CompositeMessageBus) parent.getMessageBus());
    }

    @Override
    protected List<ServiceDescriptor> getPluginServices(Plugin plugin) {
        return plugin.getProjectServices();
    }

    @Override
    protected List<ListenerDescriptor> getPluginListeners(Plugin plugin) {
        return plugin.getProjectListeners();
    }
}
