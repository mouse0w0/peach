package com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.bytecode;

import com.github.mouse0w0.peach.mcmod.compiler.util.ASMUtils;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

public class ItemGroupLoaderClassGenerator extends ClassGenerator {

    private MethodVisitor initMethod;

    public ItemGroupLoaderClassGenerator(String className) {
        super(className);

        cw.visit(V1_8, ACC_PUBLIC | ACC_SUPER, "peach/generated/itemGroup/ItemGroupLoader", null, "java/lang/Object", null);

        cw.visitSource("Peach.generated", null);

        ASMUtils.visitDefaultConstructor(cw);

        {
            initMethod = cw.visitMethod(ACC_PUBLIC | ACC_STATIC, "init", "()V", null, null);
            initMethod.visitCode();
        }
    }

    public void visitItemGroup(String className) {
        initMethod.visitTypeInsn(NEW, className);
        initMethod.visitInsn(DUP);
        initMethod.visitMethodInsn(INVOKESPECIAL, className, "<init>", "()V", false);
        initMethod.visitInsn(POP);
    }

    @Override
    protected void doLast() {
        initMethod.visitInsn(RETURN);
        initMethod.visitMaxs(2, 0);
        initMethod.visitEnd();
    }
}
