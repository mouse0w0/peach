package com.github.mouse0w0.peach.mcmod.generator.v1_12_2.bytecode;

import com.github.mouse0w0.peach.mcmod.*;
import com.github.mouse0w0.peach.mcmod.generator.util.ASMUtils;
import com.github.mouse0w0.peach.mcmod.generator.util.JavaUtils;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import java.util.UUID;

import static org.objectweb.asm.Opcodes.*;

public class ItemClassGenerator extends ClassGenerator {
    private MethodVisitor initMethod;
    private MethodVisitor createAttributeModifiersMethod;
    private MethodVisitor canApplyAtEnchantingTableMethod;

    public ItemClassGenerator(String className) {
        super(className);
    }

    public void visitNormalItem() {
        cw.visit(V1_8, ACC_PUBLIC | ACC_SUPER, thisName, null, "net/minecraft/item/Item", null);

        ASMUtils.visitSource(cw);

        initMethod = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        initMethod.visitCode();
        initMethod.visitVarInsn(ALOAD, 0);
        initMethod.visitMethodInsn(INVOKESPECIAL, "net/minecraft/item/Item", "<init>", "()V", false);
    }

    public void visitFoodItem(int hunger, float saturation, boolean isWolfFood) {
        cw.visit(V1_8, ACC_PUBLIC | ACC_SUPER, thisName, null, "net/minecraft/item/ItemFood", null);

        ASMUtils.visitSource(cw);

        initMethod = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        initMethod.visitCode();
        initMethod.visitVarInsn(ALOAD, 0);
        ASMUtils.push(initMethod, hunger);
        ASMUtils.push(initMethod, saturation);
        ASMUtils.push(initMethod, isWolfFood);
        initMethod.visitMethodInsn(INVOKESPECIAL, "net/minecraft/item/ItemFood", "<init>", "(IFZ)V", false);
    }

    public void visitToolItem() {
        visitNormalItem();
    }

    /**
     * @param identifier must be like "modid$identifier"
     * @param damage     is attribute "generic.attackDamage" minus 3
     */
    public void visitSwordItem(String identifier, float damage) {
        cw.visit(V1_8, ACC_PUBLIC | ACC_SUPER, thisName, null, "net/minecraft/item/ItemSword", null);

        ASMUtils.visitSource(cw);

        cw.visitInnerClass("net/minecraft/item/Item$ToolMaterial", "net/minecraft/item/Item", "ToolMaterial", ACC_PUBLIC | ACC_FINAL | ACC_STATIC | ACC_ENUM);

        {
            initMethod = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            initMethod.visitCode();
            initMethod.visitVarInsn(ALOAD, 0);
            ASMUtils.push(initMethod, identifier);
            initMethod.visitInsn(ICONST_0);
            initMethod.visitInsn(ICONST_0);
            initMethod.visitInsn(FCONST_0);
            ASMUtils.push(initMethod, damage);
            initMethod.visitInsn(ICONST_0);
            initMethod.visitMethodInsn(INVOKESTATIC, "net/minecraftforge/common/util/EnumHelper", "addToolMaterial", "(Ljava/lang/String;IIFFI)Lnet/minecraft/item/Item$ToolMaterial;", false);
            initMethod.visitMethodInsn(INVOKESPECIAL, "net/minecraft/item/ItemSword", "<init>", "(Lnet/minecraft/item/Item$ToolMaterial;)V", false);
        }
    }

    public void visitArmorItem(String identifier, String equipSound) {
        cw.visit(V1_8, ACC_PUBLIC | ACC_SUPER, thisName, null, "net/minecraft/item/ItemArmor", null);

        ASMUtils.visitSource(cw);

        cw.visitInnerClass("net/minecraft/item/ItemArmor$ArmorMaterial", "net/minecraft/item/ItemArmor", "ArmorMaterial", ACC_PUBLIC | ACC_FINAL | ACC_STATIC | ACC_ENUM);

        {
            initMethod = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            initMethod.visitCode();
            initMethod.visitVarInsn(ALOAD, 0);
            ASMUtils.push(initMethod, identifier);
            initMethod.visitLdcInsn("custom");
            initMethod.visitInsn(ICONST_0);
            initMethod.visitInsn(ICONST_4);
            initMethod.visitIntInsn(NEWARRAY, T_INT);
            initMethod.visitInsn(DUP);
            initMethod.visitInsn(ICONST_0);
            initMethod.visitInsn(ICONST_0);
            initMethod.visitInsn(IASTORE);
            initMethod.visitInsn(DUP);
            initMethod.visitInsn(ICONST_1);
            initMethod.visitInsn(ICONST_0);
            initMethod.visitInsn(IASTORE);
            initMethod.visitInsn(DUP);
            initMethod.visitInsn(ICONST_2);
            initMethod.visitInsn(ICONST_0);
            initMethod.visitInsn(IASTORE);
            initMethod.visitInsn(DUP);
            initMethod.visitInsn(ICONST_3);
            initMethod.visitInsn(ICONST_0);
            initMethod.visitInsn(IASTORE);
            initMethod.visitInsn(ICONST_0);
            initMethod.visitFieldInsn(GETSTATIC, "net/minecraft/util/SoundEvent", "field_187505_a", "Lnet/minecraft/util/registry/RegistryNamespaced;");
            initMethod.visitTypeInsn(NEW, "net/minecraft/util/ResourceLocation");
            initMethod.visitInsn(DUP);
            ASMUtils.push(initMethod, equipSound);
            initMethod.visitMethodInsn(INVOKESPECIAL, "net/minecraft/util/ResourceLocation", "<init>", "(Ljava/lang/String;)V", false);
            initMethod.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/util/registry/RegistryNamespaced", "func_82594_a", "(Ljava/lang/Object;)Ljava/lang/Object;", false);
            initMethod.visitTypeInsn(CHECKCAST, "net/minecraft/util/SoundEvent");
            initMethod.visitInsn(FCONST_0);
            initMethod.visitMethodInsn(INVOKESTATIC, "net/minecraftforge/common/util/EnumHelper", "addArmorMaterial", "(Ljava/lang/String;Ljava/lang/String;I[IILnet/minecraft/util/SoundEvent;F)Lnet/minecraft/item/ItemArmor$ArmorMaterial;", false);
            initMethod.visitInsn(ICONST_0);
            initMethod.visitFieldInsn(GETSTATIC, "net/minecraft/inventory/EntityEquipmentSlot", "HEAD", "Lnet/minecraft/inventory/EntityEquipmentSlot;");
            initMethod.visitMethodInsn(INVOKESPECIAL, "net/minecraft/item/ItemArmor", "<init>", "(Lnet/minecraft/item/ItemArmor$ArmorMaterial;ILnet/minecraft/inventory/EntityEquipmentSlot;)V", false);
        }
    }

    public void visitIdentifier(String identifier) {
        initMethod.visitVarInsn(ALOAD, 0);
        ASMUtils.push(initMethod, identifier);
        initMethod.visitMethodInsn(INVOKEVIRTUAL, thisName, "setRegistryName", "(Ljava/lang/String;)Lnet/minecraftforge/registries/IForgeRegistryEntry;", false);
        initMethod.visitInsn(POP);
    }

    public void visitTranslationKey(String translationKey) {
        initMethod.visitVarInsn(ALOAD, 0);
        ASMUtils.push(initMethod, translationKey);
        initMethod.visitMethodInsn(INVOKEVIRTUAL, thisName, "func_77655_b", "(Ljava/lang/String;)Lnet/minecraft/item/Item;", false);
        initMethod.visitInsn(POP);
    }

    public void visitItemGroup(String owner, String identifier) {
        String _IDENTIFIER = JavaUtils.lowerUnderscoreToUpperUnderscore(identifier);

        initMethod.visitVarInsn(ALOAD, 0);
        initMethod.visitFieldInsn(GETSTATIC, owner, _IDENTIFIER, "Lnet/minecraft/creativetab/CreativeTabs;");
        initMethod.visitMethodInsn(INVOKEVIRTUAL, thisName, "func_77637_a", "(Lnet/minecraft/creativetab/CreativeTabs;)Lnet/minecraft/item/Item;", false);
        initMethod.visitInsn(POP);
    }

    public void visitMaxStackSize(int maxStackSize) {
        initMethod.visitVarInsn(ALOAD, 0);
        ASMUtils.push(initMethod, maxStackSize);
        initMethod.visitMethodInsn(INVOKEVIRTUAL, thisName, "func_77625_d", "(I)Lnet/minecraft/item/Item;", false);
        initMethod.visitInsn(POP);
    }

    public void visitDurability(int durability) {
        initMethod.visitVarInsn(ALOAD, 0);
        ASMUtils.push(initMethod, durability);
        initMethod.visitMethodInsn(INVOKEVIRTUAL, thisName, "func_77656_e", "(I)Lnet/minecraft/item/Item;", false);
        initMethod.visitInsn(POP);
    }

    public void visitToolAttribute(ToolAttribute toolAttribute) {
        initMethod.visitVarInsn(ALOAD, 0);
        ASMUtils.push(initMethod, toolAttribute.getType());
        ASMUtils.push(initMethod, toolAttribute.getLevel());
        initMethod.visitMethodInsn(INVOKEVIRTUAL, thisName, "setHarvestLevel", "(Ljava/lang/String;I)V", false);
    }

    public void visitHasEffect() {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "func_77636_d", "(Lnet/minecraft/item/ItemStack;)Z", null, null);
        {
            AnnotationVisitor av = mv.visitAnnotation("Lnet/minecraftforge/fml/relauncher/SideOnly;", true);
            av.visitEnum("value", "Lnet/minecraftforge/fml/relauncher/Side;", "CLIENT");
            av.visitEnd();
        }
        mv.visitCode();
        mv.visitInsn(ICONST_1);
        mv.visitInsn(IRETURN);
        mv.visitMaxs(1, 2);
        mv.visitEnd();
    }

    public void visitFuelBurnTime(int fuelBurnTime) {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "getItemBurnTime", "(Lnet/minecraft/item/ItemStack;)I", null, null);
        mv.visitCode();
        ASMUtils.push(mv, fuelBurnTime);
        mv.visitInsn(IRETURN);
        mv.visitMaxs(1, 2);
        mv.visitEnd();
    }

    public void visitDestroySpeed(float destroySpeed) {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "func_150893_a", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/block/state/IBlockState;)F", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitMethodInsn(INVOKEVIRTUAL, thisName, "func_150897_b", "(Lnet/minecraft/block/state/IBlockState;)Z", false);
        Label label0 = new Label();
        mv.visitJumpInsn(IFEQ, label0);
        ASMUtils.push(mv, destroySpeed);
        mv.visitInsn(FRETURN);
        mv.visitLabel(label0);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitMethodInsn(INVOKEVIRTUAL, thisName, "getToolClasses", "(Lnet/minecraft/item/ItemStack;)Ljava/util/Set;", false);
        mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Set", "iterator", "()Ljava/util/Iterator;", true);
        mv.visitVarInsn(ASTORE, 3);
        Label label1 = new Label();
        mv.visitLabel(label1);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Iterator", "hasNext", "()Z", true);
        Label label2 = new Label();
        mv.visitJumpInsn(IFEQ, label2);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(INVOKEINTERFACE, "java/util/Iterator", "next", "()Ljava/lang/Object;", true);
        mv.visitTypeInsn(CHECKCAST, "java/lang/String");
        mv.visitVarInsn(ASTORE, 4);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/block/state/IBlockState", "func_177230_c", "()Lnet/minecraft/block/Block;", true);
        mv.visitVarInsn(ALOAD, 4);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/Block", "isToolEffective", "(Ljava/lang/String;Lnet/minecraft/block/state/IBlockState;)Z", false);
        Label label3 = new Label();
        mv.visitJumpInsn(IFEQ, label3);
        ASMUtils.push(mv, destroySpeed);
        mv.visitInsn(FRETURN);
        mv.visitLabel(label3);
        mv.visitJumpInsn(GOTO, label1);
        mv.visitLabel(label2);
        mv.visitInsn(FCONST_1);
        mv.visitInsn(FRETURN);
        mv.visitMaxs(3, 5);
        mv.visitEnd();
    }

    public void visitCanDestroyAnyBlock() {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "func_150897_b", "(Lnet/minecraft/block/state/IBlockState;)Z", null, null);
        mv.visitCode();
        mv.visitInsn(ICONST_1);
        mv.visitInsn(IRETURN);
        mv.visitMaxs(1, 2);
        mv.visitEnd();
    }

    public void visitEnchantability(int enchantability) {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "func_77619_b", "()I", null, null);
        mv.visitCode();
        ASMUtils.push(mv, enchantability);
        mv.visitInsn(IRETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();
    }

    public void visitContainerItem(ItemRef containerItem) {
        {
            FieldVisitor fv = cw.visitField(ACC_PRIVATE | ACC_FINAL, "containerItem", "Lnet/minecraft/item/Item;", null, null);
            fv.visitEnd();
        }

        {
            initMethod.visitVarInsn(ALOAD, 0);
            initMethod.visitFieldInsn(GETSTATIC, thisName, "field_150901_e", "Lnet/minecraft/util/registry/RegistryNamespaced;");
            initMethod.visitTypeInsn(NEW, "net/minecraft/util/ResourceLocation");
            initMethod.visitInsn(DUP);
            ASMUtils.push(initMethod, containerItem.getId());
            initMethod.visitMethodInsn(INVOKESPECIAL, "net/minecraft/util/ResourceLocation", "<init>", "(Ljava/lang/String;)V", false);
            initMethod.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/util/registry/RegistryNamespaced", "func_82594_a", "(Ljava/lang/Object;)Ljava/lang/Object;", false);
            initMethod.visitTypeInsn(CHECKCAST, "net/minecraft/item/Item");
            initMethod.visitFieldInsn(PUTFIELD, thisName, "containerItem", "Lnet/minecraft/item/Item;");
        }

        {
            MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "hasContainerItem", "(Lnet/minecraft/item/ItemStack;)Z", null, null);
            mv.visitCode();
            mv.visitInsn(ICONST_1);
            mv.visitInsn(IRETURN);
            mv.visitMaxs(1, 2);
            mv.visitEnd();
        }

        {
            MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "getContainerItem", "(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;", null, null);
            mv.visitCode();
            mv.visitTypeInsn(NEW, "net/minecraft/item/ItemStack");
            mv.visitInsn(DUP);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, thisName, "containerItem", "Lnet/minecraft/item/Item;");
            mv.visitInsn(ICONST_1);
            mv.visitInsn(ICONST_0);
            mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/item/ItemStack", "<init>", "(Lnet/minecraft/item/Item;II)V", false);
            mv.visitInsn(ARETURN);
            mv.visitMaxs(5, 2);
            mv.visitEnd();
        }
    }

    public void visitRepairItem(ItemRef repairItem) {
        {
            FieldVisitor fv = cw.visitField(ACC_PRIVATE | ACC_FINAL, "repairItem", "Lnet/minecraft/item/Item;", null, null);
            fv.visitEnd();
        }

        {
            initMethod.visitVarInsn(ALOAD, 0);
            initMethod.visitFieldInsn(GETSTATIC, thisName, "field_150901_e", "Lnet/minecraft/util/registry/RegistryNamespaced;");
            initMethod.visitTypeInsn(NEW, "net/minecraft/util/ResourceLocation");
            initMethod.visitInsn(DUP);
            ASMUtils.push(initMethod, repairItem.getId());
            initMethod.visitMethodInsn(INVOKESPECIAL, "net/minecraft/util/ResourceLocation", "<init>", "(Ljava/lang/String;)V", false);
            initMethod.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/util/registry/RegistryNamespaced", "func_82594_a", "(Ljava/lang/Object;)Ljava/lang/Object;", false);
            initMethod.visitTypeInsn(CHECKCAST, "net/minecraft/item/Item");
            initMethod.visitFieldInsn(PUTFIELD, thisName, "repairItem", "Lnet/minecraft/item/Item;");
        }

        {
            MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "func_82789_a", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 2);
            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "func_77973_b", "()Lnet/minecraft/item/Item;", false);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, thisName, "repairItem", "Lnet/minecraft/item/Item;");
            Label label0 = new Label();
            mv.visitJumpInsn(IF_ACMPNE, label0);
            mv.visitVarInsn(ALOAD, 2);
            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "func_77960_j", "()I", false);
            ASMUtils.ifCmp(mv, IFNE, repairItem.getMetadata(), label0);
            mv.visitInsn(ICONST_1);
            Label label1 = new Label();
            mv.visitJumpInsn(GOTO, label1);
            mv.visitLabel(label0);
            mv.visitInsn(ICONST_0);
            mv.visitLabel(label1);
            mv.visitInsn(IRETURN);
            mv.visitMaxs(2, 3);
            mv.visitEnd();
        }
    }

    public void visitEquipmentSlot(EquipmentSlot equipmentSlot) {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "getEquipmentSlot", "(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/inventory/EntityEquipmentSlot;", null, null);
        mv.visitCode();
        mv.visitFieldInsn(GETSTATIC, "net/minecraft/inventory/EntityEquipmentSlot", equipmentSlot.name(), "Lnet/minecraft/inventory/EntityEquipmentSlot;");
        mv.visitInsn(ARETURN);
        mv.visitMaxs(1, 2);
        mv.visitEnd();
    }

    public void visitUseAnimation(UseAnimation useAnimation) {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "func_77661_b", "(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/EnumAction;", null, null);
        mv.visitCode();
        mv.visitFieldInsn(GETSTATIC, "net/minecraft/item/EnumAction", useAnimation.name(), "Lnet/minecraft/item/EnumAction;");
        mv.visitInsn(ARETURN);
        mv.visitMaxs(1, 2);
        mv.visitEnd();
    }

    public void visitUseDuration(int useDuration) {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "func_77626_a", "(Lnet/minecraft/item/ItemStack;)I", null, null);
        mv.visitCode();
        ASMUtils.push(mv, useDuration);
        mv.visitInsn(IRETURN);
        mv.visitMaxs(1, 2);
        mv.visitEnd();
    }

    public void visitAttributeModifier(AttributeModifier attributeModifier) {
        visitAttributeModifier(attributeModifier, UUID.randomUUID());
    }

    public void visitAttributeModifier(AttributeModifier attributeModifier, UUID uuid) {
        visitCreateAttributeModifiersMethod();
        ASMUtils.push(createAttributeModifiersMethod, attributeModifier.getAttribute());
        createAttributeModifiersMethod.visitTypeInsn(NEW, "net/minecraft/entity/ai/attributes/AttributeModifier");
        createAttributeModifiersMethod.visitInsn(DUP);
        createAttributeModifiersMethod.visitTypeInsn(NEW, "java/util/UUID");
        createAttributeModifiersMethod.visitInsn(DUP);
        ASMUtils.push(createAttributeModifiersMethod, uuid.getMostSignificantBits());
        ASMUtils.push(createAttributeModifiersMethod, uuid.getLeastSignificantBits());
        createAttributeModifiersMethod.visitMethodInsn(INVOKESPECIAL, "java/util/UUID", "<init>", "(JJ)V", false);
        ASMUtils.push(createAttributeModifiersMethod, attributeModifier.getAttribute());
        ASMUtils.push(createAttributeModifiersMethod, attributeModifier.getAmount());
        ASMUtils.push(createAttributeModifiersMethod, attributeModifier.getOperation().ordinal());
        createAttributeModifiersMethod.visitMethodInsn(INVOKESPECIAL, "net/minecraft/entity/ai/attributes/AttributeModifier", "<init>", "(Ljava/util/UUID;Ljava/lang/String;DI)V", false);
        createAttributeModifiersMethod.visitMethodInsn(INVOKEVIRTUAL, "com/google/common/collect/ImmutableMultimap$Builder", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMultimap$Builder;", false);
    }

    public void visitCreateAttributeModifiersMethod() {
        if (createAttributeModifiersMethod != null) return;

        cw.visitInnerClass("com/google/common/collect/ImmutableMultimap$Builder", "com/google/common/collect/ImmutableMultimap", "Builder", ACC_PUBLIC | ACC_STATIC);

        {
            FieldVisitor fv = cw.visitField(ACC_PRIVATE | ACC_FINAL, "modifiers", "Lcom/google/common/collect/Multimap;", "Lcom/google/common/collect/Multimap<Ljava/lang/String;Lnet/minecraft/entity/ai/attributes/AttributeModifier;>;", null);
            fv.visitEnd();
        }

        {
            initMethod.visitVarInsn(ALOAD, 0);
            initMethod.visitVarInsn(ALOAD, 0);
            initMethod.visitMethodInsn(INVOKESPECIAL, thisName, "createAttributeModifiers", "()Lcom/google/common/collect/Multimap;", false);
            initMethod.visitFieldInsn(PUTFIELD, thisName, "modifiers", "Lcom/google/common/collect/Multimap;");
        }

        {
            createAttributeModifiersMethod = cw.visitMethod(ACC_PRIVATE, "createAttributeModifiers", "()Lcom/google/common/collect/Multimap;", "()Lcom/google/common/collect/Multimap<Ljava/lang/String;Lnet/minecraft/entity/ai/attributes/AttributeModifier;>;", null);
            createAttributeModifiersMethod.visitCode();
            createAttributeModifiersMethod.visitMethodInsn(INVOKESTATIC, "com/google/common/collect/ImmutableMultimap", "builder", "()Lcom/google/common/collect/ImmutableMultimap$Builder;", false);
        }

        {
            MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "getAttributeModifiers", "(Lnet/minecraft/inventory/EntityEquipmentSlot;Lnet/minecraft/item/ItemStack;)Lcom/google/common/collect/Multimap;", "(Lnet/minecraft/inventory/EntityEquipmentSlot;Lnet/minecraft/item/ItemStack;)Lcom/google/common/collect/Multimap<Ljava/lang/String;Lnet/minecraft/entity/ai/attributes/AttributeModifier;>;", null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 1);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 2);
            mv.visitMethodInsn(INVOKEVIRTUAL, thisName, "getEquipmentSlot", "(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/inventory/EntityEquipmentSlot;", false);
            Label label0 = new Label();
            mv.visitJumpInsn(IF_ACMPNE, label0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, thisName, "modifiers", "Lcom/google/common/collect/Multimap;");
            Label label1 = new Label();
            mv.visitJumpInsn(GOTO, label1);
            mv.visitLabel(label0);
            mv.visitMethodInsn(INVOKESTATIC, "com/google/common/collect/ImmutableMultimap", "of", "()Lcom/google/common/collect/ImmutableMultimap;", false);
            mv.visitLabel(label1);
            mv.visitInsn(ARETURN);
            mv.visitMaxs(3, 3);
            mv.visitEnd();
        }
    }

    public void visitAcceptableEnchantment(EnchantmentType enchantmentType) {
        if (canApplyAtEnchantingTableMethod == null) {
            canApplyAtEnchantingTableMethod = cw.visitMethod(ACC_PUBLIC, "canApplyAtEnchantingTable", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/enchantment/Enchantment;)Z", null, null);
            canApplyAtEnchantingTableMethod.visitCode();
            canApplyAtEnchantingTableMethod.visitVarInsn(ALOAD, 2);
            canApplyAtEnchantingTableMethod.visitFieldInsn(GETFIELD, "net/minecraft/enchantment/Enchantment", "field_77351_y", "Lnet/minecraft/enchantment/EnumEnchantmentType;");
            canApplyAtEnchantingTableMethod.visitVarInsn(ASTORE, 3);
        }

        canApplyAtEnchantingTableMethod.visitFieldInsn(GETSTATIC, "net/minecraft/enchantment/EnumEnchantmentType", enchantmentType.name(), "Lnet/minecraft/enchantment/EnumEnchantmentType;");
        canApplyAtEnchantingTableMethod.visitVarInsn(ALOAD, 3);
        Label label0 = new Label();
        canApplyAtEnchantingTableMethod.visitJumpInsn(IF_ACMPNE, label0);
        canApplyAtEnchantingTableMethod.visitInsn(ICONST_1);
        canApplyAtEnchantingTableMethod.visitInsn(IRETURN);
        canApplyAtEnchantingTableMethod.visitLabel(label0);
    }

    public void visitInformation(String namespace, String identifier, int count) {
        String prefix = "item." + namespace + "." + identifier + ".";
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "func_77624_a", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Ljava/util/List;Lnet/minecraft/client/util/ITooltipFlag;)V", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Ljava/util/List<Ljava/lang/String;>;Lnet/minecraft/client/util/ITooltipFlag;)V", null);
        {
            AnnotationVisitor av = mv.visitAnnotation("Lnet/minecraftforge/fml/relauncher/SideOnly;", true);
            av.visitEnum("value", "Lnet/minecraftforge/fml/relauncher/Side;", "CLIENT");
            av.visitEnd();
        }
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 3);
        for (int i = 0; i < count; i++) {
            ASMUtils.push(mv, prefix + i);
            mv.visitInsn(ICONST_0);
            mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");
            mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/client/resources/I18n", "func_135052_a", "(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", false);
            mv.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z", true);
            mv.visitInsn(POP);
        }
        mv.visitInsn(RETURN);
        mv.visitMaxs(3, 5);
        mv.visitEnd();
    }

    public void visitAlwaysEdible() {
        initMethod.visitVarInsn(ALOAD, 0);
        initMethod.visitMethodInsn(INVOKEVIRTUAL, thisName, "func_77848_i", "()Lnet/minecraft/item/ItemFood;", false);
        initMethod.visitInsn(POP);
    }

    public void visitFoodContainerItem(ItemRef containerItem) {
        {
            FieldVisitor fv = cw.visitField(ACC_PRIVATE | ACC_FINAL, "foodContainerItem", "Lnet/minecraft/item/Item;", null, null);
            fv.visitEnd();
        }

        {
            initMethod.visitVarInsn(ALOAD, 0);
            initMethod.visitFieldInsn(GETSTATIC, thisName, "field_150901_e", "Lnet/minecraft/util/registry/RegistryNamespaced;");
            initMethod.visitTypeInsn(NEW, "net/minecraft/util/ResourceLocation");
            initMethod.visitInsn(DUP);
            ASMUtils.push(initMethod, containerItem.getId());
            initMethod.visitMethodInsn(INVOKESPECIAL, "net/minecraft/util/ResourceLocation", "<init>", "(Ljava/lang/String;)V", false);
            initMethod.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/util/registry/RegistryNamespaced", "func_82594_a", "(Ljava/lang/Object;)Ljava/lang/Object;", false);
            initMethod.visitTypeInsn(CHECKCAST, "net/minecraft/item/Item");
            initMethod.visitFieldInsn(PUTFIELD, thisName, "foodContainerItem", "Lnet/minecraft/item/Item;");
        }

        {
            MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "func_77654_b", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/EntityLivingBase;)Lnet/minecraft/item/ItemStack;", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitVarInsn(ALOAD, 2);
            mv.visitVarInsn(ALOAD, 3);
            mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/item/ItemFood", "func_77654_b", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/EntityLivingBase;)Lnet/minecraft/item/ItemStack;", false);
            mv.visitInsn(POP);
            mv.visitTypeInsn(NEW, "net/minecraft/item/ItemStack");
            mv.visitInsn(DUP);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, thisName, "foodContainerItem", "Lnet/minecraft/item/Item;");
            mv.visitInsn(ICONST_1);
            ASMUtils.push(mv, containerItem.getMetadata());
            mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/item/ItemStack", "<init>", "(Lnet/minecraft/item/Item;II)V", false);
            mv.visitInsn(ARETURN);
            mv.visitMaxs(5, 4);
            mv.visitEnd();
        }
    }

    public void visitArmorTexture(String texture) {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "getArmorTexture", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/Entity;Lnet/minecraft/inventory/EntityEquipmentSlot;Ljava/lang/String;)Ljava/lang/String;", null, null);
        {
            AnnotationVisitor av = mv.visitAnnotation("Lnet/minecraftforge/fml/relauncher/SideOnly;", true);
            av.visitEnum("value", "Lnet/minecraftforge/fml/relauncher/Side;", "CLIENT");
            av.visitEnd();
        }
        mv.visitCode();
        ASMUtils.push(mv, texture);
        mv.visitInsn(ARETURN);
        mv.visitMaxs(1, 5);
        mv.visitEnd();
    }

    public void visitAttackDamage(double attackDamage, boolean isSword) {
        if (isSword) {
            MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "func_150931_i", "()F", null, null);
            mv.visitCode();
            ASMUtils.push(mv, (float) attackDamage);
            mv.visitInsn(FRETURN);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }

        visitCreateAttributeModifiersMethod();
        createAttributeModifiersMethod.visitLdcInsn("generic.attackDamage");
        createAttributeModifiersMethod.visitTypeInsn(NEW, "net/minecraft/entity/ai/attributes/AttributeModifier");
        createAttributeModifiersMethod.visitInsn(DUP);
        createAttributeModifiersMethod.visitFieldInsn(GETSTATIC, thisName, "field_111210_e", "Ljava/util/UUID;");
        createAttributeModifiersMethod.visitLdcInsn("generic.attackDamage");
        ASMUtils.push(createAttributeModifiersMethod, attackDamage);
        createAttributeModifiersMethod.visitInsn(ICONST_0);
        createAttributeModifiersMethod.visitMethodInsn(INVOKESPECIAL, "net/minecraft/entity/ai/attributes/AttributeModifier", "<init>", "(Ljava/util/UUID;Ljava/lang/String;DI)V", false);
        createAttributeModifiersMethod.visitMethodInsn(INVOKEVIRTUAL, "com/google/common/collect/ImmutableMultimap$Builder", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMultimap$Builder;", false);
    }

    public void visitAttackSpeed(double attackSpeed) {
        visitCreateAttributeModifiersMethod();
        createAttributeModifiersMethod.visitLdcInsn("generic.attackSpeed");
        createAttributeModifiersMethod.visitTypeInsn(NEW, "net/minecraft/entity/ai/attributes/AttributeModifier");
        createAttributeModifiersMethod.visitInsn(DUP);
        createAttributeModifiersMethod.visitFieldInsn(GETSTATIC, thisName, "field_185050_h", "Ljava/util/UUID;");
        createAttributeModifiersMethod.visitLdcInsn("generic.attackSpeed");
        ASMUtils.push(createAttributeModifiersMethod, attackSpeed);
        createAttributeModifiersMethod.visitInsn(ICONST_0);
        createAttributeModifiersMethod.visitMethodInsn(INVOKESPECIAL, "net/minecraft/entity/ai/attributes/AttributeModifier", "<init>", "(Ljava/util/UUID;Ljava/lang/String;DI)V", false);
        createAttributeModifiersMethod.visitMethodInsn(INVOKEVIRTUAL, "com/google/common/collect/ImmutableMultimap$Builder", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMultimap$Builder;", false);
    }

    public void visitHitEntityLoss(int loss) {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "func_77644_a", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/entity/EntityLivingBase;)Z", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 1);
        ASMUtils.push(mv, loss);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "func_77972_a", "(ILnet/minecraft/entity/EntityLivingBase;)V", false);
        mv.visitInsn(ICONST_1);
        mv.visitInsn(IRETURN);
        mv.visitMaxs(3, 4);
        mv.visitEnd();
    }

    public void visitDestroyBlockLoss(int loss) {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "func_179218_a", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/EntityLivingBase;)Z", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 3);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitVarInsn(ALOAD, 4);
        mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/block/state/IBlockState", "func_185887_b", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)F", true);
        mv.visitInsn(FCONST_0);
        mv.visitInsn(FCMPL);
        Label label0 = new Label();
        mv.visitJumpInsn(IFEQ, label0);
        mv.visitVarInsn(ALOAD, 1);
        ASMUtils.push(mv, loss);
        mv.visitVarInsn(ALOAD, 5);
        mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "func_77972_a", "(ILnet/minecraft/entity/EntityLivingBase;)V", false);
        mv.visitLabel(label0);
        mv.visitInsn(ICONST_1);
        mv.visitInsn(IRETURN);
        mv.visitMaxs(3, 6);
        mv.visitEnd();
    }

    @Override
    protected void doLast() {
        initMethod.visitInsn(RETURN);
        initMethod.visitMaxs(-1, -1);
        initMethod.visitEnd();

        if (createAttributeModifiersMethod != null) {
            createAttributeModifiersMethod.visitMethodInsn(INVOKEVIRTUAL, "com/google/common/collect/ImmutableMultimap$Builder", "build", "()Lcom/google/common/collect/ImmutableMultimap;", false);
            createAttributeModifiersMethod.visitInsn(ARETURN);
            createAttributeModifiersMethod.visitMaxs(9, 1);
            createAttributeModifiersMethod.visitEnd();
        }

        if (canApplyAtEnchantingTableMethod != null) {
            canApplyAtEnchantingTableMethod.visitInsn(ICONST_0);
            canApplyAtEnchantingTableMethod.visitInsn(IRETURN);
            canApplyAtEnchantingTableMethod.visitMaxs(2, 4);
            canApplyAtEnchantingTableMethod.visitEnd();
        }
    }
}
