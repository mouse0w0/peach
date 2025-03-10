package com.github.mouse0w0.peach.plugin;

import com.github.mouse0w0.peach.service.ServiceDescriptor;
import com.github.mouse0w0.version.Version;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

final class PluginImpl implements Plugin {
    private static final Version DEFAULT_VERSION = new Version("0.0.1");

    private final String id;
    private final String name;
    private final Version version;
    private final String logo;
    private final String license;
    private final String description;
    private final String category;
    private final String author;
    private final String authorUrl;
    private final String authorEmail;
    private final String homepage;
    private final String repository;
    private final String issueTrackingUrl;
    private final String issueTrackingEmail;
    private final String updateUrl;
    private final List<PluginDependency> dependencies;
    private final List<ActionDescriptor> actions;
    private final List<ExtensionPointDescriptor> extensionPoints;
    private final Map<String, List<ExtensionDescriptor>> extensions;
    private final List<ServiceDescriptor> applicationServices;
    private final List<ServiceDescriptor> projectServices;
    private final List<ListenerDescriptor> applicationListeners;
    private final List<ListenerDescriptor> projectListeners;
    private final List<Path> classpath;
    private final boolean useCoreClassLoader;
    private ClassLoader classLoader;

    private boolean enabled;

    public PluginImpl(PluginXml xml, List<Path> classpath, boolean useCoreClassLoader) {
        this.id = xml.id;
        this.name = xml.name;
        this.version = xml.version != null ? new Version(xml.version) : DEFAULT_VERSION;
        this.logo = xml.logo;
        this.license = xml.license;
        this.description = xml.description;
        this.category = xml.category;
        this.author = xml.author;
        this.authorUrl = xml.authorUrl;
        this.authorEmail = xml.authorEmail;
        this.homepage = xml.homepage;
        this.repository = xml.repository;
        this.issueTrackingUrl = xml.issueTrackingUrl;
        this.issueTrackingEmail = xml.issueTrackingEmail;
        this.updateUrl = xml.updateUrl;
        this.dependencies = freezeList(xml.dependencies);
        this.actions = freezeList(xml.actions);
        this.extensionPoints = freezeList(xml.extensionPoints);
        this.extensions = freezeExtensions(xml.extensions);
        this.applicationServices = freezeList(xml.applicationServices);
        this.projectServices = freezeList(xml.projectServices);
        this.applicationListeners = freezeList(xml.applicationListeners);
        this.projectListeners = freezeList(xml.projectListeners);
        this.classpath = classpath;
        this.useCoreClassLoader = useCoreClassLoader;
    }

    private static <E> List<E> freezeList(List<? extends E> list) {
        return list != null ? ImmutableList.copyOf(list) : ImmutableList.of();
    }

    private static Map<String, List<ExtensionDescriptor>> freezeExtensions(Map<String, List<ExtensionDescriptor>> extensions) {
        if (extensions == null) {
            return ImmutableMap.of();
        }
        ImmutableMap.Builder<String, List<ExtensionDescriptor>> builder = ImmutableMap.builder();
        for (Map.Entry<String, List<ExtensionDescriptor>> entry : extensions.entrySet()) {
            builder.put(entry.getKey(), freezeList(entry.getValue()));
        }
        return builder.build();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Version getVersion() {
        return version;
    }

    @Override
    public String getLogo() {
        return logo;
    }

    @Override
    public String getLicense() {
        return license;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public String getAuthor() {
        return author;
    }

    @Override
    public String getAuthorUrl() {
        return authorUrl;
    }

    @Override
    public String getAuthorEmail() {
        return authorEmail;
    }

    @Override
    public String getHomepage() {
        return homepage;
    }

    @Override
    public String getRepository() {
        return repository;
    }

    @Override
    public String getIssueTrackingUrl() {
        return issueTrackingUrl;
    }

    @Override
    public String getIssueTrackingEmail() {
        return issueTrackingEmail;
    }

    @Override
    public String getUpdateUrl() {
        return updateUrl;
    }

    @Override
    public List<PluginDependency> getDependencies() {
        return dependencies;
    }

    @Override
    public List<ActionDescriptor> getActions() {
        return actions;
    }

    @Override
    public List<ExtensionPointDescriptor> getExtensionPoints() {
        return extensionPoints;
    }

    @Override
    public Map<String, List<ExtensionDescriptor>> getExtensions() {
        return extensions;
    }

    @Override
    public List<ExtensionDescriptor> getExtensions(String extensionPoint) {
        return extensions.get(extensionPoint);
    }

    @Override
    public List<ServiceDescriptor> getApplicationServices() {
        return applicationServices;
    }

    @Override
    public List<ServiceDescriptor> getProjectServices() {
        return projectServices;
    }

    @Override
    public List<ListenerDescriptor> getApplicationListeners() {
        return applicationListeners;
    }

    @Override
    public List<ListenerDescriptor> getProjectListeners() {
        return projectListeners;
    }

    @Override
    public List<Path> getClasspath() {
        return classpath;
    }

    @Override
    public boolean isUseCoreClassLoader() {
        return useCoreClassLoader;
    }

    @Override
    public ClassLoader getClassLoader() {
        return classLoader;
    }

    void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
