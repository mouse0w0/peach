package com.github.mouse0w0.peach.mcmod.generator.v1_12_2.bytecode;

import com.github.mouse0w0.peach.mcmod.element.impl.MESmeltingRecipe;
import com.github.mouse0w0.peach.mcmod.generator.util.ASMUtils;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

public class SmeltingRecipeLoaderClassGenerator extends ClassGenerator {

    private MethodVisitor initMethod;

    public SmeltingRecipeLoaderClassGenerator(String className) {
        super(className);

        cw.visit(V1_8, ACC_PUBLIC | ACC_SUPER, className, null, "java/lang/Object", null);

        ASMUtils.visitSource(cw);

        ASMUtils.visitDefaultConstructor(cw);

        {
            MethodVisitor mv = cw.visitMethod(ACC_PRIVATE | ACC_STATIC, "addSmelting", "(Ljava/lang/String;ILjava/lang/String;IIF)V", null, null);
            mv.visitCode();
            mv.visitTypeInsn(NEW, "net/minecraft/item/ItemStack");
            mv.visitInsn(DUP);
            mv.visitFieldInsn(GETSTATIC, "net/minecraftforge/fml/common/registry/ForgeRegistries", "ITEMS", "Lnet/minecraftforge/registries/IForgeRegistry;");
            mv.visitTypeInsn(NEW, "net/minecraft/util/ResourceLocation");
            mv.visitInsn(DUP);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/util/ResourceLocation", "<init>", "(Ljava/lang/String;)V", false);
            mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraftforge/registries/IForgeRegistry", "getValue", "(Lnet/minecraft/util/ResourceLocation;)Lnet/minecraftforge/registries/IForgeRegistryEntry;", true);
            mv.visitTypeInsn(CHECKCAST, "net/minecraft/item/Item");
            mv.visitInsn(ICONST_1);
            mv.visitVarInsn(ILOAD, 1);
            mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/item/ItemStack", "<init>", "(Lnet/minecraft/item/Item;II)V", false);
            mv.visitTypeInsn(NEW, "net/minecraft/item/ItemStack");
            mv.visitInsn(DUP);
            mv.visitFieldInsn(GETSTATIC, "net/minecraftforge/fml/common/registry/ForgeRegistries", "ITEMS", "Lnet/minecraftforge/registries/IForgeRegistry;");
            mv.visitTypeInsn(NEW, "net/minecraft/util/ResourceLocation");
            mv.visitInsn(DUP);
            mv.visitVarInsn(ALOAD, 2);
            mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/util/ResourceLocation", "<init>", "(Ljava/lang/String;)V", false);
            mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraftforge/registries/IForgeRegistry", "getValue", "(Lnet/minecraft/util/ResourceLocation;)Lnet/minecraftforge/registries/IForgeRegistryEntry;", true);
            mv.visitTypeInsn(CHECKCAST, "net/minecraft/item/Item");
            mv.visitVarInsn(ILOAD, 3);
            mv.visitVarInsn(ILOAD, 4);
            mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/item/ItemStack", "<init>", "(Lnet/minecraft/item/Item;II)V", false);
            mv.visitVarInsn(FLOAD, 5);
            mv.visitMethodInsn(INVOKESTATIC, "net/minecraftforge/fml/common/registry/GameRegistry", "addSmelting", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;F)V", false);
            mv.visitInsn(RETURN);
            mv.visitMaxs(7, 6);
            mv.visitEnd();
        }

        {
            initMethod = cw.visitMethod(ACC_PUBLIC | ACC_STATIC, "init", "()V", null, null);
            initMethod.visitCode();
        }
    }

    public void visitSmelting(MESmeltingRecipe smelting) {

        ASMUtils.push(initMethod, smelting.getInput().getId());
        ASMUtils.push(initMethod, smelting.getInput().getMetadata());
        ASMUtils.push(initMethod, smelting.getOutput().getId());
        ASMUtils.push(initMethod, smelting.getOutput().getAmount());
        ASMUtils.push(initMethod, smelting.getOutput().getMetadata());
        ASMUtils.push(initMethod, (float) smelting.getXp());
        initMethod.visitMethodInsn(INVOKESTATIC, thisName, "addSmelting", "(Ljava/lang/String;ILjava/lang/String;IIF)V", false);
    }

    @Override
    protected void doLast() {
        initMethod.visitInsn(RETURN);
        initMethod.visitMaxs(6, 0);
        initMethod.visitEnd();
    }
}
