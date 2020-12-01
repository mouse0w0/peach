package com.github.mouse0w0.peach.mcmod.compiler.v1_12_2;

import com.github.mouse0w0.peach.mcmod.compiler.CompileTask;
import com.github.mouse0w0.peach.mcmod.compiler.Compiler;
import com.github.mouse0w0.peach.mcmod.compiler.util.ASMUtils;
import com.github.mouse0w0.peach.mcmod.project.McModMetadata;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

public class MainClassTask implements CompileTask {

    @Override
    public void run(Compiler compiler) throws Exception {
        McModMetadata modSettings = compiler.getMetadata();
        String className = ASMUtils.normalizeClassName(modSettings.getId());
        String internalClassName = ASMUtils.getInternalName(compiler.getRootPackageName(), className);

        ClassWriter classWriter = new ClassWriter(0);
        MethodVisitor methodVisitor;
        AnnotationVisitor annotationVisitor0;

        classWriter.visit(V1_8, ACC_PUBLIC | ACC_SUPER, internalClassName, null, "java/lang/Object", null);

        ASMUtils.visitSource(classWriter);

        {
            annotationVisitor0 = classWriter.visitAnnotation("Lnet/minecraftforge/fml/common/Mod;", true);
            annotationVisitor0.visit("modid", modSettings.getId());
            annotationVisitor0.visit("version", modSettings.getVersion());
            annotationVisitor0.visitEnd();
        }
        ASMUtils.visitDefaultConstructor(classWriter);
        {
            methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "onPreInit", "(Lnet/minecraftforge/fml/common/event/FMLPreInitializationEvent;)V", null, null);
            {
                annotationVisitor0 = methodVisitor.visitAnnotation("Lnet/minecraftforge/fml/common/Mod$EventHandler;", true);
                annotationVisitor0.visitEnd();
            }
            methodVisitor.visitCode();
            methodVisitor.visitMethodInsn(INVOKESTATIC, ASMUtils.getInternalName(compiler.getRootPackageName() + ".itemGroup.ModItemGroups"), "init", "()V", false);
            methodVisitor.visitInsn(RETURN);
            methodVisitor.visitMaxs(0, 2);
            methodVisitor.visitEnd();
        }
        {
            methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "onInit", "(Lnet/minecraftforge/fml/common/event/FMLInitializationEvent;)V", null, null);
            {
                annotationVisitor0 = methodVisitor.visitAnnotation("Lnet/minecraftforge/fml/common/Mod$EventHandler;", true);
                annotationVisitor0.visitEnd();
            }
            methodVisitor.visitCode();
            methodVisitor.visitMethodInsn(INVOKESTATIC, ASMUtils.getInternalName(compiler.getRootPackageName(), "SmeltingRecipes"), "init", "()V", false);
            methodVisitor.visitInsn(RETURN);
            methodVisitor.visitMaxs(0, 2);
            methodVisitor.visitEnd();
        }
        classWriter.visitEnd();

        compiler.getClassesFiler().write(internalClassName + ".class", classWriter.toByteArray());
    }
}
