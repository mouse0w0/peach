package com.github.mouse0w0.peach.mcmod.preview;

import com.github.mouse0w0.peach.mcmod.Identifier;
import com.github.mouse0w0.peach.mcmod.element.impl.BlockElement;
import com.github.mouse0w0.peach.mcmod.element.impl.ItemElement;
import com.github.mouse0w0.peach.mcmod.model.Model;
import com.github.mouse0w0.peach.mcmod.model.ModelLoader;
import com.github.mouse0w0.peach.mcmod.model.ModelManager;
import com.github.mouse0w0.peach.mcmod.model.ModelTemplate;
import com.github.mouse0w0.peach.mcmod.util.ResourceUtils;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.ui.util.ImageUtils;
import com.github.mouse0w0.peach.util.StringUtils;
import com.github.mouse0w0.softwarerenderer.texture.RgbaTexture2D;
import com.github.mouse0w0.softwarerenderer.texture.Texture2D;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class PreviewManagerImpl implements PreviewManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(PreviewManagerImpl.class);

    private final Project project;
    private final ModelManager modelManager;
    private final ItemModelRenderer itemModelRenderer = new ItemModelRenderer(64, 64);

    public PreviewManagerImpl(Project project, ModelManager modelManager) {
        this.project = project;
        this.modelManager = modelManager;
    }

    @Override
    public Image getBlockImage(BlockElement element) {
        Identifier itemModelId = element.getItemModel();
        if (ModelManager.DEFAULT.equals(itemModelId)) {
            return getBlockImage(element.getModel(), element.getCustomModels(), element.getTextures());
        } else {
            return getItemImage(itemModelId, element.getCustomItemModels(), element.getItemTextures());
        }
    }

    @Override
    public Image getItemImage(ItemElement element) {
        return getItemImage(element.getModel(), element.getCustomModels(), element.getTextures());
    }

    private Image getBlockImage(Identifier modelId, Map<String, String> customModels, Map<String, String> textures) {
        if (ModelManager.CUSTOM.equals(modelId)) {

        } else {
            ModelTemplate modelTemplate = modelManager.getModelTemplate(modelId);
            if (StringUtils.isNotEmpty(modelTemplate.getPreview())) {
                return getPreviewModelImage(modelTemplate, textures);
            } else {

            }
        }
        return ResourceUtils.MISSING_TEXTURE;
    }

    private Image getItemImage(Identifier modelId, Map<String, String> customModels, Map<String, String> textures) {
        if (ModelManager.GENERATED.equals(modelId) || ModelManager.HANDHELD.equals(modelId)) {
            return loadImage(ResourceUtils.getTextureFile(project, textures.get("texture")));
        } else if (ModelManager.CUSTOM.equals(modelId)) {
            Path modelPath = ResourceUtils.getModelFile(project, customModels.get("item"));
            if (Files.exists(modelPath)) {
                try {
                    Model itemModel = ModelLoader.load(modelPath);
                    itemModel.getTextures().putAll(textures);
                    return SwingFXUtils.toFXImage(itemModelRenderer.render(
                                    itemModel,
                                    loadTextures(textures)::get,
                                    ItemModelRenderer.TintGetter.DEFAULT,
                                    false),
                            new WritableImage(64, 64));
                } catch (IOException ignored) {
                }
            }
        } else {
            ModelTemplate modelTemplate = modelManager.getModelTemplate(modelId);
            if (StringUtils.isNotEmpty(modelTemplate.getPreview())) {
                return getPreviewModelImage(modelTemplate, textures);
            } else {
                ModelTemplate.Entry modelTemplateEntry = modelTemplate.getModels().get("item");
                if (modelTemplateEntry != null) {

                }
            }
        }
        return ResourceUtils.MISSING_TEXTURE;
    }

    private Image getPreviewModelImage(ModelTemplate modelTemplate, Map<String, String> textures) {
        URL resource = modelTemplate.getPlugin().getClassLoader().getResource("model/preview/" + modelTemplate.getPreview());
        if (resource != null) {
            try {
                Model previewModel = ModelLoader.load(resource);
                previewModel.setTextures(textures);
                return SwingFXUtils.toFXImage(itemModelRenderer.render(
                                previewModel,
                                loadTextures(textures)::get,
                                ItemModelRenderer.TintGetter.DEFAULT,
                                false),
                        new WritableImage(64, 64));
            } catch (IOException ignored) {
            }
        }
        return ResourceUtils.MISSING_TEXTURE;
    }

    private Image loadImage(Path path) {
        if (Files.notExists(path)) {
            return ResourceUtils.MISSING_TEXTURE;
        } else {
            return ImageUtils.loadImage(path, 64, 64, true, false);
        }
    }

    private Map<String, Texture2D> loadTextures(Map<String, String> textures) {
        Map<String, Texture2D> result = new HashMap<>();
        for (String texturePath : textures.values()) {
            result.computeIfAbsent(texturePath, this::loadTexture);
        }
        return result;
    }

    private Texture2D loadTexture(String texture) {
        Path textureFile = ResourceUtils.getTextureFile(project, texture);
        if (Files.notExists(textureFile)) return null;
        try (InputStream is = Files.newInputStream(textureFile)) {
            return new RgbaTexture2D(ImageIO.read(is));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
