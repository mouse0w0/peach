package com.github.mouse0w0.peach.mcmod.generator.v1_12_2.bytecode;

import com.github.mouse0w0.peach.mcmod.IdMetadata;
import com.github.mouse0w0.peach.mcmod.generator.Context;
import com.github.mouse0w0.peach.mcmod.generator.util.ASMUtils;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

public class ItemGroupClassGenerator extends ClassGenerator {

    public ItemGroupClassGenerator(String className) {
        super(className);

        cw.visit(V1_8, ACC_PUBLIC | ACC_SUPER, className, null, "net/minecraft/creativetab/CreativeTabs", null);

        ASMUtils.visitSource(cw);

        cw.visitInnerClass("net/minecraftforge/fml/common/registry/GameRegistry$ObjectHolder", "net/minecraftforge/fml/common/registry/GameRegistry", "ObjectHolder", ACC_PUBLIC | ACC_STATIC | ACC_ANNOTATION | ACC_ABSTRACT | ACC_INTERFACE);
    }

    public void visitIdentifier(String identifier) {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        ASMUtils.push(mv, identifier);
        mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/creativetab/CreativeTabs", "<init>", "(Ljava/lang/String;)V", false);
        mv.visitInsn(RETURN);
        mv.visitMaxs(2, 1);
        mv.visitEnd();
    }

    public void visitTranslationKey(String translationKey) {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "func_78024_c", "()Ljava/lang/String;", null, null);
        {
            AnnotationVisitor av = mv.visitAnnotation("Lnet/minecraftforge/fml/relauncher/SideOnly;", true);
            av.visitEnum("value", "Lnet/minecraftforge/fml/relauncher/Side;", "CLIENT");
            av.visitEnd();
        }
        mv.visitCode();
        ASMUtils.push(mv, translationKey);
        mv.visitInsn(ARETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();
    }

    public void visitIcon(Context context, IdMetadata icon) {
        {
            FieldVisitor fv = cw.visitField(ACC_PRIVATE | ACC_FINAL | ACC_STATIC, "ICON", "Lnet/minecraft/item/Item;", null, null);
            {
                AnnotationVisitor av = fv.visitAnnotation("Lnet/minecraftforge/fml/common/registry/GameRegistry$ObjectHolder;", true);
                av.visit("value", context.mapIdentifier(icon.getIdentifier()));
                av.visitEnd();
            }
            fv.visitEnd();
        }

        {
            MethodVisitor mv = cw.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null);
            mv.visitCode();
            mv.visitInsn(ACONST_NULL);
            mv.visitFieldInsn(PUTSTATIC, thisName, "ICON", "Lnet/minecraft/item/Item;");
            mv.visitInsn(RETURN);
            mv.visitMaxs(1, 0);
            mv.visitEnd();
        }

        {
            MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "func_78016_d", "()Lnet/minecraft/item/ItemStack;", null, null);
            {
                AnnotationVisitor av = mv.visitAnnotation("Lnet/minecraftforge/fml/relauncher/SideOnly;", true);
                av.visitEnum("value", "Lnet/minecraftforge/fml/relauncher/Side;", "CLIENT");
                av.visitEnd();
            }
            mv.visitCode();
            mv.visitTypeInsn(NEW, "net/minecraft/item/ItemStack");
            mv.visitInsn(DUP);
            mv.visitFieldInsn(GETSTATIC, thisName, "ICON", "Lnet/minecraft/item/Item;");
            mv.visitInsn(ICONST_1);
            ASMUtils.push(mv, icon.getMetadata());
            mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/item/ItemStack", "<init>", "(Lnet/minecraft/item/Item;II)V", false);
            mv.visitInsn(ARETURN);
            mv.visitMaxs(5, 1);
            mv.visitEnd();
        }
    }

    public void visitHasSearchBar() {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "hasSearchBar", "()Z", null, null);
        {
            AnnotationVisitor av = mv.visitAnnotation("Lnet/minecraftforge/fml/relauncher/SideOnly;", true);
            av.visitEnum("value", "Lnet/minecraftforge/fml/relauncher/Side;", "CLIENT");
            av.visitEnd();
        }
        mv.visitCode();
        mv.visitInsn(ICONST_1);
        mv.visitInsn(IRETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();
    }

    public void visitBackgroundWithSearchBar() {
        visitBackground("textures/gui/container/creative_inventory/tab_item_search.png");
    }

    public void visitBackground(String background) {
        if (background == null) return;
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "getBackgroundImage", "()Lnet/minecraft/util/ResourceLocation;", null, null);
        {
            AnnotationVisitor av = mv.visitAnnotation("Lnet/minecraftforge/fml/relauncher/SideOnly;", true);
            av.visitEnum("value", "Lnet/minecraftforge/fml/relauncher/Side;", "CLIENT");
            av.visitEnd();
        }
        mv.visitCode();
        mv.visitTypeInsn(NEW, "net/minecraft/util/ResourceLocation");
        mv.visitInsn(DUP);
        mv.visitLdcInsn(background);
        mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/util/ResourceLocation", "<init>", "(Ljava/lang/String;)V", false);
        mv.visitInsn(ARETURN);
        mv.visitMaxs(3, 1);
        mv.visitEnd();
    }

    public String getThisName() {
        return thisName;
    }

    public byte[] toByteArray() {
        return cw.toByteArray();
    }
}
