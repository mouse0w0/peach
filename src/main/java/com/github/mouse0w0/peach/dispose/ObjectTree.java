package com.github.mouse0w0.peach.dispose;

import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Supplier;

final class ObjectTree {
    private final Map<Disposable, ObjectNode> disposable2NodeMap = new Reference2ObjectOpenHashMap<>();
    private final Set<Disposable> disposedObjects = Collections.newSetFromMap(new WeakHashMap<>());

    public void register(Disposable parent, Disposable child) {
        if (parent == child) {
            throw new IllegalArgumentException("Cannot register to itself: " + parent + "(" + parent.getClass() + ")");
        }
        synchronized (this) {
            if (disposedObjects.contains(parent)) {
                throw new IllegalStateException("Parent: " + parent + " (" + parent.getClass() + ") has been disposed");
            }
            if (disposedObjects.contains(child)) {
                throw new IllegalStateException("Child: " + child + " (" + child.getClass() + ") has been disposed");
            }
            ObjectNode parentNode = getNode(parent);
            if (parentNode == null) {
                parentNode = createNode(parent);
            }
            ObjectNode childNode = getNode(child);
            if (childNode == null) {
                childNode = createNode(child);
            } else {
                ObjectNode oldParentNode = childNode.getParent();
                if (oldParentNode != null) {
                    oldParentNode.removeChild(childNode);
                }
            }
            parentNode.addChild(childNode);
        }
    }

    public void dispose(Disposable disposable, boolean processUnregistered) {
        doDispose(() -> {
            if (disposedObjects.contains(disposable)) {
                return Collections.emptyList();
            }
            ObjectNode node = getNode(disposable);
            if (node == null) {
                if (processUnregistered) {
                    disposedObjects.add(disposable);
                    return Collections.singletonList(disposable);
                } else {
                    return Collections.emptyList();
                }
            }
            ObjectNode parent = node.getParent();
            if (parent != null) {
                parent.removeChild(node);
            }
            List<Disposable> disposables = new ArrayList<>();
            node.markAndCollectRecursively(disposables);
            return disposables;
        });
    }

    private void doDispose(Supplier<? extends List<Disposable>> supplier) {
        List<Disposable> disposables;
        synchronized (this) {
            disposables = supplier.get();
        }
        for (Disposable disposable : disposables) {
            disposable.dispose();
        }
    }

    private ObjectNode getNode(Disposable disposable) {
        return disposable2NodeMap.get(disposable);
    }

    private ObjectNode createNode(Disposable disposable) {
        ObjectNode node = new ObjectNode(this, disposable);
        disposable2NodeMap.put(disposable, node);
        return node;
    }

    /**
     * @param disposable The disposable
     * @return If true, disposable isn't disposed.
     */
    boolean markDisposed(Disposable disposable) {
        disposable2NodeMap.remove(disposable);
        return disposedObjects.add(disposable);
    }

    synchronized void checkAllDisposed() {
        if (disposable2NodeMap.isEmpty()) return;
        Logger logger = LoggerFactory.getLogger(ObjectTree.class);
        disposable2NodeMap.forEach((disposable, node) -> {
            if (node.getParent() != null) return;
            logger.error("Memory leak detected: {} ({}) is registered in Disposer but isn't disposed.",
                    disposable, disposable.getClass());
        });
    }
}
