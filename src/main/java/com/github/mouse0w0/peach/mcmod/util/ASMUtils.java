package com.github.mouse0w0.peach.mcmod.util;


import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

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

    public static void push(final MethodVisitor mv, final boolean value) {
        push(mv, value ? 1 : 0);
    }

    public static void push(final MethodVisitor mv, final int value) {
        if (value >= -1 && value <= 5) {
            mv.visitInsn(Opcodes.ICONST_0 + value);
        } else if (value >= Byte.MIN_VALUE && value <= Byte.MAX_VALUE) {
            mv.visitIntInsn(Opcodes.BIPUSH, value);
        } else if (value >= Short.MIN_VALUE && value <= Short.MAX_VALUE) {
            mv.visitIntInsn(Opcodes.SIPUSH, value);
        } else {
            mv.visitLdcInsn(value);
        }
    }

    public static void push(final MethodVisitor mv, final long value) {
        if (value == 0L || value == 1L) {
            mv.visitInsn(Opcodes.LCONST_0 + (int) value);
        } else {
            mv.visitLdcInsn(value);
        }
    }

    public static void push(final MethodVisitor mv, final float value) {
        int bits = Float.floatToIntBits(value);
        if (bits == 0L || bits == 0x3F800000 || bits == 0x40000000) { // 0..2
            mv.visitInsn(Opcodes.FCONST_0 + (int) value);
        } else {
            mv.visitLdcInsn(value);
        }
    }

    public static void push(final MethodVisitor mv, final double value) {
        long bits = Double.doubleToLongBits(value);
        if (bits == 0L || bits == 0x3FF0000000000000L) { // +0.0d and 1.0d
            mv.visitInsn(Opcodes.DCONST_0 + (int) value);
        } else {
            mv.visitLdcInsn(value);
        }
    }

    public static void push(final MethodVisitor mv, final String value) {
        if (value == null) {
            mv.visitInsn(Opcodes.ACONST_NULL);
        } else {
            mv.visitLdcInsn(value);
        }
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
