package com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.util;

import com.github.mouse0w0.peach.mcmod.compiler.Filer;
import com.github.mouse0w0.peach.mcmod.util.ASMUtils;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import java.io.IOException;

import static org.objectweb.asm.Opcodes.*;

public class ModItemGroupsClass {

    ClassWriter classWriter = new ClassWriter(0);
    MethodVisitor init;

    private String internalName;

    public ModItemGroupsClass(String className) {
        this.internalName = ASMUtils.getInternalName(className);
        init();
    }

    private void init() {
        classWriter.visit(V1_8, ACC_PUBLIC | ACC_SUPER, internalName, null, "java/lang/Object", null);

        ASMUtils.visitSource(classWriter);

        ASMUtils.visitDefaultConstructor(classWriter);
        {
        }
        {
            init = classWriter.visitMethod(ACC_PUBLIC | ACC_STATIC, "init", "()V", null, null);
            init.visitCode();
        }
    }

    public void addItemGroup(String internalName) {
        init.visitTypeInsn(NEW, internalName);
        init.visitInsn(DUP);
        init.visitMethodInsn(INVOKESPECIAL, internalName, "<init>", "()V", false);
        init.visitInsn(POP);
    }

    public void save(Filer classesFiler) throws IOException {
        init.visitInsn(RETURN);
        init.visitMaxs(1, 0);
        init.visitEnd();

        classWriter.visitEnd();

        classesFiler.write(internalName + ".class", classWriter.toByteArray());
    }
}
