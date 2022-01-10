package com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.bytecode;

import com.github.mouse0w0.peach.mcmod.compiler.util.ASMUtils;
import com.github.mouse0w0.peach.mcmod.compiler.util.JavaUtils;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

public class ItemLoaderClassGenerator extends ClassGenerator {

    private final MethodVisitor cinitMethod;
    private final MethodVisitor registerItemMethod;
    private final MethodVisitor registerItemModelMethod;

    public ItemLoaderClassGenerator(String className) {
        super(className);

        cw.visit(V1_8, ACC_PUBLIC | ACC_SUPER, thisName, null, "java/lang/Object", null);

        cw.visitSource("Peach.generated", null);

        cw.visitInnerClass("net/minecraftforge/event/RegistryEvent$Register", "net/minecraftforge/event/RegistryEvent", "Register", ACC_PUBLIC | ACC_STATIC);

        cw.visitInnerClass("net/minecraftforge/fml/common/Mod$EventBusSubscriber", "net/minecraftforge/fml/common/Mod", "EventBusSubscriber", ACC_PUBLIC | ACC_STATIC | ACC_ANNOTATION | ACC_ABSTRACT | ACC_INTERFACE);

        ASMUtils.visitDefaultConstructor(cw);
        {
            registerItemMethod = cw.visitMethod(ACC_PUBLIC | ACC_STATIC, "registerItem", "(Lnet/minecraftforge/event/RegistryEvent$Register;)V", "(Lnet/minecraftforge/event/RegistryEvent$Register<Lnet/minecraft/item/Item;>;)V", null);
            {
                AnnotationVisitor av = registerItemMethod.visitAnnotation("Lnet/minecraftforge/fml/common/eventhandler/SubscribeEvent;", true);
                av.visitEnd();
            }
            registerItemMethod.visitCode();
            registerItemMethod.visitVarInsn(ALOAD, 0);
            registerItemMethod.visitMethodInsn(INVOKEVIRTUAL, "net/minecraftforge/event/RegistryEvent$Register", "getRegistry", "()Lnet/minecraftforge/registries/IForgeRegistry;", false);
            registerItemMethod.visitVarInsn(ASTORE, 1);
            registerItemMethod.visitVarInsn(ALOAD, 1);
            registerItemMethod.visitFieldInsn(GETSTATIC, "peach/generated/item/ItemLoader", "EXAMPLE_ITEM", "Lnet/minecraft/item/Item;");
            registerItemMethod.visitMethodInsn(INVOKEINTERFACE, "net/minecraftforge/registries/IForgeRegistry", "register", "(Lnet/minecraftforge/registries/IForgeRegistryEntry;)V", true);
        }
        {
            registerItemModelMethod = cw.visitMethod(ACC_PUBLIC | ACC_STATIC, "registerItemModel", "(Lnet/minecraftforge/client/event/ModelRegistryEvent;)V", null, null);
            {
                AnnotationVisitor av = registerItemModelMethod.visitAnnotation("Lnet/minecraftforge/fml/common/eventhandler/SubscribeEvent;", true);
                av.visitEnd();
            }
            {
                AnnotationVisitor av = registerItemModelMethod.visitAnnotation("Lnet/minecraftforge/fml/relauncher/SideOnly;", true);
                av.visitEnum("value", "Lnet/minecraftforge/fml/relauncher/Side;", "CLIENT");
                av.visitEnd();
            }
            registerItemModelMethod.visitCode();
        }
        {
            MethodVisitor mv = cw.visitMethod(ACC_PRIVATE | ACC_STATIC, "registerItemModel", "(Lnet/minecraft/item/Item;)V", null, null);
            {
                AnnotationVisitor av = mv.visitAnnotation("Lnet/minecraftforge/fml/relauncher/SideOnly;", true);
                av.visitEnum("value", "Lnet/minecraftforge/fml/relauncher/Side;", "CLIENT");
                av.visitEnd();
            }
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitInsn(ICONST_0);
            mv.visitTypeInsn(NEW, "net/minecraft/client/renderer/block/model/ModelResourceLocation");
            mv.visitInsn(DUP);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/Item", "getRegistryName", "()Lnet/minecraft/util/ResourceLocation;", false);
            mv.visitLdcInsn("inventory");
            mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/renderer/block/model/ModelResourceLocation", "<init>", "(Lnet/minecraft/util/ResourceLocation;Ljava/lang/String;)V", false);
            mv.visitMethodInsn(INVOKESTATIC, "net/minecraftforge/client/model/ModelLoader", "setCustomModelResourceLocation", "(Lnet/minecraft/item/Item;ILnet/minecraft/client/renderer/block/model/ModelResourceLocation;)V", false);
            mv.visitInsn(RETURN);
            mv.visitMaxs(6, 1);
            mv.visitEnd();
        }
        {
            cinitMethod = cw.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null);
            cinitMethod.visitCode();
        }
        cw.visitEnd();
    }

    private void visitModId(String modId) {
        AnnotationVisitor av = cw.visitAnnotation("Lnet/minecraftforge/fml/common/Mod$EventBusSubscriber;", true);
        av.visit("modid", modId);
        av.visitEnd();
    }

    private void visitItem(String identifier, String className) {
        String _IDENTIFIER = JavaUtils.lowerUnderscoreToUpperUnderscore(identifier);

        FieldVisitor fv = cw.visitField(ACC_PUBLIC | ACC_STATIC, "EXAMPLE_ITEM", "Lnet/minecraft/item/Item;", null, null);
        fv.visitEnd();

        cinitMethod.visitTypeInsn(NEW, className);
        cinitMethod.visitInsn(DUP);
        cinitMethod.visitMethodInsn(INVOKESPECIAL, className, "<init>", "()V", false);
        cinitMethod.visitFieldInsn(PUTSTATIC, thisName, _IDENTIFIER, "Lnet/minecraft/item/Item;");

        registerItemMethod.visitVarInsn(ALOAD, 1);
        registerItemMethod.visitFieldInsn(GETSTATIC, thisName, _IDENTIFIER, "Lnet/minecraft/item/Item;");
        registerItemMethod.visitMethodInsn(INVOKEINTERFACE, "net/minecraftforge/registries/IForgeRegistry", "register", "(Lnet/minecraftforge/registries/IForgeRegistryEntry;)V", true);

        registerItemModelMethod.visitFieldInsn(GETSTATIC, thisName, _IDENTIFIER, "Lnet/minecraft/item/Item;");
        registerItemModelMethod.visitMethodInsn(INVOKESTATIC, thisName, "registerItemModel", "(Lnet/minecraft/item/Item;)V", false);
    }

    @Override
    protected void doLast() {
        registerItemMethod.visitInsn(RETURN);
        registerItemMethod.visitMaxs(2, 2);
        registerItemMethod.visitEnd();

        registerItemModelMethod.visitInsn(RETURN);
        registerItemModelMethod.visitMaxs(1, 1);
        registerItemModelMethod.visitEnd();

        cinitMethod.visitInsn(RETURN);
        cinitMethod.visitMaxs(2, 0);
        cinitMethod.visitEnd();
    }
}
