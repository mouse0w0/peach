package com.github.mouse0w0.peach.mcmod.generator.util;


import org.objectweb.asm.*;

import static org.objectweb.asm.Opcodes.*;

public class ASMUtils {

    private static final String CLASS_DESCRIPTOR = "Ljava/lang/Class;";

    public static String getInternalName(String className) {
        return className.replace('.', '/');
    }

    public static String getInternalName(String packageName, String className) {
        return packageName.replace('.', '/') + "/" + className.replace('.', '/');
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

    public static void push(final MethodVisitor mv, final Type value) {
        if (value == null) {
            mv.visitInsn(Opcodes.ACONST_NULL);
        } else {
            switch (value.getSort()) {
                case Type.BOOLEAN:
                    mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/Boolean", "TYPE", CLASS_DESCRIPTOR);
                    break;
                case Type.CHAR:
                    mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/Character", "TYPE", CLASS_DESCRIPTOR);
                    break;
                case Type.BYTE:
                    mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/Byte", "TYPE", CLASS_DESCRIPTOR);
                    break;
                case Type.SHORT:
                    mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/Short", "TYPE", CLASS_DESCRIPTOR);
                    break;
                case Type.INT:
                    mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/Integer", "TYPE", CLASS_DESCRIPTOR);
                    break;
                case Type.FLOAT:
                    mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/Float", "TYPE", CLASS_DESCRIPTOR);
                    break;
                case Type.LONG:
                    mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/Long", "TYPE", CLASS_DESCRIPTOR);
                    break;
                case Type.DOUBLE:
                    mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/Double", "TYPE", CLASS_DESCRIPTOR);
                    break;
                default:
                    mv.visitLdcInsn(value);
                    break;
            }
        }
    }

    public static void push(final MethodVisitor mv, final Handle handle) {
        if (handle == null) {
            mv.visitInsn(Opcodes.ACONST_NULL);
        } else {
            mv.visitLdcInsn(handle);
        }
    }

    public static void push(final MethodVisitor mv, final ConstantDynamic constantDynamic) {
        if (constantDynamic == null) {
            mv.visitInsn(Opcodes.ACONST_NULL);
        } else {
            mv.visitLdcInsn(constantDynamic);
        }
    }

    public static final int ICMP_OFFSET = IF_ICMPEQ - IFEQ;

    public static void ifCmp(final MethodVisitor mv, final int opcode, final int value, final Label label) {
        if (value == 0) {
            mv.visitJumpInsn(opcode, label);
        } else {
            push(mv, value);
            mv.visitJumpInsn(opcode + ICMP_OFFSET, label);
        }
    }
}
