package com.github.mouse0w0.peach;

public class RecentProjectInfo {
    private String path;
    private String name;
    private long latestOpenTimestamp;

    public RecentProjectInfo(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLatestOpenTimestamp() {
        return latestOpenTimestamp;
    }

    public void setLatestOpenTimestamp(long latestOpenTimestamp) {
        this.latestOpenTimestamp = latestOpenTimestamp;
    }
}
