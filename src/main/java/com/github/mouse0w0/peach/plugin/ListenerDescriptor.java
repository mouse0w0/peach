package com.github.mouse0w0.peach.plugin;

public final class ListenerDescriptor {
    private final String topic;
    private final String listenerClassName;

    private Plugin plugin;

    public ListenerDescriptor(String topic, String listenerClassName) {
        this.topic = topic;
        this.listenerClassName = listenerClassName;
    }

    public String getTopic() {
        return topic;
    }

    public String getListenerClassName() {
        return listenerClassName;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public void setPlugin(Plugin plugin) {
        this.plugin = plugin;
    }
}
