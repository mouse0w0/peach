package com.github.mouse0w0.minecraft.model.mesh;

import com.github.mouse0w0.minecraft.model.*;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class MeshGenerator {

    private static final Matrix4f IDENTIFY = new Matrix4f();

    private static final float SCALE_ROTATION_22_5 = 1.0F / (float) Math.cos(0.39269909262657166D) - 1.0F;
    private static final float SCALE_ROTATION_GENERAL = 1.0F / (float) Math.cos((Math.PI / 4D)) - 1.0F;

    private static final int[][] VERTEX_DATA_INDICES = {
            {1, 0, 4, 1, 4, 5}, // Down
            {2, 3, 7, 2, 7, 6}, // Up
            {6, 4, 0, 6, 0, 2}, // North
            {3, 1, 5, 3, 5, 7}, // South
            {2, 0, 1, 2, 1, 3}, // West
            {7, 5, 4, 7, 4, 6}  // East
    };
    private static final int[] TEX_COORDS_INDICES = {0, 1, 3, 0, 3, 2};

    public static void generate(McModel model, TextureMap textureMap, VertexDataConsumer consumer) {
        generate(model.getElements(), model.getTextures(), textureMap, consumer);
    }

    private static void generate(List<McElement> elements, Map<String, String> textures, TextureMap textureMap,
                                 VertexDataConsumer consumer) {
        for (McElement element : elements) {
            Vector3f[] boxPoints = createBoxPoints(element);

            for (Map.Entry<McFacing, McFace> faceEntry : element.getFaces().entrySet()) {
                McFacing facing = faceEntry.getKey();
                McFace face = faceEntry.getValue();
                Vector4f texCoord = createTexCoord(face, textures, textureMap);
                int[] indices = VERTEX_DATA_INDICES[facing.ordinal()];
                for (int i = 0; i < 6; i++) {
                    Vector3f point = boxPoints[indices[i]];
                    consumer.pos(point.x, point.y, point.z);
                    texCoord(texCoord, TEX_COORDS_INDICES[i], consumer);
                }
            }
        }
    }

    private static Vector3f[] createBoxPoints(McElement element) {
        Vector3f from = element.getFrom();
        Vector3f to = element.getTo();
        Matrix4f matrix = getRotationMatrix(element.getRotation());

        Vector3f[] boxPoints = new Vector3f[8];
        for (int i = 0; i < 8; i++) {
            boxPoints[i] = transform(createBoxPoint(from, to, i), matrix);
        }
        return boxPoints;
    }

    private static Vector3f createBoxPoint(Vector3f from, Vector3f to, int index) {
        float x = (index & 0x4) == 0 ? from.x : to.x;
        float y = (index & 0x2) == 0 ? from.y : to.y;
        float z = (index & 0x1) == 0 ? from.z : to.z;
        return new Vector3f(x, y, z).div(16);
    }

    private static Vector3f transform(Vector3f src, Matrix4f matrix) {
        Vector4f transformed = matrix.transform(new Vector4f(src, 1F));
        return src.set(transformed.x, transformed.y, transformed.z);
    }

    private static Matrix4f getRotationMatrix(McElement.Rotation rotation) {
        if (rotation == null) return IDENTIFY;

        Matrix4f matrix = new Matrix4f();
        Vector3f scale = new Vector3f();
        switch (rotation.getAxis()) {
            case X:
                matrix.rotateX(rotation.getAngle() * 0.017453292F);
                scale.set(0F, 1F, 1F);
                break;
            case Y:
                matrix.rotateY(rotation.getAngle() * 0.017453292F);
                scale.set(1F, 0F, 1F);
                break;
            case Z:
                matrix.rotateZ(rotation.getAngle() * 0.017453292F);
                scale.set(1F, 1F, 0F);
                break;
        }

        if (rotation.isRescale()) {
            if (Math.abs(rotation.getAngle()) == 22.5F) {
                scale.mul(SCALE_ROTATION_22_5);
            } else {
                scale.mul(SCALE_ROTATION_GENERAL);
            }

            scale.add(1F, 1F, 1F);
        } else {
            scale.set(1F);
        }

        Vector3f origin = rotation.getOrigin().div(16, new Vector3f());
        return matrix.scale(scale).translateLocal(origin).translate(origin.negate());
    }

    private static Vector4f createTexCoord(McFace face, Map<String, String> textures, TextureMap textureMap) {
        return createTexCoord(textureMap.getTexCoord(getTextureName(face.getTexture(), textures)), face.getUv());
    }

    private static Vector4f createTexCoord(Vector4f texMapUv, Vector4f faceUv) {
        return new Vector4f(
                lerp(faceUv.x / 16, texMapUv.x, texMapUv.w),
                lerp(faceUv.y / 16, texMapUv.y, texMapUv.w),
                lerp(faceUv.z / 16, texMapUv.x, texMapUv.w),
                lerp(faceUv.w / 16, texMapUv.y, texMapUv.w));
    }

    private static float lerp(float value, float min, float max) {
        return min + value * (max - min);
    }

    private static String getTextureName(String name, Map<String, String> textures) {
        while (name.charAt(0) == '#') {
            name = textures.get(name.substring(1));
        }
        return name;
    }

    private static void texCoord(Vector4f texCoord, int index, VertexDataConsumer consumer) {
        float u = (index & 0x2) == 0 ? texCoord.x : texCoord.z;
        float v = (index & 0x1) == 0 ? texCoord.y : texCoord.w;
        consumer.texCoord(u, v);
    }

    public static void main(String[] args) throws IOException {
        McModel model = McModelHelper.load(Paths.get("D:\\Workspace\\Forge\\Peach\\src\\main\\java\\com\\github\\mouse0w0\\peach\\mcmod\\model\\json\\Hello.json"));
        System.out.println(model);
        McElement.Rotation rotation = new McElement.Rotation(new Vector3f(8f, 8f, 8f), McAxis.Y, 45, true);
        System.out.println(transform(new Vector3f(0.95f, 1.0f, 0.5f), getRotationMatrix(rotation)));
        System.out.println(transform(new Vector3f(0.95f, 1.0f, 0.5f), getRotationMatrix(rotation))
                .equals(new Vector3f(0.9499999f, 1.0f, 0.05000004f)));
    }
}
