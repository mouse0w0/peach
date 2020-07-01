package com.github.mouse0w0.peach.forge;

import com.github.mouse0w0.peach.data.Key;
import com.github.mouse0w0.peach.util.JsonFile;

import java.util.Locale;

public class ForgeProjectInfo {

    public static final Key<JsonFile<ForgeProjectInfo>> KEY = Key.of(ForgeProjectInfo.class);

    public static final String FILE_NAME = "project.forge.json";

    private String name = "untitled";
    private String id = "untitled";
    private String version = "1.0.0";
    private String mcVersion = "1.12.2";
    private Locale language = Locale.getDefault();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMcVersion() {
        return mcVersion;
    }

    public void setMcVersion(String mcVersion) {
        this.mcVersion = mcVersion;
    }

    public Locale getLanguage() {
        return language;
    }

    public void setLanguage(Locale language) {
        this.language = language;
    }
}
