package com.github.mouse0w0.peach.forge;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class ForgeModInfo {

    public static final String FILE_NAME = "project.forge.json";

    private String name = "untitled";
    private String id = "untitled";
    private String version = "1.0.0";
    private String mcVersion = "1.12.2";
    private String description = "";
    private List<String> authors = Collections.emptyList();
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public String getFirstAuthor() {
        return authors != null && authors.size() >= 1 ? authors.get(0) : null;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public Locale getLanguage() {
        return language;
    }

    public void setLanguage(Locale language) {
        this.language = language;
    }
}
