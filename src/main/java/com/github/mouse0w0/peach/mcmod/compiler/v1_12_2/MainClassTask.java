package com.github.mouse0w0.peach.mcmod.compiler.v1_12_2;

import com.github.mouse0w0.peach.mcmod.compiler.CompileContext;
import com.github.mouse0w0.peach.mcmod.compiler.CompileTask;
import com.github.mouse0w0.peach.mcmod.compiler.CompilerImpl;
import com.github.mouse0w0.peach.mcmod.data.McModSettings;
import com.github.mouse0w0.peach.mcmod.util.ASMUtils;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

public class MainClassTask implements CompileTask {

    @Override
    public void run(CompileContext context) throws Exception {
        String packageName = context.getData(CompilerImpl.ROOT_PACKAGE_NAME);
        McModSettings projectInfo = context.getData(CompilerImpl.MOD_INFO_KEY);
        String className = ASMUtils.normalizeClassName(projectInfo.getId());
        String internalClassName = ASMUtils.getInternalName(packageName, className);

        ClassWriter classWriter = new ClassWriter(0);
        MethodVisitor methodVisitor;
        AnnotationVisitor annotationVisitor0;

        classWriter.visit(V1_8, ACC_PUBLIC | ACC_SUPER, internalClassName, null, "java/lang/Object", null);

        classWriter.visitSource("Peach.generated", null);

        {
            annotationVisitor0 = classWriter.visitAnnotation("Lnet/minecraftforge/fml/common/Mod;", true);
            annotationVisitor0.visit("modid", projectInfo.getId());
            annotationVisitor0.visit("version", projectInfo.getVersion());
            annotationVisitor0.visitEnd();
        }
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
            methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "onInit", "(Lnet/minecraftforge/fml/common/event/FMLInitializationEvent;)V", null, null);
            {
                annotationVisitor0 = methodVisitor.visitAnnotation("Lnet/minecraftforge/fml/common/Mod$EventHandler;", true);
                annotationVisitor0.visitEnd();
            }
            methodVisitor.visitCode();
            methodVisitor.visitMethodInsn(INVOKESTATIC, ASMUtils.getInternalName(packageName, "SmeltingRecipes"), "init", "()V", false);
            methodVisitor.visitInsn(RETURN);
            methodVisitor.visitMaxs(0, 2);
            methodVisitor.visitEnd();
        }
        classWriter.visitEnd();

        context.getClassesFiler().write(internalClassName + ".class", classWriter.toByteArray());
    }
}
