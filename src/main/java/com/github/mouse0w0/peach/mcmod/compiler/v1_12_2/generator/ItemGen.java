package com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.generator;

import com.github.mouse0w0.peach.mcmod.compiler.Environment;
import com.github.mouse0w0.peach.mcmod.compiler.Filer;
import com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.util.ItemGroupsClass;
import com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.util.ItemModelsClass;
import com.github.mouse0w0.peach.mcmod.compiler.v1_12_2.util.ModItemsClass;
import com.github.mouse0w0.peach.mcmod.element.impl.ItemElement;
import com.github.mouse0w0.peach.mcmod.model.json.JsonModel;
import com.github.mouse0w0.peach.mcmod.model.json.JsonModelHelper;
import com.github.mouse0w0.peach.mcmod.util.ASMUtils;
import com.github.mouse0w0.peach.mcmod.util.JavaUtils;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.GeneratorAdapter;

import java.io.BufferedWriter;
import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.objectweb.asm.Opcodes.*;

public class ItemGen extends Generator<ItemElement> {
    private ItemGroupsClass itemGroupsClass;
    private ModItemsClass itemsClass;
    private ItemModelsClass itemModelsClass;

    private String itemPackageName;
    private String namespace;

    public static String getItemFieldName(String registerName) {
        return registerName.toUpperCase();
    }

    public static String getItemClassName(String registerName) {
        return JavaUtils.lowerUnderscoreToUpperCamel(registerName);
    }

    @Override
    protected void before(Environment environment, Collection<ItemElement> elements) throws Exception {
        String packageName = environment.getRootPackageName();
        namespace = environment.getModSettings().getId();

        itemPackageName = packageName + ".item";
        itemsClass = new ModItemsClass(itemPackageName, namespace);
        itemGroupsClass = new ItemGroupsClass(packageName + ".init.ItemGroups");
        itemModelsClass = new ItemModelsClass(packageName + ".client.item.ItemModels", namespace, itemsClass);
    }

    @Override
    protected void after(Environment environment, Collection<ItemElement> elements) throws Exception {
        Filer classesFiler = environment.getClassesFiler();
        itemsClass.save(classesFiler);
        itemGroupsClass.save(classesFiler);
        itemModelsClass.save(classesFiler);
    }

    @Override
    protected void generate(Environment environment, ItemElement item) throws Exception {
        String registerName = item.getRegisterName();
        itemsClass.visitItem(registerName);
        itemGroupsClass.visitItemGroup(item.getItemGroup());
        itemModelsClass.visitModel(registerName);

        JsonModel model = environment.getModelManager().getItemModel(item.getModel());
        Map<String, String> textures = new LinkedHashMap<>();
        item.getTextures().forEach((key, value) -> textures.put(key, namespace + ":" + value));
        model.setTextures(textures);

        Filer assetsFiler = environment.getAssetsFiler();
        try (BufferedWriter writer = assetsFiler.newWriter("models", "item", registerName + ".json")) {
            JsonModelHelper.GSON.toJson(model, writer);
        }

        for (String texture : item.getTextures().values()) {
            Path textureFile = getItemTextureFilePath(environment, texture);
            assetsFiler.copy(textureFile, "textures/" + texture + ".png");
        }

        String internalName = ASMUtils.getInternalName(itemPackageName, ItemGen.getItemClassName(registerName));

        ClassWriter classWriter = new ClassWriter(0);
        MethodVisitor methodVisitor;
        GeneratorAdapter generatorAdapter;
        AnnotationVisitor annotationVisitor0;

        classWriter.visit(V1_8, ACC_PUBLIC | ACC_SUPER, internalName, null, "net/minecraft/item/Item", null);

        ASMUtils.visitSource(classWriter);

        {
            methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            generatorAdapter = new GeneratorAdapter(methodVisitor, ACC_PUBLIC, "<init>", "()V");
            methodVisitor.visitCode();
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitMethodInsn(INVOKESPECIAL, "net/minecraft/item/Item", "<init>", "()V", false);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitLdcInsn(namespace + ":" + registerName);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, internalName, "setRegistryName", "(Ljava/lang/String;)Lnet/minecraftforge/registries/IForgeRegistryEntry;", false);
            methodVisitor.visitInsn(POP);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitLdcInsn(namespace + "." + registerName);
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, internalName, "func_77655_b", "(Ljava/lang/String;)Lnet/minecraft/item/Item;", false);
            methodVisitor.visitInsn(POP);
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitFieldInsn(GETSTATIC, itemGroupsClass.getInternalName(), item.getItemGroup().toUpperCase(), "Lnet/minecraft/creativetab/CreativeTabs;");
            methodVisitor.visitMethodInsn(INVOKEVIRTUAL, internalName, "func_77637_a", "(Lnet/minecraft/creativetab/CreativeTabs;)Lnet/minecraft/item/Item;", false);
            methodVisitor.visitInsn(POP);

            if (item.getMaxStackSize() != 64) {
                methodVisitor.visitVarInsn(ALOAD, 0);
                generatorAdapter.push(item.getMaxStackSize());
                methodVisitor.visitMethodInsn(INVOKEVIRTUAL, internalName, "func_77625_d", "(I)Lnet/minecraft/item/Item;", false);
                methodVisitor.visitInsn(POP);
            }

            methodVisitor.visitInsn(RETURN);
            methodVisitor.visitMaxs(2, 1);
            methodVisitor.visitEnd();
        }
        if (item.isEffect()) {
            methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "func_77636_d", "(Lnet/minecraft/item/ItemStack;)Z", null, null);
            {
                annotationVisitor0 = methodVisitor.visitAnnotation("Lnet/minecraftforge/fml/relauncher/SideOnly;", true);
                annotationVisitor0.visitEnum("value", "Lnet/minecraftforge/fml/relauncher/Side;", "CLIENT");
                annotationVisitor0.visitEnd();
            }
            methodVisitor.visitCode();
            methodVisitor.visitInsn(ICONST_1);
            methodVisitor.visitInsn(IRETURN);
            methodVisitor.visitMaxs(1, 2);
            methodVisitor.visitEnd();
        }
        classWriter.visitEnd();

        environment.getClassesFiler().write(internalName + ".class", classWriter.toByteArray());
    }

    private Path getItemTextureFilePath(Environment environment, String textureName) {
        return environment.getSourceDirectory().resolve("resources/textures/" + textureName + ".png");
    }
}
