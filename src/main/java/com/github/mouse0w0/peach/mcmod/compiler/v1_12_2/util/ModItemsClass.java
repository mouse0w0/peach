package com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.util;

import com.github.mouse0w0.peach.mcmod.compiler.Filer;
import com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.generator.ItemGen;
import com.github.mouse0w0.peach.mcmod.util.ASMUtils;
import org.objectweb.asm.*;

import java.io.IOException;

public class ModItemsClass implements Opcodes {

    private final String packageName;
    private final String modid;
    private final String internalName;

    private ClassWriter classWriter = new ClassWriter(0);

    private MethodVisitor registerItem;
    private MethodVisitor clinit;

    public ModItemsClass(String packageName, String modid) {
        this.packageName = packageName;
        this.modid = modid;
        this.internalName = ASMUtils.getInternalName(packageName, "Items");
        visitStart();
    }

    public String getInternalName() {
        return internalName;
    }

    public void visitStart() {
        MethodVisitor methodVisitor;
        AnnotationVisitor annotationVisitor0;

        classWriter.visit(V1_8, ACC_PUBLIC | ACC_SUPER, internalName, null, "java/lang/Object", null);

        ASMUtils.visitSource(classWriter);

        {
            annotationVisitor0 = classWriter.visitAnnotation("Lnet/minecraftforge/fml/common/Mod$EventBusSubscriber;", true);
            annotationVisitor0.visit("modid", modid);
            annotationVisitor0.visitEnd();
        }
        classWriter.visitInnerClass("net/minecraftforge/event/RegistryEvent$Register", "net/minecraftforge/event/RegistryEvent", "Register", ACC_PUBLIC | ACC_STATIC);

        classWriter.visitInnerClass("net/minecraftforge/fml/common/Mod$EventBusSubscriber", "net/minecraftforge/fml/common/Mod", "EventBusSubscriber", ACC_PUBLIC | ACC_STATIC | ACC_ANNOTATION | ACC_ABSTRACT | ACC_INTERFACE);

        ASMUtils.visitDefaultConstructor(classWriter);
        {
            registerItem = methodVisitor = classWriter.visitMethod(ACC_PUBLIC | ACC_STATIC, "registerItem", "(Lnet/minecraftforge/event/RegistryEvent$Register;)V", "(Lnet/minecraftforge/event/RegistryEvent$Register<Lnet/minecraft/item/Item;>;)V", null);
            {
                annotationVisitor0 = methodVisitor.visitAnnotation("Lnet/minecraftforge/fml/common/eventhandler/SubscribeEvent;", true);
                annotationVisitor0.visitEnd();
            }
            methodVisitor.visitCode();
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "net/minecraftforge/event/RegistryEvent$Register", "getRegistry", "()Lnet/minecraftforge/registries/IForgeRegistry;", false);
            methodVisitor.visitVarInsn(ASTORE, 1);
        }
        {
            clinit = methodVisitor = classWriter.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null);
            methodVisitor.visitCode();
        }
    }

    public void visitItem(String registerName) {
        String itemFieldName = ItemGen.getItemFieldName(registerName);
        String itemInternalName = ASMUtils.getInternalName(packageName, ItemGen.getItemClassName(registerName));
        {
            FieldVisitor fieldVisitor = classWriter.visitField(ACC_PUBLIC | ACC_FINAL | ACC_STATIC, itemFieldName, "Lnet/minecraft/item/Item;", null, null);
            fieldVisitor.visitEnd();
        }

        {
            clinit.visitTypeInsn(NEW, itemInternalName);
            clinit.visitInsn(DUP);
            clinit.visitMethodInsn(INVOKESPECIAL, itemInternalName, "<init>", "()V", false);
            clinit.visitFieldInsn(PUTSTATIC, internalName, itemFieldName, "Lnet/minecraft/item/Item;");
        }

        {
            registerItem.visitVarInsn(ALOAD, 1);
            registerItem.visitFieldInsn(GETSTATIC, internalName, itemFieldName, "Lnet/minecraft/item/Item;");
            registerItem.visitMethodInsn(INVOKEINTERFACE, "net/minecraftforge/registries/IForgeRegistry", "register", "(Lnet/minecraftforge/registries/IForgeRegistryEntry;)V", true);
        }
    }

    public void save(Filer classesFiler) throws IOException {
        {
            registerItem.visitInsn(RETURN);
            registerItem.visitMaxs(2, 2);
            registerItem.visitEnd();
        }

        {
            clinit.visitInsn(RETURN);
            clinit.visitMaxs(2, 0);
            clinit.visitEnd();
        }

        classWriter.visitEnd();

        classesFiler.write(internalName + ".class", classWriter.toByteArray());
    }
}
