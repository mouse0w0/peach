package com.github.mouse0w0.peach.plugin;

import com.github.mouse0w0.peach.service.ServiceDescriptor;
import com.github.mouse0w0.version.Version;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

final class PluginImpl implements Plugin {
    private static final Version DEFAULT_VERSION = new Version("1.0.0");

    private String id;
    private String name;
    private Version version;
    private String logo;
    private String license;
    private String description;
    private String category;
    private String author;
    private String authorUrl;
    private String authorEmail;
    private String homepage;
    private String repository;
    private String issueTrackingUrl;
    private String issueTrackingEmail;
    private String updateUrl;
    private List<PluginDependency> dependencies;
    private List<ActionDescriptor> actions;
    private List<ExtensionPointDescriptor> extensionPoints;
    private Map<String, List<ExtensionDescriptor>> extensions;
    private List<ServiceDescriptor> applicationServices;
    private List<ServiceDescriptor> projectServices;
    private List<ListenerDescriptor> applicationListeners;
    private List<ListenerDescriptor> projectListeners;

    private List<Path> classpath;
    private boolean useCoreClassLoader;
    private ClassLoader classLoader;

    private boolean enabled;

    public PluginImpl(PluginDescriptor descriptor, List<Path> classpath, boolean useCoreClassLoader) {
        this.id = descriptor.id;
        this.name = descriptor.name;
        this.version = descriptor.version != null ? new Version(descriptor.version) : DEFAULT_VERSION;
        this.logo = descriptor.logo;
        this.license = descriptor.license;
        this.description = descriptor.description;
        this.category = descriptor.category;
        this.author = descriptor.author;
        this.authorUrl = descriptor.authorUrl;
        this.authorEmail = descriptor.authorEmail;
        this.homepage = descriptor.homepage;
        this.repository = descriptor.repository;
        this.issueTrackingUrl = descriptor.issueTrackingUrl;
        this.issueTrackingEmail = descriptor.issueTrackingEmail;
        this.updateUrl = descriptor.updateUrl;
        this.dependencies = freezeList(descriptor.dependencies);
        this.actions = freezeList(descriptor.actions);
        this.extensionPoints = freezeList(descriptor.extensionPoints);
        this.extensions = freezeExtensions(descriptor.extensions);
        this.applicationServices = freezeList(descriptor.applicationServices);
        this.projectServices = freezeList(descriptor.projectServices);
        this.applicationListeners = freezeList(descriptor.applicationListeners);
        this.projectListeners = freezeList(descriptor.projectListeners);
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
