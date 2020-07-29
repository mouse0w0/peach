package com.github.mouse0w0.peach.forge.compiler.v1_12_2.element;

import com.github.mouse0w0.peach.forge.Item;
import com.github.mouse0w0.peach.forge.ItemStack;
import com.github.mouse0w0.peach.forge.compiler.CompileContext;
import com.github.mouse0w0.peach.forge.compiler.ForgeCompiler;
import com.github.mouse0w0.peach.forge.element.ElementFile;
import com.github.mouse0w0.peach.forge.element.SmeltingRecipe;
import com.github.mouse0w0.peach.forge.util.ASMUtils;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.GeneratorAdapter;

import java.util.Collection;

import static org.objectweb.asm.Opcodes.*;

public class SmeltingRecipeGen extends ElementGen<SmeltingRecipe> {

    private String internalClassName;
    private GeneratorAdapter adapter;

    @Override
    public void generate(CompileContext context, Collection<ElementFile<SmeltingRecipe>> elementFiles) throws Exception {
        internalClassName = ASMUtils.getInternalName(context.getData(ForgeCompiler.ROOT_PACKAGE_NAME), "SmeltingRecipes");

        ClassWriter classWriter = new ClassWriter(0);
        MethodVisitor methodVisitor;
        AnnotationVisitor annotationVisitor0;

        classWriter.visit(V1_8, ACC_PUBLIC | ACC_SUPER, internalClassName, null, "java/lang/Object", null);

        classWriter.visitSource("Peach.generated", null);

        {
            methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            methodVisitor.visitCode();
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            methodVisitor.visitInsn(RETURN);
            methodVisitor.visitMaxs(1, 1);
            methodVisitor.visitEnd();
        }
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
            methodVisitor = classWriter.visitMethod(ACC_PUBLIC | ACC_STATIC, "init", "()V", null, null);
            adapter = new GeneratorAdapter(methodVisitor, ACC_PUBLIC | ACC_STATIC, "init", "()V");
            methodVisitor.visitCode();
            super.generate(context, elementFiles);
            methodVisitor.visitInsn(RETURN);
            methodVisitor.visitMaxs(6, 0);
            methodVisitor.visitEnd();
        }
        classWriter.visitEnd();

        context.getClassesFiler().write(internalClassName + ".class", classWriter.toByteArray());
    }

    @Override
    protected void generate(CompileContext context, ElementFile<SmeltingRecipe> file) throws Exception {
        SmeltingRecipe smelting = file.get();
        Item input = smelting.getInput();
        adapter.push(input.getId());
        adapter.push(input.getMetadata());
        ItemStack output = smelting.getOutput();
        adapter.push(output.getItem().getId());
        adapter.push(output.getAmount());
        adapter.push(output.getItem().getMetadata());
        adapter.push((float) smelting.getXp());
        adapter.visitMethodInsn(INVOKESTATIC, internalClassName, "addSmelting", "(Ljava/lang/String;ILjava/lang/String;IIF)V", false);
    }
}
