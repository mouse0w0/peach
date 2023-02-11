package com.github.mouse0w0.peach;

import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.i18n.Translator;
import com.github.mouse0w0.i18n.source.ClasspathFileTranslationSource;
import com.github.mouse0w0.peach.application.AppLifecycleListener;
import com.github.mouse0w0.peach.component.ComponentManagerImpl;
import com.github.mouse0w0.peach.component.ServiceDescriptor;
import com.github.mouse0w0.peach.dispose.Disposer;
import com.github.mouse0w0.peach.extension.ExtensionException;
import com.github.mouse0w0.peach.extension.Extensions;
import com.github.mouse0w0.peach.javafx.FXApplication;
import com.github.mouse0w0.peach.message.MessageBus;
import com.github.mouse0w0.peach.message.impl.MessageBusFactory;
import com.github.mouse0w0.peach.project.ProjectManager;
import com.github.mouse0w0.peach.util.StringUtils;
import com.github.mouse0w0.version.Version;
import javafx.application.Application;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;

import static org.apache.commons.lang3.SystemUtils.*;

public final class Peach extends ComponentManagerImpl {
    private static final Logger LOGGER = LoggerFactory.getLogger(Peach.class);

    private static final Peach INSTANCE = new Peach();

    private final Version version = new Version(getImplementationVersion());
    private final Path userPropertiesPath = Paths.get(SystemUtils.USER_HOME, ".peach");

    private MessageBus messageBus = MessageBusFactory.create(null);

    public static Peach getInstance() {
        return INSTANCE;
    }

    public static void main(String[] args) {
        LOGGER.info("Initializing application...");
        initUncaughtExceptionHandler();
        printSystemInfo();
        initTranslator();
        LOGGER.info("Initializing extensions.");
        INSTANCE.initExtensions();
        LOGGER.info("Initializing application services.");
        INSTANCE.initServices(ServiceDescriptor.APPLICATION_SERVICE.getExtensions());
        LOGGER.info("Initializing JavaFX.");
        Application.launch(FXApplication.class, args);
    }

    private static void initUncaughtExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler((t, e) ->
                LOGGER.error("Uncaught exception detected in thread: " + t.getName(), e));
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

    private static void initTranslator() {
        I18n.setTranslator(Translator.builder()
                .locale(Locale.getDefault())
                .source(new ClasspathFileTranslationSource("lang"))
                .build());
    }

    private Peach() {
        super(null);
    }

    public Version getVersion() {
        return version;
    }

    public Path getUserPropertiesPath() {
        return userPropertiesPath;
    }

    public MessageBus getMessageBus() {
        return messageBus;
    }

    public void exit() {
        exit(false);
    }

    public void exit(boolean force) {
        LOGGER.info("Exiting application (force: {}).", force);

        getMessageBus().getPublisher(AppLifecycleListener.TOPIC).appClosing();

        if (!force) {
            MutableBoolean cancelled = new MutableBoolean();
            getMessageBus().getPublisher(AppLifecycleListener.TOPIC).canExitApp(cancelled);
            if (cancelled.isTrue()) {
                LOGGER.info("Cancelled exit application.");
                return;
            }
        }

        ProjectManager.getInstance().closeAllProjects();
        INSTANCE.saveComponents();

        getMessageBus().getPublisher(AppLifecycleListener.TOPIC).appWillBeClosed();

        Disposer.dispose(INSTANCE);
        Disposer.checkAllDisposed();

        LOGGER.info("Exited application.");
        System.exit(0);
    }

    private void initExtensions() {
        URL url = Peach.class.getResource("/application.xml");
        Element plugin;
        try {
            plugin = new SAXReader().read(url).getRootElement();
        } catch (DocumentException e) {
            throw new ExtensionException("Cannot load extensions from " + url, e);
        }

        Extensions.registerExtensionPoints(plugin.element("extensionPoints"));
        Extensions.registerExtensions(plugin.element("extensions"));
    }

    private String getImplementationVersion() {
        String version = Peach.class.getPackage().getImplementationVersion();
        return version != null && !version.isEmpty() ? version : "99.0.0.0-INDEV";
    }

    @Override
    public void dispose() {
        disposeComponents();

        if (messageBus != null) {
            Disposer.dispose(messageBus);
            messageBus = null;
        }
    }
}
