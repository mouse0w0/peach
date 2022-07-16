package com.github.mouse0w0.peach.mcmod.generator.v1_12_2.bytecode.item;

import com.github.mouse0w0.peach.mcmod.generator.util.ASMUtils;
import com.github.mouse0w0.peach.mcmod.generator.v1_12_2.bytecode.ClassGenerator;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

public class ItemBlockBase extends ClassGenerator {
    public ItemBlockBase(String className) {
        super(className);

        MethodVisitor mv;

        cw.visit(V1_8, ACC_PUBLIC | ACC_SUPER, className, null, "net/minecraft/item/ItemBlock", null);

        ASMUtils.visitSource(cw);

        {
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(Lnet/minecraft/block/Block;)V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/item/ItemBlock", "<init>", "(Lnet/minecraft/block/Block;)V", false);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/Block", "getRegistryName", "()Lnet/minecraft/util/ResourceLocation;", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, className, "setRegistryName", "(Lnet/minecraft/util/ResourceLocation;)Lnet/minecraftforge/registries/IForgeRegistryEntry;", false);
            mv.visitInsn(POP);
            mv.visitInsn(RETURN);
            mv.visitMaxs(2, 2);
            mv.visitEnd();
        }
        cw.visitEnd();
    }
}
