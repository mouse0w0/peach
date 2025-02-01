package com.github.mouse0w0.peach.mcmod.preview;

import com.github.mouse0w0.softwarerenderer.Vertex;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class ItemModelVertex implements Vertex<ItemModelVertex> {
    public final Vector4f position = new Vector4f();
    public final Vector4f color = new Vector4f();
    public final Vector2f texCoord = new Vector2f();
    public final Vector3f normal = new Vector3f();

    @Override
    public Vector4f position() {
        return position;
    }

    @Override
    public void lerp(ItemModelVertex other, float t, ItemModelVertex dest) {
        position.lerp(other.position, t, dest.position);
        color.lerp(other.color, t, dest.color);
        texCoord.lerp(other.texCoord, t, dest.texCoord);
    }

    @Override
    public void perspectiveDivide() {
        float inv = 1f / position.w;

        position.x = position.x * inv;
        position.y = position.y * inv;
        position.z = position.z * inv;
        position.w = inv;

        color.mul(inv, color);
        texCoord.mul(inv, texCoord);
        normal.mul(inv, normal);
    }

    @Override
    public void beforeFragmentShader() {
        float inv = 1f / position.w;

        color.mul(inv, color);
        texCoord.mul(inv, texCoord);
        normal.mul(inv, normal);
    }
}
