package com.github.mouse0w0.peach.mcmod.fileEditor;

import com.github.mouse0w0.peach.fileEditor.FileEditorWithButtonBar;
import com.github.mouse0w0.peach.l10n.AppL10n;
import com.github.mouse0w0.peach.mcmod.project.ModProjectMetadata;
import com.github.mouse0w0.peach.mcmod.project.ModProjectService;
import com.github.mouse0w0.peach.mcmod.ui.form.TextureField;
import com.github.mouse0w0.peach.mcmod.util.ModUtils;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.ui.form.ColSpan;
import com.github.mouse0w0.peach.ui.form.Form;
import com.github.mouse0w0.peach.ui.form.FormView;
import com.github.mouse0w0.peach.ui.form.Section;
import com.github.mouse0w0.peach.ui.form.field.ChoiceBoxField;
import com.github.mouse0w0.peach.ui.form.field.ComboBoxField;
import com.github.mouse0w0.peach.ui.form.field.TextFieldField;
import com.github.mouse0w0.peach.ui.util.Check;
import javafx.scene.Node;
import javafx.util.StringConverter;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Locale;

public class MetadataFileEditor extends FileEditorWithButtonBar {
    private final ModProjectService descriptor;
    private final ModProjectMetadata metadata;

    private Form form;

    private TextFieldField name;
    private TextFieldField id;
    private TextFieldField version;
    private ChoiceBoxField<String> mcVersion;
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
        name.setValue(metadata.getName());

        id = new TextFieldField();
        id.setLabel(AppL10n.localize("metadata.general.id"));
        id.setColSpan(ColSpan.HALF);
        id.getChecks().add(Check.of(AppL10n.localize("validate.invalidModId"), ModUtils::validateIdentifier));
        id.setValue(metadata.getId());

        version = new TextFieldField();
        version.setLabel(AppL10n.localize("metadata.general.version"));
        version.setColSpan(ColSpan.HALF);
        version.setValue(metadata.getVersion());

        mcVersion = new ChoiceBoxField<>();
        mcVersion.setLabel(AppL10n.localize("metadata.general.mcVersion"));
        mcVersion.setColSpan(ColSpan.HALF);
        mcVersion.getItems().add("1.12.2");
        mcVersion.setValue(metadata.getMcVersion());

        language = new ComboBoxField<>();
        language.setLabel(AppL10n.localize("metadata.general.language"));
        language.setColSpan(ColSpan.HALF);
        language.setConverter(new StringConverter<>() {
            @Override
            public String toString(Locale object) {
                return object.getDisplayName() + " (" + object.toLanguageTag() + ")";
            }

            @Override
            public Locale fromString(String string) {
                throw new UnsupportedOperationException();
            }
        });
        language.getItems().addAll(Locale.getAvailableLocales());
        language.setValue(metadata.getLanguage());

        authors = new TextFieldField();
        authors.setLabel(AppL10n.localize("metadata.general.author"));
        authors.setColSpan(ColSpan.HALF);
        authors.setValue(metadata.getAuthor());

        description = new TextFieldField();
        description.setLabel(AppL10n.localize("metadata.general.description"));
        description.setValue(metadata.getDescription());

        credits = new TextFieldField();
        credits.setLabel(AppL10n.localize("metadata.general.credits"));
        credits.setValue(metadata.getCredits());

        url = new TextFieldField();
        url.setLabel(AppL10n.localize("metadata.general.url"));
        url.setValue(metadata.getUrl());

        updateUrl = new TextFieldField();
        updateUrl.setLabel(AppL10n.localize("metadata.general.updateUrl"));
        updateUrl.setValue(metadata.getUpdateUrl());

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

        return new FormView(form);
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
