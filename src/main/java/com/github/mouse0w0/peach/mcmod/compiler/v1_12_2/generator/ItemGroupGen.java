package com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.generator;

import com.github.mouse0w0.peach.mcmod.Item;
import com.github.mouse0w0.peach.mcmod.compiler.Environment;
import com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.util.ModItemGroupsClass;
import com.github.mouse0w0.peach.mcmod.element.Element;
import com.github.mouse0w0.peach.mcmod.element.impl.ItemGroup;
import com.github.mouse0w0.peach.mcmod.util.ASMUtils;
import com.github.mouse0w0.peach.mcmod.util.JavaUtils;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

import java.util.Collection;

import static org.objectweb.asm.Opcodes.*;

public class ItemGroupGen extends Generator<ItemGroup> {

    private String namespace;
    private String packageName;
    private ModItemGroupsClass itemGroupsClass;

    @Override
    public void generate(Environment environment, Collection<Element<ItemGroup>> elements) throws Exception {
        namespace = environment.getModSettings().getId();
        packageName = environment.getRootPackageName() + ".itemGroup";
        itemGroupsClass = new ModItemGroupsClass(packageName + ".ItemGroups");

        super.generate(environment, elements);

        itemGroupsClass.save(environment.getClassesFiler());
    }

    @Override
    protected void generate(Environment environment, Element<ItemGroup> element) throws Exception {
        ItemGroup itemGroup = element.get();

        Item icon = itemGroup.getIcon();
        String internalName = ASMUtils.getInternalName(packageName, JavaUtils.lowerUnderscoreToUpperCamel(itemGroup.getRegisterName()) + "ItemGroup");

        itemGroupsClass.addItemGroup(internalName);

        ClassWriter classWriter = new ClassWriter(0);
        FieldVisitor fieldVisitor;
        MethodVisitor methodVisitor;
        AnnotationVisitor annotationVisitor0;

        classWriter.visit(V1_8, ACC_PUBLIC | ACC_SUPER, internalName, null, "net/minecraft/creativetab/CreativeTabs", null);

        ASMUtils.visitSource(classWriter);

        classWriter.visitInnerClass("net/minecraftforge/fml/common/registry/GameRegistry$ObjectHolder", "net/minecraftforge/fml/common/registry/GameRegistry", "ObjectHolder", ACC_PUBLIC | ACC_STATIC | ACC_ANNOTATION | ACC_ABSTRACT | ACC_INTERFACE);

        {
            fieldVisitor = classWriter.visitField(ACC_PUBLIC | ACC_FINAL | ACC_STATIC, "ITEM", "Lnet/minecraft/item/Item;", null, null);
            {
                annotationVisitor0 = fieldVisitor.visitAnnotation("Lnet/minecraftforge/fml/common/registry/GameRegistry$ObjectHolder;", true);
                annotationVisitor0.visit("value", icon.getId());
                annotationVisitor0.visitEnd();
            }
            fieldVisitor.visitEnd();
        }
        {
            methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            methodVisitor.visitCode();
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitLdcInsn(itemGroup.getRegisterName());
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "net/minecraft/creativetab/CreativeTabs", "<init>", "(Ljava/lang/String;)V", false);
            methodVisitor.visitInsn(RETURN);
            methodVisitor.visitMaxs(2, 1);
            methodVisitor.visitEnd();
        }
        {
            methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "func_78024_c", "()Ljava/lang/String;", null, null);
            methodVisitor.visitCode();
            methodVisitor.visitLdcInsn("itemGroup." + namespace + "." + itemGroup.getRegisterName());
            methodVisitor.visitInsn(ARETURN);
            methodVisitor.visitMaxs(1, 1);
            methodVisitor.visitEnd();
        }
        {
            methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "func_78016_d", "()Lnet/minecraft/item/ItemStack;", null, null);
            methodVisitor.visitCode();
            methodVisitor.visitTypeInsn(NEW, "net/minecraft/item/ItemStack");
            methodVisitor.visitInsn(DUP);
            methodVisitor.visitFieldInsn(GETSTATIC, internalName, "ITEM", "Lnet/minecraft/item/Item;");
            methodVisitor.visitInsn(ICONST_1);
            ASMUtils.push(methodVisitor, icon.getMetadata());
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "net/minecraft/item/ItemStack", "<init>", "(Lnet/minecraft/item/Item;II)V", false);
            methodVisitor.visitInsn(ARETURN);
            methodVisitor.visitMaxs(3, 1);
            methodVisitor.visitEnd();
        }
//        {
//            methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "hasSearchBar", "()Z", null, null);
//            methodVisitor.visitCode();
//            methodVisitor.visitVarInsn(ALOAD, 0);
//            methodVisitor.visitMethodInsn(INVOKESPECIAL, "net/minecraft/creativetab/CreativeTabs", "hasSearchBar", "()Z", false);
//            methodVisitor.visitInsn(IRETURN);
//            methodVisitor.visitMaxs(1, 1);
//            methodVisitor.visitEnd();
//        }
        {
            methodVisitor = classWriter.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null);
            methodVisitor.visitCode();
            methodVisitor.visitInsn(ACONST_NULL);
            methodVisitor.visitFieldInsn(PUTSTATIC, internalName, "ITEM", "Lnet/minecraft/item/Item;");
            methodVisitor.visitInsn(RETURN);
            methodVisitor.visitMaxs(1, 0);
            methodVisitor.visitEnd();
        }
        classWriter.visitEnd();

        environment.getClassesFiler().write(internalName + ".class", classWriter.toByteArray());
    }
}
