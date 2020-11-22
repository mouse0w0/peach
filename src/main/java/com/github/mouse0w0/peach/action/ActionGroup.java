package com.github.mouse0w0.peach.action;

import java.util.ArrayList;
import java.util.List;

public class ActionGroup extends Action {

    private final List<Action> children = new ArrayList<>();

    public List<Action> getChildren() {
        return children;
    }

    public void addChild(Action action) {
        children.add(action);
    }

    @Override
    public void perform(ActionEvent event) {
        throw new UnsupportedOperationException();
    }
}
