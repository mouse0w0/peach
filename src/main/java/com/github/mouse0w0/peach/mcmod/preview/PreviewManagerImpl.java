package com.github.mouse0w0.peach.mcmod.preview;

import com.github.mouse0w0.peach.mcmod.Identifier;
import com.github.mouse0w0.peach.mcmod.element.impl.BlockElement;
import com.github.mouse0w0.peach.mcmod.element.impl.ItemElement;
import com.github.mouse0w0.peach.mcmod.model.*;
import com.github.mouse0w0.peach.mcmod.util.ResourceUtils;
import com.github.mouse0w0.peach.plugin.Plugin;
import com.github.mouse0w0.peach.project.Project;
import com.github.mouse0w0.peach.ui.util.ImageUtils;
import com.github.mouse0w0.peach.util.StringUtils;
import com.github.mouse0w0.softwarerenderer.texture.RgbaTexture2D;
import com.github.mouse0w0.softwarerenderer.texture.Texture2D;
import com.github.mouse0w0.softwarerenderer.util.FXUtils;
import com.google.common.collect.ImmutableSet;
import javafx.scene.image.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
    public Image renderBlockItem(BlockElement element) {
        Identifier itemModel = element.getItemModel();
        if (ModelManager.DEFAULT.equals(itemModel)) {
            return renderBlockItemModel(element);
        } else if (ModelManager.CUSTOM.equals(itemModel)) {
            return renderCustomItemModel(element.getCustomItemModels(), element.getItemTextures());
        } else {
            return renderItemModel(itemModel, element.getItemTextures());
        }
    }

    @Override
    public Image renderItem(ItemElement element) {
        Identifier model = element.getModel();
        if (ModelManager.CUSTOM.equals(model)) {
            return renderCustomItemModel(element.getCustomModels(), element.getTextures());
        } else {
            return renderItemModel(model, element.getTextures());
        }
    }

    private Image renderCustomItemModel(Map<String, String> customModels, Map<String, String> textures) {
        Model model = loadModelFromProject(customModels.get("item"));
        if (model == null) {
            return ResourceUtils.MISSING_TEXTURE;
        }

        return renderModel(model, textures);
    }

    private Image renderItemModel(Identifier model, Map<String, String> textures) {
        ModelTemplate modelTemplate = modelManager.getModelTemplate(model);
        if (StringUtils.isEmpty(modelTemplate.getPreview())) {
            return ResourceUtils.MISSING_TEXTURE;
        }

        return renderModelTemplate(modelTemplate, null, textures);
    }

    private Image renderBlockItemModel(BlockElement element) {
        Identifier modelId = element.getModel();
        if (!ModelManager.CUSTOM.equals(modelId)) {
            ModelTemplate modelTemplate = modelManager.getModelTemplate(modelId);
            if (StringUtils.isNotEmpty(modelTemplate.getPreview())) {
                return renderModelTemplate(modelTemplate, null, element.getTextures());
            }

            Identifier itemModelId = modelTemplate.getItem();
            if (itemModelId != null) {
                ModelTemplate itemModelTemplate = modelManager.getModelTemplate(itemModelId);
                if (StringUtils.isNotEmpty(itemModelTemplate.getPreview())) {
                    Map<String, String> compositedTextures = new HashMap<>();
                    compositedTextures.putAll(element.getTextures());
                    compositedTextures.putAll(element.getItemTextures());
                    return renderModelTemplate(itemModelTemplate, null, compositedTextures);
                }
            }
        }
        return renderBlockstateItemModelTemplate(element);
    }

    private Image renderBlockstateItemModelTemplate(BlockElement element) {
        String blockstate = element.getType().getBlockstate();
        BlockstateTemplate blockstateTemplate = modelManager.getBlockstateTemplate(blockstate);
        Identifier blockstateItemModelId = blockstateTemplate.getItem();
        ModelTemplate blockstateItemModelTemplate = modelManager.getModelTemplate(blockstateItemModelId);
        if (StringUtils.isNotEmpty(blockstateItemModelTemplate.getPreview())) {
            return renderModelTemplate(blockstateItemModelTemplate, element.getCustomModels(), element.getTextures());
        }

        return ResourceUtils.MISSING_TEXTURE;
    }

    private Image renderModelTemplate(ModelTemplate modelTemplate, Map<String, String> customModels, Map<String, String> textures) {
        Model model;
        String preview = modelTemplate.getPreview();
        if (preview.charAt(0) == '#') {
            if (customModels == null) {
                LOGGER.warn("No support rendering custom model");
                return ResourceUtils.MISSING_TEXTURE;
            }
            model = loadModelFromProject(customModels.get(preview.substring(1)));
        } else {
            model = loadModelFromPlugin(modelTemplate.getPlugin(), "model/preview/" + preview);
        }

        if (model == null) {
            return ResourceUtils.MISSING_TEXTURE;
        }

        return renderModel(model, textures);
    }

    public static final Set<Identifier> GENERATED_MODEL = ImmutableSet.of(
            Identifier.of("minecraft:builtin/generated"),
            Identifier.of("minecraft:item/generated"),
            Identifier.of("minecraft:item/handheld")
    );

    private Image renderModel(Model model, Map<String, String> textures) {
        Map<String, String> modelTextures = model.getTextures();
        if (modelTextures != null) {
            modelTextures.putAll(textures);
        } else {
            model.setTextures(textures);
        }

        if (GENERATED_MODEL.contains(model.getParent())) {
            Path layer0 = ResourceUtils.getTextureFile(project, model.resolveTexture("layer0"));
            if (Files.notExists(layer0)) {
                return ResourceUtils.MISSING_TEXTURE;
            }

            return ImageUtils.loadImage(layer0, 64, 64, true, false);
        }

        return FXUtils.toWritableImage(itemModelRenderer.render(
                model,
                loadTextures(textures)::get,
                ItemModelRenderer.TintGetter.DEFAULT,
                false
        ));
    }

    private Model loadModelFromProject(String model) {
        Path modelFile = ResourceUtils.getModelFile(project, model);
        if (Files.notExists(modelFile)) {
            LOGGER.warn("Not found model file, path={}", modelFile);
            return null;
        }

        try {
            return ModelLoader.load(modelFile);
        } catch (IOException e) {
            LOGGER.warn("Cannot load model.", e);
            return null;
        }
    }

    private Model loadModelFromPlugin(Plugin plugin, String path) {
        URL resource = plugin.getClassLoader().getResource(path);
        if (resource == null) {
            LOGGER.warn("Not found model file. plugin={}, path={}.", plugin, path);
            return null;
        }

        try {
            return ModelLoader.load(resource);
        } catch (IOException e) {
            LOGGER.warn("Cannot load model.", e);
            return null;
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
        if (texture == null) {
            return null;
        }
        Path textureFile = ResourceUtils.getTextureFile(project, texture);
        if (Files.notExists(textureFile)) {
            LOGGER.warn("Not found texture file, path={}", textureFile);
            return null;
        }
        try (InputStream is = Files.newInputStream(textureFile)) {
            return new RgbaTexture2D(ImageIO.read(is));
        } catch (IOException e) {
            LOGGER.warn("Cannot load texture", e);
            return null;
        }
    }
}
