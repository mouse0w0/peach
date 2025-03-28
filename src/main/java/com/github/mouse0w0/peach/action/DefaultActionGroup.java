package com.github.mouse0w0.peach.action;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DefaultActionGroup extends ActionGroup {
    private final List<Action> children = new ArrayList<>();
    private final List<Action> unmodifiableChildren = Collections.unmodifiableList(children);

    private Object[] pendingAction;
    private int pendingActionSize;

    @Override
    public List<Action> getChildren(@Nullable ActionEvent event) {
        return unmodifiableChildren;
    }

    public void add(Action action, Order order) {
        add(action, order, ActionManager.getInstance());
    }

    public boolean contains(Action action) {
        if (children.contains(action)) return true;
        for (int i = 0; i < pendingActionSize; i += 2) {
            if (pendingAction[i].equals(action)) {
                return true;
            }
        }
        return false;
    }

    public synchronized void add(Action action, Order order, ActionManager actionManager) {
        if (action == this) {
            throw new IllegalArgumentException("Cannot add a group to itself. group=" + actionManager.getActionId(this));
        }

        if (!(action instanceof Separator) && contains(action)) {
            throw new IllegalArgumentException("Cannot add an action twice. action=" + actionManager.getActionId(action) + ", group=" + actionManager.getActionId(this));
        }

        if (order.getAnchor() == Anchor.FIRST) {
            children.add(0, action);
        } else if (order.getAnchor() == Anchor.LAST) {
            children.add(action);
        } else {
            addPendingAction(action, order);
        }
        processPendingAction(actionManager);
    }

    public synchronized boolean remove(Action action) {
        if (children.remove(action)) return true;
        for (int i = 0; i < pendingActionSize; i += 2) {
            if (pendingAction[i].equals(action)) {
                pendingActionSize -= 2;
                System.arraycopy(pendingAction, i + 2, pendingAction, i, pendingActionSize - i);
                pendingAction[pendingActionSize] = null;
                pendingAction[pendingActionSize + 1] = null;
                return true;
            }
        }
        return false;
    }

    private void addPendingAction(Action action, Order order) {
        if (pendingAction == null) {
            pendingAction = new Object[]{action, order};
            pendingActionSize = 2;
        } else {
            if (pendingActionSize + 2 >= pendingAction.length) {
                pendingAction = Arrays.copyOf(pendingAction, pendingAction.length * 2);
            }
            pendingAction[pendingActionSize++] = action;
            pendingAction[pendingActionSize++] = order;
        }
    }

    private void processPendingAction(ActionManager actionManager) {
        int i = 0;
        while (i < pendingActionSize) {
            Action action = (Action) pendingAction[i];
            Order order = (Order) pendingAction[i + 1];
            Action relativeToAction = actionManager.getAction(order.getRelativeToActionId());
            if (relativeToAction == null) {
                i += 2;
                continue;
            }

            int relativeToActionIndex = children.indexOf(relativeToAction);
            if (relativeToActionIndex == -1) {
                i += 2;
                continue;
            }

            if (order.getAnchor() == Anchor.BEFORE) {
                children.add(relativeToActionIndex, action);
            } else {
                children.add(relativeToActionIndex + 1, action);
            }

            pendingActionSize -= 2;
            System.arraycopy(pendingAction, i + 2, pendingAction, i, pendingActionSize - i);
            pendingAction[pendingActionSize] = null;
            pendingAction[pendingActionSize + 1] = null;
        }
    }
}
