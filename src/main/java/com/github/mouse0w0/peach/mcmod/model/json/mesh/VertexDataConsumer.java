package com.github.mouse0w0.peach.mcmod.model.json.mesh;

public interface VertexDataConsumer {

    void pos(float x, float y, float z);

    void texCoord(float u, float v);

    void normal(float nx, float ny, float nz);
}
