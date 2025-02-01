package com.github.mouse0w0.peach.mcmod.preview;

import com.github.mouse0w0.peach.mcmod.Direction;
import com.github.mouse0w0.peach.mcmod.Identifier;
import com.github.mouse0w0.peach.mcmod.model.Model;
import com.github.mouse0w0.peach.mcmod.model.ModelElement;
import com.github.mouse0w0.peach.mcmod.model.ModelFace;
import com.github.mouse0w0.peach.mcmod.model.ModelTransform;
import com.github.mouse0w0.softwarerenderer.BlendMode;
import com.github.mouse0w0.softwarerenderer.CullMode;
import com.github.mouse0w0.softwarerenderer.Renderer;
import com.github.mouse0w0.softwarerenderer.framebuffer.DefaultFrameBuffer;
import com.github.mouse0w0.softwarerenderer.sampler.WrapMode;
import com.github.mouse0w0.softwarerenderer.texture.FloatTexture2D;
import com.github.mouse0w0.softwarerenderer.texture.RgbaTexture2D;
import com.github.mouse0w0.softwarerenderer.texture.Texture2D;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.Map;
import java.util.function.Function;

public final class ItemModelRenderer {
    private static final Matrix4f IDENTIFY = new Matrix4f();

    private static final float SCALE_ROTATION_22_5 = 1.0f / (float) Math.cos(Math.PI / 8d) - 1.0f;
    private static final float SCALE_ROTATION_GENERAL = 1.0f / (float) Math.cos(Math.PI / 4d) - 1.0f;

    private static final float TO_RADIANS = (float) (Math.PI / 180.0);

    private static final int[][] VERTEX_DATA_INDICES = {
            {1, 0, 4, 1, 4, 5}, // Down (-Y)
            {2, 3, 7, 2, 7, 6}, // Up (+Y)
            {6, 4, 0, 6, 0, 2}, // North (-Z)
            {3, 1, 5, 3, 5, 7}, // South (+Z)
            {2, 0, 1, 2, 1, 3}, // West (-X)
            {7, 5, 4, 7, 4, 6}  // East (+X)
    };
    private static final int[] TEX_COORDS_INDICES = {0, 1, 3, 0, 3, 2};
    private static final int[] TEX_COORDS_90_INDICES = {1, 3, 2, 1, 2, 0};
    private static final int[] TEX_COORDS_180_INDICES = {3, 2, 0, 3, 0, 1};
    private static final int[] TEX_COORDS_270_INDICES = {2, 0, 1, 2, 1, 3};

    private static final RgbaTexture2D MISSING_TEXTURE = new RgbaTexture2D(2, 2, new float[]{
            0.972549f, 0, 0.972549f, 1, 0, 0, 0, 1,
            0, 0, 0, 1, 0.972549f, 0, 0.972549f, 1
    });

    private final Renderer<ItemModelVertex> renderer;
    private final ItemModelShader shader;
    private final DefaultFrameBuffer frameBuffer;

    public ItemModelRenderer(int width, int height) {
        renderer = new Renderer<>(ItemModelVertex::new);
        renderer.setDepthTest(true);
        renderer.setCullMode(CullMode.BACK);
        renderer.setBlendMode(BlendMode.SRC_OVER);

        shader = new ItemModelShader();
        shader.sampler.setWrapS(WrapMode.CLAMP_TO_EDGE);
        shader.sampler.setWrapT(WrapMode.CLAMP_TO_EDGE);
        renderer.setShader(shader);

        frameBuffer = new DefaultFrameBuffer(new RgbaTexture2D(width, height), new FloatTexture2D(width, height));
        renderer.setFrameBuffer(frameBuffer);
        renderer.setViewport(0, 0, width, height);
    }

    public Texture2D render(Model model,
                            Function<String, Texture2D> textureGetter,
                            TintGetter tintGetter,
                            boolean useBlockLight) {
        renderer.clearColor(0, 0, 0, 0);
        renderer.clearDepth(1);

        setupModelViewProjectionMatrix(model);

        if ("front".equals(model.resolveGuiLight())) {
            shader.setupGuiFlatLighting();
        } else {
            shader.setupGui3DLighting();
        }

        for (ModelElement element : model.resolveElements()) {
            Vector3f[] boxPositions = getBoxPositions(element);
            Map<Direction, ModelFace> faces = element.getFaces();
            for (Direction direction : faces.keySet()) {
                renderFace(model, direction, faces.get(direction), boxPositions, textureGetter, tintGetter, useBlockLight);
            }
        }

        return frameBuffer.getColorTexture();
    }

    private static final Matrix4f BLOCK_GUI_TRANSFORM = new Matrix4f()
            .rotateX((float) Math.toRadians(30))
            .rotateY((float) Math.toRadians(225))
            .scale(0.625f, 0.625f, 0.625f);

    private static final Identifier BLOCK_MODEL = Identifier.of("minecraft:block/block");

    private void setupModelViewProjectionMatrix(Model model) {
        Matrix4f modelMatrix = shader.modelMatrix;
        ModelTransform transform = model.resolveTransform("gui");
        if (transform != null) {
            transform.apply(modelMatrix.identity());
        } else if (BLOCK_MODEL.equals(model.getParent())) {
            modelMatrix.set(BLOCK_GUI_TRANSFORM);
        } else {
            modelMatrix.identity();
        }

        modelMatrix.translate(-0.5f, -0.5f, -0.5f);

        shader.modelViewProjectionMatrix.identity()
                .ortho(0f, 1f, 0f, 1f, -1000f, 1000f)
                .translate(0.5f, 0.5f, 0f)
                .mul(modelMatrix);
    }

    private final Vector4f tempTexCoord = new Vector4f();
    private final Vector4f tempColor = new Vector4f();
    private final Vector4f tempTint = new Vector4f();
    private final Vector3f tempAB = new Vector3f();
    private final Vector3f tempAC = new Vector3f();
    private final Vector3f tempNormal = new Vector3f();

    private void renderFace(Model model,
                            Direction direction,
                            ModelFace face,
                            Vector3f[] boxPositions,
                            Function<String, Texture2D> textureGetter,
                            TintGetter tintGetter,
                            boolean useBlockLight) {
        Vector4f texCoord = getTexCoord(face, tempTexCoord);
        Vector4f color = getFaceColor(direction, useBlockLight, tempColor);
        if (face.getTintIndex() != -1) {
            color.mul(tintGetter.getTint(face.getTintIndex(), tempTint));
        }
        int[] posIndices = VERTEX_DATA_INDICES[direction.ordinal()];
        int[] texCoordIndices = getTexCoordIndices(face);

        Texture2D texture = textureGetter.apply(model.resolveTexture(face.getTexture()));
        if (texture != null) {
            shader.sampler.setTexture(texture);
        } else {
            shader.sampler.setTexture(MISSING_TEXTURE);
        }

        boxPositions[posIndices[1]].sub(boxPositions[posIndices[0]], tempAB);
        boxPositions[posIndices[2]].sub(boxPositions[posIndices[0]], tempAC);
        tempAB.cross(tempAC, tempNormal);
        tempNormal.normalize();

        ItemModelVertex a = renderer.a();
        ItemModelVertex b = renderer.b();
        ItemModelVertex c = renderer.c();

        a.position.set(boxPositions[posIndices[0]], 1f);
        a.color.set(color);
        texCoord(texCoord, texCoordIndices[0], a.texCoord);
        a.normal.set(tempNormal);

        b.position.set(boxPositions[posIndices[1]], 1f);
        b.color.set(color);
        texCoord(texCoord, texCoordIndices[1], b.texCoord);
        b.normal.set(tempNormal);

        c.position.set(boxPositions[posIndices[2]], 1f);
        c.color.set(color);
        texCoord(texCoord, texCoordIndices[2], c.texCoord);
        c.normal.set(tempNormal);

        renderer.drawTriangle();

        a.position.set(boxPositions[posIndices[3]], 1f);
        a.color.set(color);
        texCoord(texCoord, texCoordIndices[3], a.texCoord);
        a.normal.set(tempNormal);

        b.position.set(boxPositions[posIndices[4]], 1f);
        b.color.set(color);
        texCoord(texCoord, texCoordIndices[4], b.texCoord);
        b.normal.set(tempNormal);

        c.position.set(boxPositions[posIndices[5]], 1f);
        c.color.set(color);
        texCoord(texCoord, texCoordIndices[5], c.texCoord);
        c.normal.set(tempNormal);

        renderer.drawTriangle();
    }

    private int[] getTexCoordIndices(ModelFace face) {
        switch (face.getRotation()) {
            default:
                return TEX_COORDS_INDICES;
            case 90:
                return TEX_COORDS_90_INDICES;
            case 180:
                return TEX_COORDS_180_INDICES;
            case 270:
                return TEX_COORDS_270_INDICES;
        }
    }

    private static Vector4f getFaceColor(Direction direction, boolean useBlockLight, Vector4f dest) {
        if (useBlockLight) {
            switch (direction) {
                case DOWN:
                    return dest.set(0.5f, 0.5f, 0.5f, 1f);
                case NORTH:
                case SOUTH:
                    return dest.set(0.8f, 0.8f, 0.8f, 1f);
                case WEST:
                case EAST:
                    return dest.set(0.6f, 0.6f, 0.6f, 1f);
                case UP:
                default:
                    return dest.set(1f, 1f, 1f, 1f);
            }
        } else {
            return dest.set(1f);
        }
    }

    private final Vector3f[] boxPositions = new Vector3f[]{
            new Vector3f(), new Vector3f(), new Vector3f(), new Vector3f(),
            new Vector3f(), new Vector3f(), new Vector3f(), new Vector3f()
    };

    private Vector3f[] getBoxPositions(ModelElement element) {
        Vector3f from = element.getFrom();
        Vector3f to = element.getTo();
        Matrix4f matrix = getRotationMatrix(element.getRotation());

        for (int i = 0; i < 8; i++) {
            getBoxPosition(from, to, i, boxPositions[i]).mulPosition(matrix);
        }
        return boxPositions;
    }

    private static Vector3f getBoxPosition(Vector3f from, Vector3f to, int index, Vector3f dest) {
        float x = (index & 0x4) == 0 ? from.x : to.x;
        float y = (index & 0x2) == 0 ? from.y : to.y;
        float z = (index & 0x1) == 0 ? from.z : to.z;
        return dest.set(x, y, z).div(16);
    }

    private final Matrix4f tempRotationMatrix = new Matrix4f();
    private final Vector3f tempScale = new Vector3f();
    private final Vector3f tempOrigin = new Vector3f();

    private Matrix4f getRotationMatrix(ModelElement.Rotation rotation) {
        if (rotation == null) return IDENTIFY;

        Matrix4f matrix = tempRotationMatrix.identity();
        Vector3f scale = tempScale;
        switch (rotation.getAxis()) {
            case X:
                matrix.rotateX(rotation.getAngle() * TO_RADIANS);
                scale.set(0f, 1f, 1f);
                break;
            case Y:
                matrix.rotateY(rotation.getAngle() * TO_RADIANS);
                scale.set(1f, 0f, 1f);
                break;
            case Z:
                matrix.rotateZ(rotation.getAngle() * TO_RADIANS);
                scale.set(1f, 1f, 0f);
                break;
        }

        if (rotation.isRescale()) {
            if (Math.abs(rotation.getAngle()) == 22.5F) {
                scale.mul(SCALE_ROTATION_22_5);
            } else {
                scale.mul(SCALE_ROTATION_GENERAL);
            }

            scale.add(1f, 1f, 1f);
        } else {
            scale.set(1f);
        }

        Vector3f origin = rotation.getOrigin().div(16, tempOrigin);
        return matrix.scale(scale).translateLocal(origin).translate(origin.negate());
    }

    private static Vector4f getTexCoord(ModelFace face, Vector4f dest) {
        Vector4f faceUv = face.getUv();
        if (faceUv != null) {
            return dest.set(normalizeTexCoord(faceUv.x), normalizeTexCoord(faceUv.y),
                    normalizeTexCoord(faceUv.z), normalizeTexCoord(faceUv.w));
        } else {
            return dest.set(0, 0, 1, 1);
        }
    }

    private static float normalizeTexCoord(float v) {
        if (v < 0f) return 0f;
        if (v > 16f) return 16f;
        return v / 16f;
    }

    private static void texCoord(Vector4f texCoord, int flag, Vector2f dest) {
        float u = (flag & 0x2) == 0 ? texCoord.x : texCoord.z;
        float v = (flag & 0x1) == 0 ? texCoord.y : texCoord.w;
        dest.set(u, v);
    }

    @FunctionalInterface
    public interface TintGetter {
        TintGetter DEFAULT = (tintIndex, dest) -> dest.set(1f);

        Vector4f getTint(int tintIndex, Vector4f dest);
    }
}
