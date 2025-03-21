package com.github.mouse0w0.peach.plugin;

import com.github.mouse0w0.peach.Peach;
import com.github.mouse0w0.peach.util.FileUtils;
import com.github.mouse0w0.peach.util.graph.DirectedGraph;
import com.github.mouse0w0.peach.util.graph.TopoSort;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@ApiStatus.Internal
public final class PluginManagerCore {
    public static final String PLUGIN_XML = "plugin.xml";

    public static final String CORE_PLUGIN_ID = "peach";

    private static final Logger LOGGER = LoggerFactory.getLogger(PluginManagerCore.class);

    private static final ClassLoader[] EMPTY_CLASS_LOADER_ARRAY = new ClassLoader[0];

    private static List<? extends Plugin> plugins;
    private static List<? extends Plugin> enabledPlugins;
    private static Map<String, ? extends Plugin> idToPluginMap;

    public static List<? extends Plugin> getPlugins() {
        return plugins;
    }

    public static List<? extends Plugin> getEnabledPlugins() {
        return enabledPlugins;
    }

    @Nullable
    public static Plugin getPlugin(String id) {
        return idToPluginMap.get(id);
    }

    public static void loadPlugins() {
        LOGGER.info("Loading plugins.");

        ClassLoader coreClassLoader = PluginManagerCore.class.getClassLoader();

        List<CompletableFuture<PluginImpl>> futures = new ArrayList<>();
        loadFromClasspath(coreClassLoader, futures);
        loadFromDir(Path.of("plugin").toAbsolutePath(), futures);

        Map<String, PluginImpl> idToPluginMap = new HashMap<>();
        Multimap<String, PluginImpl> duplicatePlugins = ArrayListMultimap.create();
        for (CompletableFuture<PluginImpl> future : futures) {
            try {
                PluginImpl plugin = future.join();
                if (plugin != null) {
                    String pluginId = plugin.getId();
                    if (duplicatePlugins.containsKey(pluginId)) {
                        duplicatePlugins.put(pluginId, plugin);
                    } else {
                        PluginImpl duplicatePlugin = idToPluginMap.put(pluginId, plugin);
                        if (duplicatePlugin != null) {
                            duplicatePlugins.put(pluginId, duplicatePlugin);
                            duplicatePlugins.put(pluginId, plugin);
                            idToPluginMap.remove(pluginId);
                        }
                    }
                }
            } catch (CompletionException e) {
                LOGGER.error("An exception occurred while loading the plugin.", e.getCause());
            }
        }
        logDuplicatePlugins(duplicatePlugins);

        Collection<PluginImpl> plugins = idToPluginMap.values();
        Multimap<PluginImpl, PluginDependency> missingDependency = ArrayListMultimap.create();
        for (PluginImpl plugin : plugins) {
            for (PluginDependency dependency : plugin.getDependencies()) {
                if (!dependency.isOptional()) {
                    PluginImpl parentPlugin = idToPluginMap.get(dependency.getId());
                    if (parentPlugin == null || !dependency.getVersionRange().containsVersion(parentPlugin.getVersion())) {
                        missingDependency.put(plugin, dependency);
                    }
                }
            }
        }
        logMissingDependency(missingDependency);

        DirectedGraph<PluginImpl> pluginGraph = new DirectedGraph<>();
        for (PluginImpl plugin : plugins) {
            if (!missingDependency.containsKey(plugin)) {
                pluginGraph.addNode(plugin);
                for (PluginDependency dependency : plugin.getDependencies()) {
                    PluginImpl parentPlugin = idToPluginMap.get(dependency.getId());
                    if (parentPlugin != null) {
                        pluginGraph.addEdge(parentPlugin, plugin);
                    }
                }
            }
        }
        TopoSort<PluginImpl> builder = new TopoSort<>(pluginGraph);
        List<PluginImpl> enabledPlugins = new ArrayList<>();
        for (List<PluginImpl> scc : builder.getComponents()) {
            if (scc.size() == 1) {
                enabledPlugins.add(scc.get(0));
            } else {
                logCircularDependency(scc);
            }
        }

        LOGGER.info("Enabling plugins.");
        Set<ClassLoader> classLoaders = new LinkedHashSet<>();
        Deque<PluginImpl> pluginQueue = new ArrayDeque<>();
        for (PluginImpl plugin : enabledPlugins) {
            if (plugin.isUseCoreClassLoader()) {
                plugin.setClassLoader(coreClassLoader);
            } else {
                classLoaders.clear();
                for (PluginDependency dependency : plugin.getDependencies()) {
                    PluginImpl parentPlugin = idToPluginMap.get(dependency.getId());
                    if (parentPlugin != null) {
                        pluginQueue.add(parentPlugin);
                    }
                }

                while (!pluginQueue.isEmpty()) {
                    PluginImpl popPlugin = pluginQueue.pop();
                    if (!classLoaders.add(popPlugin.getClassLoader())) {
                        continue;
                    }

                    for (PluginDependency dependency : plugin.getDependencies()) {
                        PluginImpl parentPlugin = idToPluginMap.get(dependency.getId());
                        if (parentPlugin != null) {
                            pluginQueue.add(parentPlugin);
                        }
                    }
                }
                classLoaders.add(coreClassLoader);

                List<Path> classpath = plugin.getClasspath();
                int len = classpath.size();
                URL[] urls = new URL[len];
                for (int i = 0; i < len; i++) {
                    urls[i] = FileUtils.toURL(classpath.get(i));
                }
                plugin.setClassLoader(new PluginClassLoader(urls, plugin, classLoaders.toArray(EMPTY_CLASS_LOADER_ARRAY)));
            }
            plugin.setEnabled(true);
        }

        PluginManagerCore.plugins = ImmutableList.copyOf(plugins);
        PluginManagerCore.enabledPlugins = ImmutableList.copyOf(enabledPlugins);
        PluginManagerCore.idToPluginMap = ImmutableMap.copyOf(idToPluginMap);

        logEnabledPlugins(enabledPlugins);
    }

    private static void logDuplicatePlugins(Multimap<String, PluginImpl> duplicatePlugins) {
        for (String pluginId : duplicatePlugins.keySet()) {
            StringBuilder errorMessageBuilder = new StringBuilder();
            errorMessageBuilder.append("Duplicate plugin with id `").append(pluginId).append("`, as follows:");
            for (PluginImpl plugin1 : duplicatePlugins.get(pluginId)) {
                errorMessageBuilder.append("\n    - ").append(plugin1.getClasspath());
            }
            LOGGER.error(errorMessageBuilder.toString());
        }
    }

    private static void logMissingDependency(Multimap<PluginImpl, PluginDependency> missingDependency) {
        for (PluginImpl plugin : missingDependency.keySet()) {
            StringBuilder errorMessageBuilder = new StringBuilder();
            errorMessageBuilder.append("Missing dependency with plugin `").append(plugin.getId()).append("`, as follows:");
            for (PluginDependency dependency : missingDependency.get(plugin)) {
                errorMessageBuilder.append("\n    - ").append(dependency);
            }
            LOGGER.error(errorMessageBuilder.toString());
        }
    }

    private static void logCircularDependency(List<PluginImpl> scc) {
        StringBuilder errorMessageBuilder = new StringBuilder();
        errorMessageBuilder.append("Circular dependency detected, as follows:");
        for (PluginImpl plugin : scc) {
            errorMessageBuilder.append("\n    - ").append(plugin.getId()).append(" ").append(plugin.getClasspath());
        }
        LOGGER.error(errorMessageBuilder.toString());
    }

    private static void logEnabledPlugins(List<PluginImpl> plugins) {
        if (!LOGGER.isInfoEnabled()) return;
        StringJoiner stringJoiner = new StringJoiner(", ", "Enabled plugins: ", "");
        for (PluginImpl plugin : plugins) {
            stringJoiner.add(plugin.getId() + "@" + plugin.getVersion());
        }
        LOGGER.info(stringJoiner.toString());
    }

    private static void loadFromClasspath(ClassLoader coreClassLoader, List<CompletableFuture<PluginImpl>> futures) {
        try {
            Enumeration<URL> resources = coreClassLoader.getResources(PluginManagerCore.PLUGIN_XML);
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                switch (url.getProtocol()) {
                    case "file" -> {
                        String urlPath = url.getPath();
                        int idx = urlPath.lastIndexOf('/');
                        Path path = Path.of(URLDecoder.decode(urlPath.substring(1, idx), StandardCharsets.UTF_8));
                        futures.add(CompletableFuture.supplyAsync(() -> loadFromPluginDir(path, true)));
                    }
                    case "jar" -> {
                        String urlPath = url.getPath();
                        int idx = urlPath.lastIndexOf('!');
                        Path path = Path.of(URLDecoder.decode(urlPath.substring(6, idx), StandardCharsets.UTF_8));
                        futures.add(CompletableFuture.supplyAsync(() -> loadFromZip(path, true)));
                    }
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e.getMessage(), e);
        }
    }

    private static void loadFromDir(Path dir, List<CompletableFuture<PluginImpl>> futures) {
        if (Files.notExists(dir)) {
            return;
        }
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path path : stream) {
                if (Files.isDirectory(path)) {
                    futures.add(CompletableFuture.supplyAsync(() -> loadFromPluginDir(path, false)));
                } else {
                    String pathString = path.toString();
                    if (pathString.endsWith(".jar") || pathString.endsWith(".zip")) {
                        futures.add(CompletableFuture.supplyAsync(() -> loadFromZip(path, false)));
                    }
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e.getMessage(), e);
        }
    }

    private static PluginImpl loadFromPluginDir(Path dir, boolean useCoreClassLoader) {
        Path pluginXmlFile = dir.resolve(PLUGIN_XML);
        if (Files.notExists(pluginXmlFile)) {
            return null;
        }

        List<Path> classpath;
        if (dir.getFileName().toString().equals("resources")) {
            Path classesDir = dir.getParent().resolve("classes");
            if (Files.exists(classesDir)) {
                classpath = ImmutableList.of(dir, classesDir);
            } else {
                classpath = ImmutableList.of(dir);
            }
        } else {
            classpath = ImmutableList.of(dir);
        }

        try {
            return new PluginImpl(loadPluginXml(FileUtils.toURLString(pluginXmlFile), Files.newInputStream(pluginXmlFile)), classpath, useCoreClassLoader);
        } catch (Exception e) {
            throw new PluginLoadException(e, classpath);
        }
    }

    private static PluginImpl loadFromZip(Path zip, boolean useCoreClassLoader) {
        List<Path> classpath = ImmutableList.of(zip);

        try (ZipFile zipFile = new ZipFile(zip.toFile())) {
            ZipEntry pluginXmlFile = zipFile.getEntry(PLUGIN_XML);
            if (pluginXmlFile == null) {
                return null;
            }

            return new PluginImpl(loadPluginXml("jar:" + FileUtils.toURLString(zip) + "!/" + PLUGIN_XML, zipFile.getInputStream(pluginXmlFile)), classpath, useCoreClassLoader);
        } catch (Exception e) {
            throw new PluginLoadException(e, classpath);
        }
    }

    private static PluginXml loadPluginXml(String systemId, InputStream inputStream) throws XMLStreamException {
        PluginXml pluginXml = PluginXmlReader.read(systemId, inputStream);

        if (CORE_PLUGIN_ID.equals(pluginXml.id)) {
            pluginXml.version = Peach.getInstance().getVersion().toString();
        }

        return pluginXml;
    }

    private PluginManagerCore() {
    }
}
