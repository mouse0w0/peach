package com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.bytecode;

import com.github.mouse0w0.peach.mcmod.compiler.util.ASMUtils;
import com.github.mouse0w0.peach.mcmod.compiler.util.JavaUtils;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import java.util.HashSet;
import java.util.Set;

import static org.objectweb.asm.Opcodes.*;

public class ItemGroupsClassGenerator extends ClassGenerator {
    private final Set<String> visitedItemGroups = new HashSet<>();
    private final MethodVisitor cinitMethod;

    public ItemGroupsClassGenerator(String className) {
        super(className);

        cw.visit(V1_8, ACC_PUBLIC | ACC_SUPER, thisName, null, "java/lang/Object", null);

        ASMUtils.visitDefaultConstructor(cw);
        {
            MethodVisitor mv = cw.visitMethod(ACC_PRIVATE | ACC_STATIC, "find", "(Ljava/lang/String;)Lnet/minecraft/creativetab/CreativeTabs;", null, null);
            mv.visitCode();
            mv.visitFieldInsn(GETSTATIC, "net/minecraft/creativetab/CreativeTabs", "field_78032_a", "[Lnet/minecraft/creativetab/CreativeTabs;");
            mv.visitVarInsn(ASTORE, 1);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitInsn(ARRAYLENGTH);
            mv.visitVarInsn(ISTORE, 2);
            mv.visitInsn(ICONST_0);
            mv.visitVarInsn(ISTORE, 3);
            Label label0 = new Label();
            mv.visitLabel(label0);
            mv.visitVarInsn(ILOAD, 3);
            mv.visitVarInsn(ILOAD, 2);
            Label label1 = new Label();
            mv.visitJumpInsn(IF_ICMPGE, label1);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitVarInsn(ILOAD, 3);
            mv.visitInsn(AALOAD);
            mv.visitVarInsn(ASTORE, 4);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 4);
            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/creativetab/CreativeTabs", "func_78013_b", "()Ljava/lang/String;", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
            Label label2 = new Label();
            mv.visitJumpInsn(IFEQ, label2);
            mv.visitVarInsn(ALOAD, 4);
            mv.visitInsn(ARETURN);
            mv.visitLabel(label2);
            mv.visitIincInsn(3, 1);
            mv.visitJumpInsn(GOTO, label0);
            mv.visitLabel(label1);
            mv.visitInsn(ACONST_NULL);
            mv.visitInsn(ARETURN);
            mv.visitMaxs(2, 5);
            mv.visitEnd();
        }
        {
            cinitMethod = cw.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null);
            cinitMethod.visitCode();
        }
        cw.visitEnd();
    }

    public void visitItemGroup(String identifier) {
        if (visitedItemGroups.add(identifier)) {
            String upperUnderscore = JavaUtils.lowerUnderscoreToUpperUnderscore(identifier);
            FieldVisitor fv = cw.visitField(ACC_PUBLIC | ACC_STATIC, upperUnderscore, "Lnet/minecraft/creativetab/CreativeTabs;", null, null);
            fv.visitEnd();

            ASMUtils.push(cinitMethod, identifier);
            cinitMethod.visitMethodInsn(INVOKESTATIC, thisName, "find", "(Ljava/lang/String;)Lnet/minecraft/creativetab/CreativeTabs;", false);
            cinitMethod.visitFieldInsn(PUTSTATIC, thisName, upperUnderscore, "Lnet/minecraft/creativetab/CreativeTabs;");
        }
    }

    @Override
    protected void doLast() {
        cinitMethod.visitInsn(RETURN);
        cinitMethod.visitMaxs(1, 0);
        cinitMethod.visitEnd();
    }
}
