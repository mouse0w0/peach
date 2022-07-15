package com.github.mouse0w0.peach.mcmod.generator.v1_12_2.bytecode;

import com.github.mouse0w0.peach.mcmod.generator.util.ASMUtils;
import com.github.mouse0w0.peach.mcmod.generator.util.JavaUtils;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import java.util.HashSet;
import java.util.Set;

import static org.objectweb.asm.Opcodes.*;

public class ItemGroupsClassGenerator extends ClassGenerator {
    private final Set<String> visitedItemGroups = new HashSet<>();
    private final MethodVisitor clinitMethod;

    public ItemGroupsClassGenerator(String className) {
        super(className);

        cw.visit(V1_8, ACC_PUBLIC | ACC_SUPER, className, null, "java/lang/Object", null);

        ASMUtils.visitSource(cw);

        ASMUtils.visitDefaultConstructor(cw);
        {
            clinitMethod = cw.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null);
            clinitMethod.visitCode();
            Label label0 = new Label();
            Label label1 = new Label();
            Label label2 = new Label();
            clinitMethod.visitTryCatchBlock(label0, label1, label2, "java/lang/ReflectiveOperationException");
            clinitMethod.visitTypeInsn(NEW, "java/util/HashMap");
            clinitMethod.visitInsn(DUP);
            clinitMethod.visitMethodInsn(INVOKESPECIAL, "java/util/HashMap", "<init>", "()V", false);
            clinitMethod.visitVarInsn(ASTORE, 0);
            clinitMethod.visitMethodInsn(INVOKESTATIC, "net/minecraftforge/fml/common/FMLCommonHandler", "instance", "()Lnet/minecraftforge/fml/common/FMLCommonHandler;", false);
            clinitMethod.visitMethodInsn(INVOKEVIRTUAL, "net/minecraftforge/fml/common/FMLCommonHandler", "getSide", "()Lnet/minecraftforge/fml/relauncher/Side;", false);
            clinitMethod.visitMethodInsn(INVOKEVIRTUAL, "net/minecraftforge/fml/relauncher/Side", "isClient", "()Z", false);
            clinitMethod.visitJumpInsn(IFEQ, label0);
            clinitMethod.visitFieldInsn(GETSTATIC, "net/minecraft/creativetab/CreativeTabs", "field_78032_a", "[Lnet/minecraft/creativetab/CreativeTabs;");
            clinitMethod.visitVarInsn(ASTORE, 1);
            clinitMethod.visitVarInsn(ALOAD, 1);
            clinitMethod.visitInsn(ARRAYLENGTH);
            clinitMethod.visitVarInsn(ISTORE, 2);
            clinitMethod.visitInsn(ICONST_0);
            clinitMethod.visitVarInsn(ISTORE, 3);
            Label label3 = new Label();
            clinitMethod.visitLabel(label3);
            clinitMethod.visitVarInsn(ILOAD, 3);
            clinitMethod.visitVarInsn(ILOAD, 2);
            Label label4 = new Label();
            clinitMethod.visitJumpInsn(IF_ICMPGE, label4);
            clinitMethod.visitVarInsn(ALOAD, 1);
            clinitMethod.visitVarInsn(ILOAD, 3);
            clinitMethod.visitInsn(AALOAD);
            clinitMethod.visitVarInsn(ASTORE, 4);
            clinitMethod.visitVarInsn(ALOAD, 0);
            clinitMethod.visitVarInsn(ALOAD, 4);
            clinitMethod.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/creativetab/CreativeTabs", "func_78013_b", "()Ljava/lang/String;", false);
            clinitMethod.visitVarInsn(ALOAD, 4);
            clinitMethod.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", true);
            clinitMethod.visitInsn(POP);
            clinitMethod.visitIincInsn(3, 1);
            clinitMethod.visitJumpInsn(GOTO, label3);
            clinitMethod.visitLabel(label4);
            Label label5 = new Label();
            clinitMethod.visitJumpInsn(GOTO, label5);
            clinitMethod.visitLabel(label0);
            clinitMethod.visitLdcInsn(Type.getType("Lnet/minecraft/creativetab/CreativeTabs;"));
            clinitMethod.visitLdcInsn("field_78034_o");
            clinitMethod.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Class", "getDeclaredField", "(Ljava/lang/String;)Ljava/lang/reflect/Field;", false);
            clinitMethod.visitVarInsn(ASTORE, 1);
            clinitMethod.visitVarInsn(ALOAD, 1);
            clinitMethod.visitInsn(ICONST_1);
            clinitMethod.visitMethodInsn(INVOKEVIRTUAL, "java/lang/reflect/Field", "setAccessible", "(Z)V", false);
            clinitMethod.visitFieldInsn(GETSTATIC, "net/minecraft/creativetab/CreativeTabs", "field_78032_a", "[Lnet/minecraft/creativetab/CreativeTabs;");
            clinitMethod.visitVarInsn(ASTORE, 2);
            clinitMethod.visitVarInsn(ALOAD, 2);
            clinitMethod.visitInsn(ARRAYLENGTH);
            clinitMethod.visitVarInsn(ISTORE, 3);
            clinitMethod.visitInsn(ICONST_0);
            clinitMethod.visitVarInsn(ISTORE, 4);
            Label label6 = new Label();
            clinitMethod.visitLabel(label6);
            clinitMethod.visitVarInsn(ILOAD, 4);
            clinitMethod.visitVarInsn(ILOAD, 3);
            clinitMethod.visitJumpInsn(IF_ICMPGE, label1);
            clinitMethod.visitVarInsn(ALOAD, 2);
            clinitMethod.visitVarInsn(ILOAD, 4);
            clinitMethod.visitInsn(AALOAD);
            clinitMethod.visitVarInsn(ASTORE, 5);
            clinitMethod.visitVarInsn(ALOAD, 0);
            clinitMethod.visitVarInsn(ALOAD, 1);
            clinitMethod.visitVarInsn(ALOAD, 5);
            clinitMethod.visitMethodInsn(INVOKEVIRTUAL, "java/lang/reflect/Field", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", false);
            clinitMethod.visitTypeInsn(CHECKCAST, "java/lang/String");
            clinitMethod.visitVarInsn(ALOAD, 5);
            clinitMethod.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", true);
            clinitMethod.visitInsn(POP);
            clinitMethod.visitIincInsn(4, 1);
            clinitMethod.visitJumpInsn(GOTO, label6);
            clinitMethod.visitLabel(label1);
            clinitMethod.visitJumpInsn(GOTO, label5);
            clinitMethod.visitLabel(label2);
            clinitMethod.visitVarInsn(ASTORE, 1);
            clinitMethod.visitLabel(label5);
        }
        cw.visitEnd();
    }

    public void visitItemGroup(String identifier) {
        if (visitedItemGroups.add(identifier)) {
            String _IDENTIFER = JavaUtils.lowerUnderscoreToUpperUnderscore(identifier);
            FieldVisitor fv = cw.visitField(ACC_PUBLIC | ACC_STATIC | ACC_FINAL, _IDENTIFER, "Lnet/minecraft/creativetab/CreativeTabs;", null, null);
            fv.visitEnd();

            clinitMethod.visitVarInsn(ALOAD, 0);
            ASMUtils.push(clinitMethod, identifier);
            clinitMethod.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", true);
            clinitMethod.visitTypeInsn(CHECKCAST, "net/minecraft/creativetab/CreativeTabs");
            clinitMethod.visitFieldInsn(PUTSTATIC, thisName, _IDENTIFER, "Lnet/minecraft/creativetab/CreativeTabs;");
        }
    }

    @Override
    protected void doLast() {
        clinitMethod.visitInsn(RETURN);
        clinitMethod.visitMaxs(3, 6);
        clinitMethod.visitEnd();
    }
}
