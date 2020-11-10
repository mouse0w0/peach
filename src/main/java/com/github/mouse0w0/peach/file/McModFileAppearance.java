package com.github.mouse0w0.peach.file;

import com.github.mouse0w0.peach.mcmod.element.ElementRegistry;
import com.github.mouse0w0.peach.ui.icon.Icons;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

import java.nio.file.Path;

public class McModFileAppearance implements FileAppearance {
    @Override
    public boolean accept(Path file) {
        return ElementRegistry.getInstance().getElementType(file) != null;
    }

    @Override
    public void apply(Path file, StringProperty text, ObjectProperty<Image> icon) {
        text.set(file.getFileName().toString());
        icon.set(Icons.File.ModElement);
    }
}
