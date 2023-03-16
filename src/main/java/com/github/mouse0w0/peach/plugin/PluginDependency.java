package com.github.mouse0w0.peach.plugin;

import com.github.mouse0w0.version.VersionRange;

public final class PluginDependency {
    public static final VersionRange ACCEPT_ALL_VERSION = VersionRange.createFromVersionSpec("*");

    private final String id;
    private final VersionRange versionRange;
    private final boolean optional;

    public PluginDependency(String id, VersionRange versionRange, boolean optional) {
        this.id = id;
        this.versionRange = versionRange;
        this.optional = optional;
    }

    public String getId() {
        return id;
    }

    public VersionRange getVersionRange() {
        return versionRange;
    }

    public boolean isOptional() {
        return optional;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(id);
        if (versionRange != ACCEPT_ALL_VERSION) {
            builder.append(':').append(versionRange);
        }
        if (optional) {
            builder.append(':').append("optional");
        }
        return builder.toString();
    }
}
