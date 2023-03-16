package com.github.mouse0w0.peach.plugin;

import com.github.mouse0w0.peach.service.ServiceDescriptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

final class PluginDescriptor {
    public String id;
    public String name;
    public String version;
    public String logo;
    public String license;
    public String description;
    public String category;
    public String author;
    public String authorUrl;
    public String authorEmail;
    public String homepage;
    public String repository;
    public String issueTrackingUrl;
    public String issueTrackingEmail;
    public String updateUrl;
    public List<PluginDependency> dependencies;
    public List<ActionDescriptor> actions;
    public List<ExtensionPointDescriptor> extensionPoints;
    public Map<String, List<ExtensionDescriptor>> extensions;
    public List<ServiceDescriptor> applicationServices;
    public List<ServiceDescriptor> projectServices;
    public List<ListenerDescriptor> applicationListeners;
    public List<ListenerDescriptor> projectListeners;

    public void addApplicationService(ServiceDescriptor serviceDescriptor) {
        if (applicationServices == null) {
            applicationServices = new ArrayList<>();
        }
        applicationServices.add(serviceDescriptor);
    }

    public void addProjectService(ServiceDescriptor serviceDescriptor) {
        if (projectServices == null) {
            projectServices = new ArrayList<>();
        }
        projectServices.add(serviceDescriptor);
    }

    public void addApplicationListener(ListenerDescriptor listenerDescriptor) {
        if (applicationListeners == null) {
            applicationListeners = new ArrayList<>();
        }
        applicationListeners.add(listenerDescriptor);
    }

    public void addProjectListeners(ListenerDescriptor listenerDescriptor) {
        if (projectListeners == null) {
            projectListeners = new ArrayList<>();
        }
        projectListeners.add(listenerDescriptor);
    }
}
