package com.github.mouse0w0.peach.plugin;

import com.github.mouse0w0.peach.service.ServiceDescriptor;
import com.github.mouse0w0.version.Version;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public interface Plugin {

    String getId();

    String getName();

    Version getVersion();

    String getLogo();

    String getLicense();

    String getDescription();

    String getCategory();

    String getAuthor();

    String getAuthorUrl();

    String getAuthorEmail();

    String getHomepage();

    String getRepository();

    String getIssueTrackingUrl();

    String getIssueTrackingEmail();

    String getUpdateUrl();

    List<PluginDependency> getDependencies();

    List<ActionDescriptor> getActions();

    List<ExtensionPointDescriptor> getExtensionPoints();

    Map<String, List<ExtensionDescriptor>> getExtensions();

    List<ExtensionDescriptor> getExtensions(String extensionPoint);

    List<ServiceDescriptor> getApplicationServices();

    List<ServiceDescriptor> getProjectServices();

    List<ListenerDescriptor> getApplicationListeners();

    List<ListenerDescriptor> getProjectListeners();

    List<Path> getClasspath();

    boolean isUseCoreClassLoader();

    ClassLoader getClassLoader();

    boolean isEnabled();
}
