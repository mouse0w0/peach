package com.github.mouse0w0.peach.mcmod.compiler.v1_12_2;

import com.github.mouse0w0.peach.mcmod.compiler.CompileTask;
import com.github.mouse0w0.peach.mcmod.compiler.Context;
import com.github.mouse0w0.peach.mcmod.compiler.util.ASMUtils;
import com.github.mouse0w0.peach.mcmod.compiler.util.JavaUtils;
import com.github.mouse0w0.peach.mcmod.project.McModMetadata;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

public class MainClassTask implements CompileTask {

    @Override
    public void run(Context context) throws Exception {
        McModMetadata metadata = context.getMetadata();
        String className = JavaUtils.lowerUnderscoreToUpperCamel(metadata.getId());
        String internalClassName = context.getInternalName(className);

        ClassWriter classWriter = new ClassWriter(0);
        MethodVisitor methodVisitor;
        AnnotationVisitor annotationVisitor0;

        classWriter.visit(V1_8, ACC_PUBLIC | ACC_SUPER, internalClassName, null, "java/lang/Object", null);

        ASMUtils.visitSource(classWriter);

        {
            annotationVisitor0 = classWriter.visitAnnotation("Lnet/minecraftforge/fml/common/Mod;", true);
            annotationVisitor0.visit("modid", metadata.getId());
            annotationVisitor0.visit("version", metadata.getVersion());
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
            methodVisitor.visitMethodInsn(INVOKESTATIC, context.getInternalName("itemGroup/ItemGroupLoader"), "init", "()V", false);
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
            methodVisitor.visitMethodInsn(INVOKESTATIC, context.getInternalName("SmeltingRecipeLoader"), "init", "()V", false);
            methodVisitor.visitInsn(RETURN);
            methodVisitor.visitMaxs(0, 2);
            methodVisitor.visitEnd();
        }
        classWriter.visitEnd();

        context.getClassesFiler().write(internalClassName + ".class", classWriter.toByteArray());
    }
}
