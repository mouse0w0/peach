package com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.generator;

import com.github.mouse0w0.peach.mcmod.Item;
import com.github.mouse0w0.peach.mcmod.ItemStack;
import com.github.mouse0w0.peach.mcmod.compiler.Environment;
import com.github.mouse0w0.peach.mcmod.element.impl.SmeltingRecipe;
import com.github.mouse0w0.peach.mcmod.util.ASMUtils;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import java.util.Collection;

import static org.objectweb.asm.Opcodes.*;

public class SmeltingRecipeGen extends Generator<SmeltingRecipe> {

    private String internalClassName;
    private MethodVisitor init;

    @Override
    public void generate(Environment environment, Collection<SmeltingRecipe> elements) throws Exception {
        internalClassName = ASMUtils.getInternalName(environment.getRootPackageName(), "SmeltingRecipes");

        ClassWriter classWriter = new ClassWriter(0);
        MethodVisitor methodVisitor;

        classWriter.visit(V1_8, ACC_PUBLIC | ACC_SUPER, internalClassName, null, "java/lang/Object", null);

        ASMUtils.visitSource(classWriter);

        ASMUtils.visitDefaultConstructor(classWriter);
        {
            methodVisitor = classWriter.visitMethod(ACC_PRIVATE | ACC_STATIC, "addSmelting", "(Ljava/lang/String;ILjava/lang/String;IIF)V", null, null);
            methodVisitor.visitCode();
            Label label0 = new Label();
            methodVisitor.visitLabel(label0);
            methodVisitor.visitLineNumber(15, label0);
            methodVisitor.visitTypeInsn(NEW, "net/minecraft/item/ItemStack");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitFieldInsn(GETSTATIC, "net/minecraftforge/fml/common/registry/ForgeRegistries", "ITEMS", "Lnet/minecraftforge/registries/IForgeRegistry;");
            methodVisitor.visitTypeInsn(NEW, "net/minecraft/util/ResourceLocation");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "net/minecraft/util/ResourceLocation", "<init>", "(Ljava/lang/String;)V", false);
            Label label1 = new Label();
            methodVisitor.visitLabel(label1);
            methodVisitor.visitLineNumber(16, label1);
            methodVisitor.visitMethodInsn(INVOKEINTERFACE, "net/minecraftforge/registries/IForgeRegistry", "getValue", "(Lnet/minecraft/util/ResourceLocation;)Lnet/minecraftforge/registries/IForgeRegistryEntry;", true);
            methodVisitor.visitTypeInsn(CHECKCAST, "net/minecraft/item/Item");
            methodVisitor.visitInsn(ICONST_1);
            methodVisitor.visitVarInsn(ILOAD, 1);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "net/minecraft/item/ItemStack", "<init>", "(Lnet/minecraft/item/Item;II)V", false);
            methodVisitor.visitTypeInsn(NEW, "net/minecraft/item/ItemStack");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitFieldInsn(GETSTATIC, "net/minecraftforge/fml/common/registry/ForgeRegistries", "ITEMS", "Lnet/minecraftforge/registries/IForgeRegistry;");
            methodVisitor.visitTypeInsn(NEW, "net/minecraft/util/ResourceLocation");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitVarInsn(ALOAD, 2);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "net/minecraft/util/ResourceLocation", "<init>", "(Ljava/lang/String;)V", false);
            Label label2 = new Label();
            methodVisitor.visitLabel(label2);
            methodVisitor.visitLineNumber(17, label2);
            methodVisitor.visitMethodInsn(INVOKEINTERFACE, "net/minecraftforge/registries/IForgeRegistry", "getValue", "(Lnet/minecraft/util/ResourceLocation;)Lnet/minecraftforge/registries/IForgeRegistryEntry;", true);
            methodVisitor.visitTypeInsn(CHECKCAST, "net/minecraft/item/Item");
            methodVisitor.visitVarInsn(ILOAD, 3);
            methodVisitor.visitVarInsn(ILOAD, 4);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "net/minecraft/item/ItemStack", "<init>", "(Lnet/minecraft/item/Item;II)V", false);
            methodVisitor.visitVarInsn(FLOAD, 5);
            Label label3 = new Label();
            methodVisitor.visitLabel(label3);
            methodVisitor.visitLineNumber(15, label3);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "net/minecraftforge/fml/common/registry/GameRegistry", "addSmelting", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;F)V", false);
            Label label4 = new Label();
            methodVisitor.visitLabel(label4);
            methodVisitor.visitLineNumber(19, label4);
            methodVisitor.visitInsn(RETURN);
            Label label5 = new Label();
            methodVisitor.visitLabel(label5);
            methodVisitor.visitLocalVariable("input", "Ljava/lang/String;", null, label0, label5, 0);
            methodVisitor.visitLocalVariable("inputMetadata", "I", null, label0, label5, 1);
            methodVisitor.visitLocalVariable("output", "Ljava/lang/String;", null, label0, label5, 2);
            methodVisitor.visitLocalVariable("outputAmount", "I", null, label0, label5, 3);
            methodVisitor.visitLocalVariable("outputMetadata", "I", null, label0, label5, 4);
            methodVisitor.visitLocalVariable("xp", "F", null, label0, label5, 5);
            methodVisitor.visitMaxs(7, 6);
            methodVisitor.visitEnd();
        }
        {
            init = methodVisitor = classWriter.visitMethod(ACC_PUBLIC | ACC_STATIC, "init", "()V", null, null);
            methodVisitor.visitCode();
            super.generate(environment, elements);
            methodVisitor.visitInsn(RETURN);
            methodVisitor.visitMaxs(6, 0);
            methodVisitor.visitEnd();
        }
        classWriter.visitEnd();

        environment.getClassesFiler().write(internalClassName + ".class", classWriter.toByteArray());
    }

    @Override
    protected void generate(Environment environment, SmeltingRecipe smelting) throws Exception {
        Item input = smelting.getInput();
        ASMUtils.push(init, input.getId());
        ASMUtils.push(init, input.getMetadata());
        ItemStack output = smelting.getOutput();
        ASMUtils.push(init, output.getItem().getId());
        ASMUtils.push(init, output.getAmount());
        ASMUtils.push(init, output.getItem().getMetadata());
        ASMUtils.push(init, (float) smelting.getXp());
        init.visitMethodInsn(INVOKESTATIC, internalClassName, "addSmelting", "(Ljava/lang/String;ILjava/lang/String;IIF)V", false);
    }
}
