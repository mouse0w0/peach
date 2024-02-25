package com.github.mouse0w0.peach.action;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultActionGroup extends ActionGroup {
    private final List<Action> children = new ArrayList<>();
    private final List<Action> unmodifiableChildren = Collections.unmodifiableList(children);

    @Override
    public List<Action> getChildren(@Nullable ActionEvent event) {
        return unmodifiableChildren;
    }

    public boolean containsAction(Action action) {
        return children.contains(action);
    }

    public void addLast(Action action) {
        children.add(action);
    }

    public void addFirst(Action action) {
        children.add(0, action);
    }

    public void addBefore(Action action, Action anchor) {
        int index = children.indexOf(anchor);
        if (index != -1) {
            children.add(index, action);
        }
    }

    public void addAfter(Action action, Action anchor) {
        int index = children.indexOf(anchor);
        if (index != -1) {
            children.add(index + 1, action);
        }
    }
}
