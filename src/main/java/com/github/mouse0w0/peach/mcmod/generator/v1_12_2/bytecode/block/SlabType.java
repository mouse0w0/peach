package com.github.mouse0w0.peach.mcmod.generator.v1_12_2.bytecode.block;

import com.github.mouse0w0.peach.mcmod.generator.util.ASMUtils;
import com.github.mouse0w0.peach.mcmod.generator.v1_12_2.bytecode.ClassGenerator;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import static org.objectweb.asm.Opcodes.*;

public class SlabType extends ClassGenerator {
    public SlabType(String className) {
        super(className);

        String descriptor = ASMUtils.getDescriptor(className);

        FieldVisitor fv;
        MethodVisitor mv;

        cw.visit(V1_8, ACC_PUBLIC | ACC_FINAL | ACC_SUPER | ACC_ENUM, className, "Ljava/lang/Enum<" + descriptor + ">;Lnet/minecraft/util/IStringSerializable;", "java/lang/Enum", new String[]{"net/minecraft/util/IStringSerializable"});

        {
            fv = cw.visitField(ACC_PUBLIC | ACC_FINAL | ACC_STATIC | ACC_ENUM, "BOTTOM", descriptor, null, null);
            fv.visitEnd();
        }
        {
            fv = cw.visitField(ACC_PUBLIC | ACC_FINAL | ACC_STATIC | ACC_ENUM, "TOP", descriptor, null, null);
            fv.visitEnd();
        }
        {
            fv = cw.visitField(ACC_PUBLIC | ACC_FINAL | ACC_STATIC | ACC_ENUM, "DOUBLE", descriptor, null, null);
            fv.visitEnd();
        }
        {
            fv = cw.visitField(ACC_PRIVATE | ACC_FINAL, "name", "Ljava/lang/String;", null, null);
            fv.visitEnd();
        }
        {
            fv = cw.visitField(ACC_PRIVATE | ACC_FINAL | ACC_STATIC | ACC_SYNTHETIC, "$VALUES", "[" + descriptor, null, null);
            fv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC | ACC_STATIC, "values", "()[" + descriptor, null, null);
            mv.visitCode();
            mv.visitFieldInsn(GETSTATIC, className, "$VALUES", "[" + descriptor);
            mv.visitMethodInsn(INVOKEVIRTUAL, "[" + descriptor, "clone", "()Ljava/lang/Object;", false);
            mv.visitTypeInsn(CHECKCAST, "[" + descriptor);
            mv.visitInsn(ARETURN);
            mv.visitMaxs(1, 0);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC | ACC_STATIC, "valueOf", "(Ljava/lang/String;)" + descriptor, null, null);
            mv.visitCode();
            mv.visitLdcInsn(Type.getType(descriptor));
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Enum", "valueOf", "(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/Enum;", false);
            mv.visitTypeInsn(CHECKCAST, className);
            mv.visitInsn(ARETURN);
            mv.visitMaxs(2, 1);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PRIVATE, "<init>", "(Ljava/lang/String;ILjava/lang/String;)V", "(Ljava/lang/String;)V", null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitVarInsn(ILOAD, 2);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Enum", "<init>", "(Ljava/lang/String;I)V", false);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 3);
            mv.visitFieldInsn(PUTFIELD, className, "name", "Ljava/lang/String;");
            mv.visitInsn(RETURN);
            mv.visitMaxs(3, 4);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "toString", "()Ljava/lang/String;", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, className, "name", "Ljava/lang/String;");
            mv.visitInsn(ARETURN);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "func_176610_l", "()Ljava/lang/String;", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, className, "name", "Ljava/lang/String;");
            mv.visitInsn(ARETURN);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC | ACC_STATIC, "byMetadata", "(I)" + descriptor, null, null);
            mv.visitCode();
            mv.visitVarInsn(ILOAD, 0);
            Label label0 = new Label();
            Label label1 = new Label();
            Label label2 = new Label();
            mv.visitTableSwitchInsn(0, 2, label0, new Label[]{label0, label1, label2});
            mv.visitLabel(label0);
            mv.visitFieldInsn(GETSTATIC, className, "BOTTOM", descriptor);
            mv.visitInsn(ARETURN);
            mv.visitLabel(label1);
            mv.visitFieldInsn(GETSTATIC, className, "TOP", descriptor);
            mv.visitInsn(ARETURN);
            mv.visitLabel(label2);
            mv.visitFieldInsn(GETSTATIC, className, "DOUBLE", descriptor);
            mv.visitInsn(ARETURN);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null);
            mv.visitCode();
            mv.visitTypeInsn(NEW, className);
            mv.visitInsn(DUP);
            mv.visitLdcInsn("BOTTOM");
            mv.visitInsn(ICONST_0);
            mv.visitLdcInsn("bottom");
            mv.visitMethodInsn(INVOKESPECIAL, className, "<init>", "(Ljava/lang/String;ILjava/lang/String;)V", false);
            mv.visitFieldInsn(PUTSTATIC, className, "BOTTOM", descriptor);
            mv.visitTypeInsn(NEW, className);
            mv.visitInsn(DUP);
            mv.visitLdcInsn("TOP");
            mv.visitInsn(ICONST_1);
            mv.visitLdcInsn("top");
            mv.visitMethodInsn(INVOKESPECIAL, className, "<init>", "(Ljava/lang/String;ILjava/lang/String;)V", false);
            mv.visitFieldInsn(PUTSTATIC, className, "TOP", descriptor);
            mv.visitTypeInsn(NEW, className);
            mv.visitInsn(DUP);
            mv.visitLdcInsn("DOUBLE");
            mv.visitInsn(ICONST_2);
            mv.visitLdcInsn("double");
            mv.visitMethodInsn(INVOKESPECIAL, className, "<init>", "(Ljava/lang/String;ILjava/lang/String;)V", false);
            mv.visitFieldInsn(PUTSTATIC, className, "DOUBLE", descriptor);
            mv.visitInsn(ICONST_3);
            mv.visitTypeInsn(ANEWARRAY, className);
            mv.visitInsn(DUP);
            mv.visitInsn(ICONST_0);
            mv.visitFieldInsn(GETSTATIC, className, "BOTTOM", descriptor);
            mv.visitInsn(AASTORE);
            mv.visitInsn(DUP);
            mv.visitInsn(ICONST_1);
            mv.visitFieldInsn(GETSTATIC, className, "TOP", descriptor);
            mv.visitInsn(AASTORE);
            mv.visitInsn(DUP);
            mv.visitInsn(ICONST_2);
            mv.visitFieldInsn(GETSTATIC, className, "DOUBLE", descriptor);
            mv.visitInsn(AASTORE);
            mv.visitFieldInsn(PUTSTATIC, className, "$VALUES", "[" + descriptor);
            mv.visitInsn(RETURN);
            mv.visitMaxs(5, 0);
            mv.visitEnd();
        }
        cw.visitEnd();
    }
}
