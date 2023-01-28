package com.github.mouse0w0.peach.project;

import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.component.ComponentManagerImpl;
import com.github.mouse0w0.peach.component.ServiceDescriptor;
import com.github.mouse0w0.peach.component.store.ProjectComponentStore;
import com.github.mouse0w0.peach.dispose.Disposer;
import com.github.mouse0w0.peach.message.MessageBus;
import com.github.mouse0w0.peach.message.impl.MessageBusFactory;
import com.github.mouse0w0.peach.util.Validate;
import com.github.mouse0w0.peach.window.ProjectWindow;
import com.github.mouse0w0.peach.window.WindowManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class Project extends ComponentManagerImpl {
    private final Path path;
    private MessageBus messageBus;

    public Project(@Nonnull Path path) throws IOException {
        super(Peach.getInstance());
        this.path = Validate.notNull(path);
        this.messageBus = MessageBusFactory.create(Peach.getInstance().getMessageBus());
        if (!Files.exists(path)) {
            throw new IllegalStateException("Project path must be exists");
        }
        if (!Files.isDirectory(path)) {
            throw new IllegalArgumentException("Project path must be a directory");
        }
        initServices(ServiceDescriptor.PROJECT_SERVICE.getExtensions());
    }

    @Nonnull
    public Path getPath() {
        return path;
    }

    @Nonnull
    public String getName() {
        String name = getComponentStore().getProjectName();
        if (name != null) {
            return name;
        }
        return path.getFileName().toString();
    }

    public void setName(@Nullable String name) {
        getComponentStore().setProjectName(name);

        ProjectWindow window = WindowManager.getInstance().getWindow(this);
        if (window != null) {
            window.getStage().setTitle(getName());
        }
    }

    public MessageBus getMessageBus() {
        return messageBus;
    }

    @Override
    protected ProjectComponentStore getComponentStore() {
        return (ProjectComponentStore) super.getComponentStore();
    }

    public boolean isOpen() {
        return ProjectManager.getInstance().isProjectOpened(this);
    }

    public void save() throws IOException {
        saveComponents();
    }

    @Override
    public void dispose() {
        disposeComponents();

        if (messageBus != null) {
            Disposer.dispose(messageBus);
            messageBus = null;
        }
    }
}
