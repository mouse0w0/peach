package com.github.mouse0w0.peach.mcmod.util;


import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

public class ASMUtils {

    public static String getInternalName(String className) {
        return className.replace('.', '/');
    }

    public static String getInternalName(String packageName, String className) {
        return packageName.replace('.', '/') + "/" + className;
    }

    public static String normalizeClassName(String className) {
        return Character.toUpperCase(className.charAt(0)) + className.substring(1);
    }

    public static String getDescriptor(String internalClassName) {
        return "L" + internalClassName + ";";
    }

    public static String getDescriptor(String packageName, String className) {
        return "L" + getInternalName(packageName, className) + ";";
    }

    public static void visitSource(ClassVisitor cv) {
        cv.visitSource("Peach.generated", null);
    }

    public static void visitDefaultConstructor(ClassVisitor cv) {
        MethodVisitor mv = cv.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        mv.visitInsn(RETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();
    }
}
