package com.github.mouse0w0.peach.fileWatch;

import com.github.mouse0w0.peach.message.BroadcastDirection;
import com.github.mouse0w0.peach.message.Topic;

import java.nio.file.Path;

public interface FileChangeListener {
    Topic<FileChangeListener> TOPIC = new Topic<>("FileChangeListener", FileChangeListener.class, BroadcastDirection.NONE);

    default void onFileCreate(Path path) {}

    default void onFileDelete(Path path) {}

    default void onFileModify(Path path) {}
}
