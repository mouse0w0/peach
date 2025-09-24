package com.github.mouse0w0.peach.mcmod.fileEditor;

import com.github.mouse0w0.peach.fileEditor.FileEditorWithButtonBar;
import com.github.mouse0w0.peach.mcmod.project.ModProjectMetadata;
import com.github.mouse0w0.peach.mcmod.project.ModProjectService;
import com.github.mouse0w0.peach.mcmod.util.ModIdUtils;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.ui.util.Validator;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.*;

import static com.github.mouse0w0.peach.l10n.AppL10n.localize;
import static com.github.mouse0w0.peach.ui.layout.Form.form;
import static com.github.mouse0w0.peach.ui.layout.FormItem.half;
import static com.github.mouse0w0.peach.ui.layout.FormItem.one;
import static com.github.mouse0w0.peach.ui.layout.LayoutUtils.scrollVBox;
import static com.github.mouse0w0.peach.ui.layout.LayoutUtils.titled;

public class MetadataFileEditor extends FileEditorWithButtonBar {
    private static final List<Locale> LOCALES = new ArrayList<>();
    private static final Map<Locale, String> LOCALE_DISPLAY_NAME = new HashMap<>();

    static {
        for (Locale locale : Locale.getAvailableLocales()) {
            if (locale.getLanguage().isEmpty()) continue;
            if (locale.getCountry().isEmpty()) continue;
            if (!locale.getVariant().isEmpty()) continue;
            if (!locale.getScript().isEmpty()) continue;
            if (locale.hasExtensions()) continue;
            LOCALES.add(locale);
            // TODO: Load it from file
            LOCALE_DISPLAY_NAME.put(locale, locale.getDisplayName(locale));
        }
        LOCALES.sort(Comparator.comparing(Locale::toString));
    }

    private final ModProjectService descriptor;
    private final ModProjectMetadata metadata;

    private TextField name;
    private TextField id;
    private TextField version;
    private ComboBox<String> mcVersion;
    private ComboBox<Locale> language;
    private TextField authors;
    private TextField description;
    private TextField credits;
    private TextField url;
    private TextField updateUrl;

    public MetadataFileEditor(@NotNull Project project, @NotNull Path file) {
        super(project, file);

        descriptor = ModProjectService.getInstance(project);
        metadata = descriptor.getMetadata();
    }

    @Override
    protected Node getContent() {
        name = new TextField();
        id = new TextField();
        version = new TextField();
        mcVersion = new ComboBox<>();
        language = new ComboBox<>();
        authors = new TextField();
        description = new TextField();
        credits = new TextField();
        url = new TextField();
        updateUrl = new TextField();

        Validator.of(id, localize("validate.invalidModId"), ModIdUtils::validateModId);

        mcVersion.getItems().add("1.12.2");

        language.setConverter(new StringConverter<>() {
            @Override
            public String toString(Locale object) {
                return LOCALE_DISPLAY_NAME.get(object) + " (" + object + ")";
            }

            @Override
            public Locale fromString(String string) {
                throw new UnsupportedOperationException();
            }
        });
        language.getItems().addAll(LOCALES);

        initialize();

        return scrollVBox(
                titled(localize("metadata.general.title"), form(
                        half(localize("metadata.general.name"), name),
                        half(localize("metadata.general.id"), id),
                        half(localize("metadata.general.version"), version),
                        half(localize("metadata.general.mcVersion"), mcVersion),
                        half(localize("metadata.general.language"), language),
                        half(localize("metadata.general.author"), authors),
                        one(localize("metadata.general.description"), description),
                        one(localize("metadata.general.credits"), credits),
                        one(localize("metadata.general.url"), url),
                        one(localize("metadata.general.updateUrl"), updateUrl)
                ))
        );
    }

    private void initialize() {
        name.setText(metadata.getName());
        id.setText(metadata.getId());
        version.setText(metadata.getVersion());
        mcVersion.setValue(metadata.getMcVersion());
        language.setValue(metadata.getLanguage());
        authors.setText(metadata.getAuthor());
        description.setText(metadata.getDescription());
        credits.setText(metadata.getCredits());
        url.setText(metadata.getUrl());
        updateUrl.setText(metadata.getUpdateUrl());
    }

    @Override
    protected void onApply() {
        metadata.setName(name.getText());
        metadata.setId(id.getText());
        metadata.setVersion(version.getText());
        metadata.setMcVersion(mcVersion.getValue());
        metadata.setDescription(description.getText());
        metadata.setAuthor(authors.getText());
        metadata.setLanguage(language.getValue());
        metadata.setUrl(url.getText());
        metadata.setUpdateUrl(updateUrl.getText());
        metadata.setCredits(credits.getText());
        descriptor.saveMetadata();
    }

    @Override
    protected boolean validate() {
        return Validator.validate(id);
    }
}
