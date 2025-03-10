package com.github.mouse0w0.peach.mcmod.fileEditor;

import com.github.mouse0w0.peach.fileEditor.FileEditorWithButtonBar;
import com.github.mouse0w0.peach.l10n.AppL10n;
import com.github.mouse0w0.peach.mcmod.project.ModProjectMetadata;
import com.github.mouse0w0.peach.mcmod.project.ModProjectService;
import com.github.mouse0w0.peach.mcmod.ui.form.TextureField;
import com.github.mouse0w0.peach.mcmod.util.ModIdUtils;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.ui.form.ColSpan;
import com.github.mouse0w0.peach.ui.form.Form;
import com.github.mouse0w0.peach.ui.form.FormView;
import com.github.mouse0w0.peach.ui.form.Section;
import com.github.mouse0w0.peach.ui.form.field.ComboBoxField;
import com.github.mouse0w0.peach.ui.form.field.TextFieldField;
import com.github.mouse0w0.peach.ui.util.Check;
import javafx.scene.Node;
import javafx.util.StringConverter;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class MetadataFileEditor extends FileEditorWithButtonBar {
    private static final List<Locale> LOCALES;

    static {
        List<Locale> locales = new ArrayList<>();
        for (Locale locale : Locale.getAvailableLocales()) {
            if (locale.getLanguage().isEmpty()) continue;
            if (!locale.getVariant().isEmpty()) continue;
            if (!locale.getScript().isEmpty()) continue;
            if (locale.hasExtensions()) continue;
            locales.add(locale);
        }
        locales.sort(Comparator.comparing(Locale::toString));
        LOCALES = locales;
    }

    private final ModProjectService descriptor;
    private final ModProjectMetadata metadata;

    private Form form;

    private TextFieldField name;
    private TextFieldField id;
    private TextFieldField version;
    private ComboBoxField<String> mcVersion;
    private ComboBoxField<Locale> language;
    private TextFieldField authors;
    private TextFieldField description;
    private TextFieldField credits;
    private TextFieldField url;
    private TextFieldField updateUrl;
    private TextureField logo;

    public MetadataFileEditor(@NotNull Project project, @NotNull Path file) {
        super(project, file);

        descriptor = ModProjectService.getInstance(project);
        metadata = descriptor.getMetadata();
    }

    @Override
    protected Node getContent() {
        form = new Form();

        name = new TextFieldField();
        name.setLabel(AppL10n.localize("metadata.general.name"));
        name.setColSpan(ColSpan.HALF);

        id = new TextFieldField();
        id.setLabel(AppL10n.localize("metadata.general.id"));
        id.setColSpan(ColSpan.HALF);
        id.getChecks().add(Check.of(AppL10n.localize("validate.invalidModId"), ModIdUtils::validateModId));

        version = new TextFieldField();
        version.setLabel(AppL10n.localize("metadata.general.version"));
        version.setColSpan(ColSpan.HALF);

        mcVersion = new ComboBoxField<>();
        mcVersion.setLabel(AppL10n.localize("metadata.general.mcVersion"));
        mcVersion.setColSpan(ColSpan.HALF);
        mcVersion.getItems().add("1.12.2");

        language = new ComboBoxField<>();
        language.setLabel(AppL10n.localize("metadata.general.language"));
        language.setColSpan(ColSpan.HALF);
        language.setConverter(new StringConverter<>() {
            @Override
            public String toString(Locale object) {
                return object.getDisplayName(object) + " (" + object + ")";
            }

            @Override
            public Locale fromString(String string) {
                throw new UnsupportedOperationException();
            }
        });
        language.getItems().addAll(LOCALES);

        authors = new TextFieldField();
        authors.setLabel(AppL10n.localize("metadata.general.author"));
        authors.setColSpan(ColSpan.HALF);

        description = new TextFieldField();
        description.setLabel(AppL10n.localize("metadata.general.description"));

        credits = new TextFieldField();
        credits.setLabel(AppL10n.localize("metadata.general.credits"));

        url = new TextFieldField();
        url.setLabel(AppL10n.localize("metadata.general.url"));

        updateUrl = new TextFieldField();
        updateUrl.setLabel(AppL10n.localize("metadata.general.updateUrl"));

        Section general = new Section();
        general.setText(AppL10n.localize("metadata.general.title"));
        general.getElements().addAll(
                name, id,
                version, mcVersion,
                language, authors,
                description,
                credits,
                url,
                updateUrl);

        form.getGroups().add(general);

        initialize();

        return new FormView(form);
    }

    private void initialize() {
        name.setValue(metadata.getName());
        id.setValue(metadata.getId());
        version.setValue(metadata.getVersion());
        mcVersion.setValue(metadata.getMcVersion());
        language.setValue(metadata.getLanguage());
        authors.setValue(metadata.getAuthor());
        description.setValue(metadata.getDescription());
        credits.setValue(metadata.getCredits());
        url.setValue(metadata.getUrl());
        updateUrl.setValue(metadata.getUpdateUrl());
    }

    @Override
    protected void onApply() {
        metadata.setName(name.getValue());
        metadata.setId(id.getValue());
        metadata.setVersion(version.getValue());
        metadata.setMcVersion(mcVersion.getValue());
        metadata.setDescription(description.getValue());
        metadata.setAuthor(authors.getValue());
        metadata.setLanguage(language.getValue());
        metadata.setUrl(url.getValue());
        metadata.setUpdateUrl(updateUrl.getValue());
        metadata.setCredits(credits.getValue());
        descriptor.saveMetadata();
    }

    @Override
    protected boolean validate() {
        return form.validate();
    }
}
