package com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.util;

import com.github.mouse0w0.peach.mcmod.compiler.Filer;
import com.github.mouse0w0.peach.mcmod.util.ASMUtils;
import org.objectweb.asm.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ItemGroupsClass implements Opcodes {

    private final String internalName;

    private ClassWriter classWriter = new ClassWriter(0);
    private MethodVisitor clinit;

    private Set<String> visitedItemGroups = new HashSet<>();

    public ItemGroupsClass(String className) {
        this.internalName = ASMUtils.getInternalName(className);
        visitStart();
    }

    public String getInternalName() {
        return internalName;
    }

    public void visitStart() {
        MethodVisitor methodVisitor;

        classWriter.visit(V1_8, ACC_PUBLIC | ACC_SUPER, internalName, null, "java/lang/Object", null);

        ASMUtils.visitSource(classWriter);

        ASMUtils.visitDefaultConstructor(classWriter);
        {
            methodVisitor = classWriter.visitMethod(ACC_PRIVATE | ACC_STATIC, "find", "(Ljava/lang/String;)Lnet/minecraft/creativetab/CreativeTabs;", null, null);
            methodVisitor.visitCode();
            methodVisitor.visitFieldInsn(GETSTATIC, "net/minecraft/creativetab/CreativeTabs", "field_78032_a", "[Lnet/minecraft/creativetab/CreativeTabs;");
            methodVisitor.visitVarInsn(ASTORE, 1);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitInsn(ARRAYLENGTH);
            methodVisitor.visitVarInsn(ISTORE, 2);
            methodVisitor.visitInsn(ICONST_0);
            methodVisitor.visitVarInsn(ISTORE, 3);
            Label label1 = new Label();
            methodVisitor.visitLabel(label1);
            methodVisitor.visitFrame(Opcodes.F_APPEND, 3, new Object[]{"[Lnet/minecraft/creativetab/CreativeTabs;", Opcodes.INTEGER, Opcodes.INTEGER}, 0, null);
            methodVisitor.visitVarInsn(ILOAD, 3);
            methodVisitor.visitVarInsn(ILOAD, 2);
            Label label2 = new Label();
            methodVisitor.visitJumpInsn(IF_ICMPGE, label2);
            methodVisitor.visitVarInsn(ALOAD, 1);
            methodVisitor.visitVarInsn(ILOAD, 3);
            methodVisitor.visitInsn(AALOAD);
            methodVisitor.visitVarInsn(ASTORE, 4);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitVarInsn(ALOAD, 4);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/creativetab/CreativeTabs", "func_78013_b", "()Ljava/lang/String;", false);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
            Label label4 = new Label();
            methodVisitor.visitJumpInsn(IFEQ, label4);
            methodVisitor.visitVarInsn(ALOAD, 4);
            methodVisitor.visitInsn(ARETURN);
            methodVisitor.visitLabel(label4);
            methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            methodVisitor.visitIincInsn(3, 1);
            methodVisitor.visitJumpInsn(GOTO, label1);
            methodVisitor.visitLabel(label2);
            methodVisitor.visitFrame(Opcodes.F_CHOP, 3, null, 0, null);
            methodVisitor.visitInsn(ACONST_NULL);
            methodVisitor.visitInsn(ARETURN);
            methodVisitor.visitMaxs(2, 5);
            methodVisitor.visitEnd();
        }
        {
            clinit = methodVisitor = classWriter.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null);
            methodVisitor.visitCode();
        }
    }

    public void visitItemGroup(String itemGroup) {
        if (visitedItemGroups.contains(itemGroup)) return;

        visitedItemGroups.add(itemGroup);

        String fieldName = itemGroup.toUpperCase();
        {
            FieldVisitor fieldVisitor = classWriter.visitField(ACC_PUBLIC | ACC_FINAL | ACC_STATIC, fieldName, "Lnet/minecraft/creativetab/CreativeTabs;", null, null);
            fieldVisitor.visitEnd();
        }

        {
            clinit.visitLdcInsn(itemGroup);
            clinit.visitMethodInsn(INVOKESTATIC, internalName, "find", "(Ljava/lang/String;)Lnet/minecraft/creativetab/CreativeTabs;", false);
            clinit.visitFieldInsn(PUTSTATIC, internalName, fieldName, "Lnet/minecraft/creativetab/CreativeTabs;");
        }
    }

    public void save(Filer classesFiler) throws IOException {
        {
            clinit.visitInsn(RETURN);
            clinit.visitMaxs(1, 0);
            clinit.visitEnd();
        }

        classWriter.visitEnd();

        classesFiler.write(internalName + ".class", classWriter.toByteArray());
    }
}
