package com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.bytecode;

import com.github.mouse0w0.peach.mcmod.compiler.util.ASMUtils;
import com.github.mouse0w0.peach.mcmod.compiler.util.JavaUtils;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

public class BlockLoaderClassGenerator extends ClassGenerator {

    private MethodVisitor clinitMethod;
    private MethodVisitor registerBlockMethod;

    public BlockLoaderClassGenerator(String className, String modId) {
        super(className);

        cw.visit(V1_8, ACC_PUBLIC | ACC_SUPER, className, null, "java/lang/Object", null);

        {
            AnnotationVisitor av = cw.visitAnnotation("Lnet/minecraftforge/fml/common/Mod$EventBusSubscriber;", true);
            av.visit("modid", modId);
            av.visitEnd();
        }
        cw.visitInnerClass("net/minecraftforge/event/RegistryEvent$Register", "net/minecraftforge/event/RegistryEvent", "Register", ACC_PUBLIC | ACC_STATIC);

        cw.visitInnerClass("net/minecraftforge/fml/common/Mod$EventBusSubscriber", "net/minecraftforge/fml/common/Mod", "EventBusSubscriber", ACC_PUBLIC | ACC_STATIC | ACC_ANNOTATION | ACC_ABSTRACT | ACC_INTERFACE);

        ASMUtils.visitDefaultConstructor(cw);

        {
            registerBlockMethod = cw.visitMethod(ACC_PUBLIC | ACC_STATIC, "registerBlock", "(Lnet/minecraftforge/event/RegistryEvent$Register;)V", "(Lnet/minecraftforge/event/RegistryEvent$Register<Lnet/minecraft/block/Block;>;)V", null);
            {
                AnnotationVisitor av = registerBlockMethod.visitAnnotation("Lnet/minecraftforge/fml/common/eventhandler/SubscribeEvent;", true);
                av.visitEnd();
            }
            registerBlockMethod.visitCode();
            registerBlockMethod.visitVarInsn(ALOAD, 0);
            registerBlockMethod.visitMethodInsn(INVOKEVIRTUAL, "net/minecraftforge/event/RegistryEvent$Register", "getRegistry", "()Lnet/minecraftforge/registries/IForgeRegistry;", false);
            registerBlockMethod.visitVarInsn(ASTORE, 1);

        }
        {
            clinitMethod = cw.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null);
            clinitMethod.visitCode();

        }
        cw.visitEnd();
    }

    public void visitBlock(String identifier, String className) {
        String _IDENTIFIER = JavaUtils.lowerUnderscoreToUpperUnderscore(identifier);

        FieldVisitor fv = cw.visitField(ACC_PUBLIC | ACC_FINAL | ACC_STATIC, _IDENTIFIER, "Lnet/minecraft/block/Block;", null, null);
        fv.visitEnd();

        clinitMethod.visitTypeInsn(NEW, className);
        clinitMethod.visitInsn(DUP);
        clinitMethod.visitMethodInsn(INVOKESPECIAL, className, "<init>", "()V", false);
        clinitMethod.visitFieldInsn(PUTSTATIC, thisName, _IDENTIFIER, "Lnet/minecraft/block/Block;");

        registerBlockMethod.visitVarInsn(ALOAD, 1);
        registerBlockMethod.visitFieldInsn(GETSTATIC, thisName, _IDENTIFIER, "Lnet/minecraft/block/Block;");
        registerBlockMethod.visitMethodInsn(INVOKEINTERFACE, "net/minecraftforge/registries/IForgeRegistry", "register", "(Lnet/minecraftforge/registries/IForgeRegistryEntry;)V", true);
    }

    @Override
    protected void doLast() {
        registerBlockMethod.visitInsn(RETURN);
        registerBlockMethod.visitMaxs(2, 2);
        registerBlockMethod.visitEnd();

        clinitMethod.visitInsn(RETURN);
        clinitMethod.visitMaxs(2, 0);
        clinitMethod.visitEnd();
    }
}
