package com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.util;

import com.github.mouse0w0.peach.mcmod.compiler.Filer;
import com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.element.ItemGen;
import com.github.mouse0w0.peach.mcmod.util.ASMUtils;
import org.objectweb.asm.*;

import java.io.IOException;

public class ItemModelsClass implements Opcodes {

    private final String internalName;
    private final String itemsInternalName;
    private final String modid;

    private ClassWriter classWriter = new ClassWriter(0);
    private MethodVisitor registerModel;

    public ItemModelsClass(String packageName, String modid) {
        this.internalName = ASMUtils.getInternalName(packageName, "ItemModels");
        this.itemsInternalName = ASMUtils.getInternalName(packageName, "Items");
        this.modid = modid;
    }

    public void visitStart() {
        MethodVisitor methodVisitor;
        AnnotationVisitor annotationVisitor0;

        classWriter.visit(V1_8, ACC_PUBLIC | ACC_SUPER, internalName, null, "java/lang/Object", null);

        classWriter.visitSource("Peach.generated", null);

        {
            annotationVisitor0 = classWriter.visitAnnotation("Lnet/minecraftforge/fml/common/Mod$EventBusSubscriber;", true);
            annotationVisitor0.visit("modid", modid);
            {
                AnnotationVisitor annotationVisitor1 = annotationVisitor0.visitArray("value");
                annotationVisitor1.visitEnum(null, "Lnet/minecraftforge/fml/relauncher/Side;", "CLIENT");
                annotationVisitor1.visitEnd();
            }
            annotationVisitor0.visitEnd();
        }
        classWriter.visitInnerClass("net/minecraftforge/fml/common/Mod$EventBusSubscriber", "net/minecraftforge/fml/common/Mod", "EventBusSubscriber", ACC_PUBLIC | ACC_STATIC | ACC_ANNOTATION | ACC_ABSTRACT | ACC_INTERFACE);

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
            registerModel = methodVisitor = classWriter.visitMethod(ACC_PUBLIC | ACC_STATIC, "registerModel", "(Lnet/minecraftforge/client/event/ModelRegistryEvent;)V", null, null);
            {
                annotationVisitor0 = methodVisitor.visitAnnotation("Lnet/minecraftforge/fml/common/eventhandler/SubscribeEvent;", true);
                annotationVisitor0.visitEnd();
            }
            methodVisitor.visitCode();
        }
        {
            methodVisitor = classWriter.visitMethod(ACC_PRIVATE | ACC_STATIC, "registerItemModel", "(Lnet/minecraft/item/Item;)V", null, null);
            methodVisitor.visitCode();
            Label label0 = new Label();
            methodVisitor.visitLabel(label0);
            methodVisitor.visitLineNumber(19, label0);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitInsn(ICONST_0);
            methodVisitor.visitTypeInsn(NEW, "net/minecraft/client/renderer/block/model/ModelResourceLocation");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/Item", "getRegistryName", "()Lnet/minecraft/util/ResourceLocation;", false);
            methodVisitor.visitLdcInsn("inventory");
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/block/model/ModelResourceLocation", "<init>", "(Lnet/minecraft/util/ResourceLocation;Ljava/lang/String;)V", false);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "net/minecraftforge/client/model/ModelLoader", "setCustomModelResourceLocation", "(Lnet/minecraft/item/Item;ILnet/minecraft/client/renderer/block/model/ModelResourceLocation;)V", false);
            Label label1 = new Label();
            methodVisitor.visitLabel(label1);
            methodVisitor.visitLineNumber(20, label1);
            methodVisitor.visitInsn(RETURN);
            Label label2 = new Label();
            methodVisitor.visitLabel(label2);
            methodVisitor.visitLocalVariable("item", "Lnet/minecraft/item/Item;", null, label0, label2, 0);
            methodVisitor.visitMaxs(6, 1);
            methodVisitor.visitEnd();
        }
    }

    public void visitModel(String registerName) {
        registerModel.visitFieldInsn(GETSTATIC, itemsInternalName, ItemGen.getItemFieldName(registerName), "Lnet/minecraft/item/Item;");
        registerModel.visitMethodInsn(INVOKESTATIC, internalName, "registerItemModel", "(Lnet/minecraft/item/Item;)V", false);
    }

    public void visitEnd() {
        {
            registerModel.visitInsn(RETURN);
            registerModel.visitMaxs(1, 1);
            registerModel.visitEnd();
        }

        classWriter.visitEnd();
    }

    public void save(Filer classesFiler) throws IOException {
        classesFiler.write(internalName + ".class", classWriter.toByteArray());
    }
}
