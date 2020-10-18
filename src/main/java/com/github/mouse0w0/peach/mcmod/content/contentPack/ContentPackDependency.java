package com.github.mouse0w0.peach.mcmod.content.contentPack;

import com.github.mouse0w0.version.VersionRange;

public final class ContentPackDependency {

    private final String id;
    private final VersionRange versionRange;
    private final boolean required;
    private final boolean before;
    private final boolean after;

    public static ContentPackDependency parse(String dependencyString) {
        String[] split = dependencyString.split("[:@]", 3);
        String instructions = split[0];
        String id = split[1];
        VersionRange versionRange = split.length == 3 ? VersionRange.createFromVersionSpec(split[2]) : null;
        boolean required = false, before = false, after = false;

        for (String instruction : instructions.split(";")) {
            if ("required".equals(instruction)) required = true;
            else if ("before".equals(instruction)) before = true;
            else if ("after".equals(instruction)) after = true;
        }

        return new ContentPackDependency(id, versionRange, required, before, after);
    }

    public ContentPackDependency(String id, VersionRange versionRange, boolean required, boolean before, boolean after) {
        this.id = id;
        this.versionRange = versionRange;
        this.required = required;
        this.before = before;
        this.after = after;
    }

    public String getId() {
        return id;
    }

    public VersionRange getVersionRange() {
        return versionRange;
    }

    public boolean isRequired() {
        return required;
    }

    public boolean isBefore() {
        return before;
    }

    public boolean isAfter() {
        return after;
    }
}
