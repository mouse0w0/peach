package com.github.mouse0w0.peach.mcmod.project;

import com.github.mouse0w0.peach.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class ModMetadata {

    public static final String FILE_NAME = "project.forge.json";

    private String name = "untitled";
    private String id = "untitled";
    private String version = "1.0.0";
    private List<String> authors = Collections.emptyList();
    private String mcVersion = "1.12.2";
    private Locale language = Locale.getDefault();
    private String description;
    private String url;
    private String updateUrl;
    private String credits;

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
        return authors != null && authors.size() >= 1 ? authors.get(0) : "";
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public void setFirstAuthor(String author) {
        if (StringUtils.isEmpty(author)) {
            authors = Collections.emptyList();
        } else {
            authors = Collections.singletonList(author);
        }
    }

    public Locale getLanguage() {
        return language;
    }

    public void setLanguage(Locale language) {
        this.language = language;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUpdateUrl() {
        return updateUrl;
    }

    public void setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
    }

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }
}
