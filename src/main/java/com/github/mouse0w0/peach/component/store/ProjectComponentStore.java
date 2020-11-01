package com.github.mouse0w0.peach.component.store;

import com.github.mouse0w0.peach.project.Project;

public class ProjectComponentStore extends ComponentStoreBase {

    public static final String STORE_FOLDER = ".peach";

    public ProjectComponentStore(Project project) {
        super(project.getPath().resolve(STORE_FOLDER));
    }
}
