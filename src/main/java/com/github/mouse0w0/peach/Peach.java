package com.github.mouse0w0.peach;

import com.github.mouse0w0.peach.application.AppLifecycleListener;
import com.github.mouse0w0.peach.dispose.Disposer;
import com.github.mouse0w0.peach.extension.Extensions;
import com.github.mouse0w0.peach.javafx.FXApplication;
import com.github.mouse0w0.peach.message.impl.CompositeMessageBus;
import com.github.mouse0w0.peach.message.impl.MessageBusImpl;
import com.github.mouse0w0.peach.plugin.ListenerDescriptor;
import com.github.mouse0w0.peach.plugin.Plugin;
import com.github.mouse0w0.peach.plugin.PluginManagerCore;
import com.github.mouse0w0.peach.project.ProjectManager;
import com.github.mouse0w0.peach.service.ServiceDescriptor;
import com.github.mouse0w0.peach.service.ServiceManagerImpl;
import com.github.mouse0w0.peach.util.StringUtils;
import com.github.mouse0w0.version.Version;
import javafx.application.Application;
import javafx.scene.image.Image;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.apache.commons.lang3.SystemUtils.*;

public final class Peach extends ServiceManagerImpl {
    private static final Logger LOGGER = LoggerFactory.getLogger(Peach.class);

    private static final Peach INSTANCE = new Peach();

    private final Version version;
    private final Path userDataPath;
    private final AppLifecycleListener appLifecycle;

    public static Peach getInstance() {
        return INSTANCE;
    }

    public static void main(String[] args) {
        INSTANCE.launch(args);
    }

    private Peach() {
        super(null);

        this.version = new Version(getImplementationVersion());
        this.userDataPath = Paths.get(SystemUtils.USER_HOME, ".peach");
        this.appLifecycle = getMessageBus().getPublisher(AppLifecycleListener.TOPIC);
    }

    public Version getVersion() {
        return version;
    }

    public Path getUserDataPath() {
        return userDataPath;
    }

    public void launch(String[] args) {
        LOGGER.info("Launching application.");
        initUncaughtExceptionHandler();
        printSystemInfo();
        LOGGER.info("Loading plugins.");
        PluginManagerCore.loadPlugins();
        LOGGER.info("Loading extensions.");
        Extensions.loadExtensions();
        LOGGER.info("Initializing application.");
        // Fix program pause when initializing Image class.
        forceInit(Image.class);
        INSTANCE.initialize();
        INSTANCE.preloadServices();
        appLifecycle.appStarted();
        LOGGER.info("Initializing JavaFX.");
        Application.launch(FXApplication.class, args);
    }

    private static void initUncaughtExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler((t, e) ->
                LOGGER.error("Uncaught exception detected in thread: {}", t.getName(), e));
    }

    private static void printSystemInfo() {
        LOGGER.info("----- System Information -----");
        LOGGER.info("Application Version: {}", getInstance().getVersion());
        LOGGER.info("Operating System: {} ({}) version {}", OS_NAME, OS_ARCH, OS_VERSION);
        LOGGER.info("Java Version: {} ({}), {}", JAVA_VERSION, JAVA_VM_VERSION, JAVA_VENDOR);
        LOGGER.info("JVM Information: {} ({}), {}", JAVA_VM_NAME, JAVA_VM_INFO, JAVA_VM_VENDOR);
        long maxMemory = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getMax();
        LOGGER.info("Max Heap Memory: {} bytes ({} MB)", maxMemory, maxMemory >> 20);
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        List<String> jvmFlags = runtimeMXBean.getInputArguments();
        LOGGER.info("JVM Flags ({} totals): {}", jvmFlags.size(), StringUtils.join(jvmFlags, ' '));
        LOGGER.info("------------------------------");
    }

    private static void forceInit(Class<?> classToInit) {
        try {
            Class.forName(classToInit.getName(), true,
                    classToInit.getClassLoader());
        } catch (ClassNotFoundException e) {
            throw new Error(e);
        }
    }

    public void exit() {
        exit(false);
    }

    public void exit(boolean force) {
        LOGGER.info("Exiting application (force: {}).", force);

        appLifecycle.appClosing();

        if (!force) {
            MutableBoolean cancelled = new MutableBoolean();
            appLifecycle.canExitApp(cancelled);
            if (cancelled.isTrue()) {
                LOGGER.info("Cancelled exit application.");
                return;
            }
        }

        ProjectManager.getInstance().closeAllProjects();
        INSTANCE.saveServices();

        appLifecycle.appWillBeClosed();

        Disposer.dispose(INSTANCE);
        Disposer.checkAllDisposed();

        LOGGER.info("Exited application.");
        System.exit(0);
    }

    private static String getImplementationVersion() {
        String version = Peach.class.getPackage().getImplementationVersion();
        return version != null && !version.isEmpty() ? version : "99.0.0.0-INDEV";
    }

    @Override
    protected MessageBusImpl createMessageBus() {
        return new CompositeMessageBus();
    }

    @Override
    protected List<ServiceDescriptor> getPluginServices(Plugin plugin) {
        return plugin.getApplicationServices();
    }

    @Override
    protected List<ListenerDescriptor> getPluginListeners(Plugin plugin) {
        return plugin.getApplicationListeners();
    }
}
