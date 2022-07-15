package com.github.mouse0w0.peach.mcmod.generator.v1_12_2.bytecode;

import org.objectweb.asm.ClassWriter;

public class ClassGenerator {
    protected final ClassWriter cw;
    protected final String thisName;

    private boolean finished;

    public ClassGenerator(String className) {
        this.cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        this.thisName = className;
    }

    protected void doLast() {

    }

    public String getThisName() {
        return thisName;
    }

    public byte[] toByteArray() {
        if (!finished) {
            doLast();
            finished = true;
        }
        return cw.toByteArray();
    }
}
