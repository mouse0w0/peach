package com.github.mouse0w0.peach.mcmod.ui.control.skin;

import com.github.mouse0w0.peach.mcmod.ui.control.TextureView;
import com.github.mouse0w0.peach.mcmod.util.ResourceUtils;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.ui.util.CachedImage;
import javafx.scene.control.SkinBase;
import javafx.scene.image.ImageView;

import java.nio.file.Path;

public class TextureViewSkin extends SkinBase<TextureView> {

    private ImageView imageView;

    public TextureViewSkin(TextureView textureView) {
        super(textureView);

        imageView = new ImageView();
        imageView.fitWidthProperty().bind(textureView.fitWidthProperty());
        imageView.fitHeightProperty().bind(textureView.fitHeightProperty());
        getChildren().add(imageView);

        textureView.textureProperty().addListener(observable -> updateImage());
        updateImage();
    }

    private void updateImage() {
        Path textureFile = getTextureFile(getTexture());
        imageView.setImage(new CachedImage(textureFile, getFitWidth(), getFitHeight()).getImage());
    }

    private Path getTextureFile(String textureName) {
        return ResourceUtils.getTextureFile(getProject(), textureName);
    }

    public Project getProject() {
        return getSkinnable().getProject();
    }

    public String getTexture() {
        return getSkinnable().getTexture();
    }

    public double getFitWidth() {
        return getSkinnable().getFitWidth();
    }

    public double getFitHeight() {
        return getSkinnable().getFitHeight();
    }

    @Override
    public void dispose() {

    }
}
