package com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.util;

import com.github.mouse0w0.peach.mcmod.compiler.Filer;
import com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.generator.ItemGen;
import com.github.mouse0w0.peach.mcmod.util.ASMUtils;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.IOException;

public class ItemModelsClass implements Opcodes {

    private final String internalName;
    private final String modid;
    private final ModItemsClass itemsClass;

    private ClassWriter classWriter = new ClassWriter(0);
    private MethodVisitor registerModel;

    public ItemModelsClass(String className, String modid, ModItemsClass itemsClass) {
        this.internalName = ASMUtils.getInternalName(className);
        this.modid = modid;
        this.itemsClass = itemsClass;
        visitStart();
    }

    public void visitStart() {
        MethodVisitor methodVisitor;
        AnnotationVisitor annotationVisitor0;

        classWriter.visit(V1_8, ACC_PUBLIC | ACC_SUPER, internalName, null, "java/lang/Object", null);

        ASMUtils.visitSource(classWriter);

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

        ASMUtils.visitDefaultConstructor(classWriter);
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
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitInsn(ICONST_0);
            methodVisitor.visitTypeInsn(NEW, "net/minecraft/client/renderer/block/model/ModelResourceLocation");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/Item", "getRegistryName", "()Lnet/minecraft/util/ResourceLocation;", false);
            methodVisitor.visitLdcInsn("inventory");
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/block/model/ModelResourceLocation", "<init>", "(Lnet/minecraft/util/ResourceLocation;Ljava/lang/String;)V", false);
            methodVisitor.visitMethodInsn(INVOKESTATIC, "net/minecraftforge/client/model/ModelLoader", "setCustomModelResourceLocation", "(Lnet/minecraft/item/Item;ILnet/minecraft/client/renderer/block/model/ModelResourceLocation;)V", false);
            methodVisitor.visitInsn(RETURN);
            methodVisitor.visitMaxs(6, 1);
            methodVisitor.visitEnd();
        }
    }

    public void visitModel(String registerName) {
        registerModel.visitFieldInsn(GETSTATIC, itemsClass.getInternalName(), ItemGen.getItemFieldName(registerName), "Lnet/minecraft/item/Item;");
        registerModel.visitMethodInsn(INVOKESTATIC, internalName, "registerItemModel", "(Lnet/minecraft/item/Item;)V", false);
    }

    public void save(Filer classesFiler) throws IOException {
        {
            registerModel.visitInsn(RETURN);
            registerModel.visitMaxs(1, 1);
            registerModel.visitEnd();
        }

        classWriter.visitEnd();

        classesFiler.write(internalName + ".class", classWriter.toByteArray());
    }
}
