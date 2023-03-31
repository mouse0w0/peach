package com.github.mouse0w0.peach.action;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ActionGroup extends Action {
    private final List<Action> children = new ArrayList<>();
    private final List<Action> unmodifiableChildren = Collections.unmodifiableList(children);

    public List<Action> getChildren() {
        return unmodifiableChildren;
    }

    public void addLast(Action action) {
        children.add(action);
    }

    public void addFirst(Action action) {
        children.add(0, action);
    }

    public void addChild(Action action, boolean before, @NotNull Action anchor) {
        int index = children.indexOf(anchor);
        if (index == -1) return;
        if (before) children.add(index, action);
        else children.add(index + 1, action);
    }

    @Override
    public void perform(ActionEvent event) {
        throw new UnsupportedOperationException();
    }
}
