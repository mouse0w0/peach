package com.github.mouse0w0.peach.dispose;

import java.util.ArrayList;
import java.util.List;

final class ObjectNode {
    private final ObjectTree tree;
    private final Disposable disposable;
    private ObjectNode parent;
    private List<ObjectNode> children;

    public ObjectNode(ObjectTree tree, Disposable disposable) {
        this.tree = tree;
        this.disposable = disposable;
    }

    public ObjectNode getParent() {
        return parent;
    }

    public void addChild(ObjectNode node) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(node);
        node.parent = this;
    }

    public void removeChild(ObjectNode node) {
        if (children != null) {
            children.remove(node);
        }
        node.parent = null;
    }

    public void markAndCollectRecursively(List<? super Disposable> result) {
        if (children != null) {
            for (ObjectNode child : children) {
                child.markAndCollectRecursively(result);
            }
            children.clear();
            children = null;
        }

        if (tree.markDisposed(disposable)) {
            result.add(disposable);
        }
    }
}
