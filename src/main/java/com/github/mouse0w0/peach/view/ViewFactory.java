package com.github.mouse0w0.peach.view;

import com.github.mouse0w0.peach.project.Project;
import javafx.scene.Node;

public interface ViewFactory {

    Node createViewContent(Project project);
}
