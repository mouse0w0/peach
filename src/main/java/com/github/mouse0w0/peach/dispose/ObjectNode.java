package com.github.mouse0w0.peach.dispose;

import it.unimi.dsi.fastutil.objects.ReferenceArrayList;

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
            children = new ReferenceArrayList<>();
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

    public void getAndRemoveRecursively(List<? super Disposable> result) {
        getAndRemoveChildrenRecursively(result);
        if (tree.removeFromTree(disposable)) {
            result.add(disposable);
        }
    }

    public void getAndRemoveChildrenRecursively(List<? super Disposable> result) {
        if (children != null) {
            for (ObjectNode child : children) {
                child.getAndRemoveRecursively(result);
            }
            children.clear();
            children = null;
        }
    }
}
