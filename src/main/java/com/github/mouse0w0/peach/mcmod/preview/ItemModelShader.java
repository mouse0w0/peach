package com.github.mouse0w0.peach.mcmod.preview;

import com.github.mouse0w0.softwarerenderer.Shader;
import com.github.mouse0w0.softwarerenderer.sampler.DefaultSampler2D;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class ItemModelShader implements Shader<ItemModelVertex> {
    public final Matrix4f modelViewProjectionMatrix = new Matrix4f();
    public final Matrix4f modelMatrix = new Matrix4f();

    public final DefaultSampler2D sampler = new DefaultSampler2D();

    public final Vector3f light0Dir = new Vector3f();
    public final Vector3f light1Dir = new Vector3f();

    public boolean enableLight = true;

    public float diffuseLight = 0.6f;
    public float ambientLight = 0.4f;

    public void setupGui3DLighting() {
        light0Dir.set(-0.9339295f, 0.26162702f, -0.24357113f);
        light1Dir.set(-0.10413062f, 0.9759761f, 0.19138299f);

        // light0Dir.set(-0.92926464f, 0.26032024f, -0.24235454f).normalize();
        // light1Dir.set(-0.10310748f, 0.96638666f, 0.18950255f).normalize();
    }

    public void setupGuiFlatLighting() {
        light0Dir.set(-0.22275901f, 0.16945612f, 0.9600329f);
        light1Dir.set(-0.21617514f, 0.97131175f, 0.099104986f);

        // light0Dir.set(-0.22151990f, 0.16851352f, 0.95469269f).normalize();
        // light1Dir.set(-0.21405162f, 0.96177041f, 0.09813146f).normalize();
    }

    @Override
    public void vertex(ItemModelVertex vertex) {
        vertex.position.mul(modelViewProjectionMatrix, vertex.position);
        vertex.normal.mulDirection(modelMatrix).normalize();
        if (enableLight) lighting(vertex);
    }

    private void lighting(ItemModelVertex vertex) {
        float light0 = Math.max(0, light0Dir.dot(vertex.normal));
        float light1 = Math.max(0, light1Dir.dot(vertex.normal));
        float light = Math.min(1, (light0 + light1) * diffuseLight + ambientLight);
        mulXYZ(vertex.color, light);
    }

    private Vector4f mulXYZ(Vector4f v, float scalar) {
        v.x = v.x * scalar;
        v.y = v.y * scalar;
        v.z = v.z * scalar;
        return v;
    }

    @Override
    public boolean fragment(ItemModelVertex fragment, Vector4f color) {
        fragment.color.mul(sampler.sample(fragment.texCoord, color), color);
        return false;
    }
}
