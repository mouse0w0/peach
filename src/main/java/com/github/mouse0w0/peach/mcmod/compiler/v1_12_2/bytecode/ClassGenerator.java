package com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.bytecode;

import org.objectweb.asm.ClassWriter;

public class ClassGenerator {
    protected final ClassWriter cw;
    protected final String thisName;

    private boolean finished;

    public ClassGenerator(String className) {
        cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        thisName = className;
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
