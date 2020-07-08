package com.github.mouse0w0.peach.forge.compiler.v1_12_2;

import com.github.mouse0w0.peach.forge.ForgeModInfo;
import com.github.mouse0w0.peach.forge.compiler.CompileContext;
import com.github.mouse0w0.peach.forge.compiler.CompileTask;
import com.github.mouse0w0.peach.forge.compiler.ForgeCompiler;
import com.github.mouse0w0.peach.util.ASMUtils;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import java.nio.file.Path;

import static org.objectweb.asm.Opcodes.*;

public class MainClassTask implements CompileTask {

    @Override
    public void run(CompileContext context) throws Exception {
        String packageName = context.getData(ForgeCompiler.ROOT_PACKAGE_NAME);
        ForgeModInfo projectInfo = context.getData(ForgeCompiler.MOD_INFO_KEY);
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
            Label label0 = new Label();
            methodVisitor.visitLabel(label0);
            methodVisitor.visitLineNumber(6, label0);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            methodVisitor.visitInsn(RETURN);
            Label label1 = new Label();
            methodVisitor.visitLabel(label1);
            methodVisitor.visitLocalVariable("this", ASMUtils.getDescriptor(internalClassName), null, label0, label1, 0);
            methodVisitor.visitMaxs(1, 1);
            methodVisitor.visitEnd();
        }
        classWriter.visitEnd();

        Path path = context.getData(ForgeCompiler.CLASSES_STORE_PATH).resolve(internalClassName + ".class");
        context.write(path, classWriter.toByteArray());
    }
}
