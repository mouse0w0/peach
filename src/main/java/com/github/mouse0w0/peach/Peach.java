package com.github.mouse0w0.peach;

import com.github.mouse0w0.eventbus.EventBus;
import com.github.mouse0w0.eventbus.SimpleEventBus;
import com.github.mouse0w0.eventbus.asm.AsmEventListenerFactory;
import com.github.mouse0w0.i18n.I18n;
import com.github.mouse0w0.i18n.Translator;
import com.github.mouse0w0.i18n.source.ClasspathFileTranslationSource;
import com.github.mouse0w0.peach.forge.ForgeProjectService;
import com.github.mouse0w0.peach.service.ServiceManagerImpl;
import com.github.mouse0w0.peach.ui.FXApplication;
import com.github.mouse0w0.peach.ui.project.WindowManager;
import javafx.application.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

public class Peach extends ServiceManagerImpl {

    public static final Logger LOGGER = LoggerFactory.getLogger(Peach.class);

    private static Peach INSTANCE;

    private static final EventBus EVENT_BUS = SimpleEventBus.builder()
            .eventListenerFactory(AsmEventListenerFactory.create()).build();

    public static EventBus getEventBus() {
        return EVENT_BUS;
    }

    public static Peach getInstance() {
        return INSTANCE;
    }

    public static void main(String[] args) {
        LOGGER.info("Locale: {}", Locale.getDefault());
        INSTANCE = new Peach();
        Application.launch(FXApplication.class, args);
    }

    public Peach() {
        I18n.setTranslator(Translator.builder()
                .locale(Locale.getDefault())
                .source(new ClasspathFileTranslationSource("lang"))
                .build());

        initServices();
    }

    private void initServices() {
        registerService(RecentProjectsManager.class, new RecentProjectsManager());
        registerService(WindowManager.class, new WindowManager());
        registerService(ForgeProjectService.class, new ForgeProjectService());
    }
}
