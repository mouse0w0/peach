package com.github.mouse0w0.peach.mcmod.generator.v1_12_2.bytecode;

import com.github.mouse0w0.peach.mcmod.BoundingBox;
import com.github.mouse0w0.peach.mcmod.generator.util.ASMUtils;
import com.github.mouse0w0.peach.mcmod.generator.util.JavaUtils;
import com.github.mouse0w0.peach.mcmod.generator.v1_12_2.Srgs;
import javafx.scene.paint.Color;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

public class BlockClassGenerator extends ClassGenerator {
    private MethodVisitor clinitMethod;
    private MethodVisitor initMethod;

    public BlockClassGenerator(String className) {
        super(className);
    }

    private void visitClinitMethod() {
        if (clinitMethod != null) return;

        clinitMethod = cw.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null);
        clinitMethod.visitCode();
    }

    public void visitBlock(String superclass, String material, String mapColor) {
        cw.visit(V1_8, ACC_PUBLIC | ACC_SUPER, thisName, null, superclass, null);

        ASMUtils.visitSource(cw);

        initMethod = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        initMethod.visitCode();
        initMethod.visitVarInsn(ALOAD, 0);
        visitMaterialAndMapColor(material, mapColor);
        initMethod.visitMethodInsn(INVOKESPECIAL, superclass, "<init>", "(Lnet/minecraft/block/material/Material;Lnet/minecraft/block/material/MapColor;)V", false);
    }

    public void visitBlock(String superclass, String material) {
        cw.visit(V1_8, ACC_PUBLIC | ACC_SUPER, thisName, null, superclass, null);

        ASMUtils.visitSource(cw);

        initMethod = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        initMethod.visitCode();
        initMethod.visitVarInsn(ALOAD, 0);
        initMethod.visitFieldInsn(GETSTATIC, "net/minecraft/block/material/Material", Srgs.MATERIALS.get(material), "Lnet/minecraft/block/material/Material;");
        initMethod.visitMethodInsn(INVOKESPECIAL, superclass, "<init>", "(Lnet/minecraft/block/material/Material;)V", false);
    }

    public void visitStairsBlock(String material, String mapColor) {
        cw.visit(V1_8, ACC_PUBLIC | ACC_SUPER, thisName, null, "net/minecraft/block/BlockStairs", null);

        ASMUtils.visitSource(cw);

        initMethod = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        initMethod.visitCode();
        initMethod.visitVarInsn(ALOAD, 0);
        initMethod.visitTypeInsn(NEW, "net/minecraft/block/Block");
        initMethod.visitInsn(DUP);
        initMethod.visitFieldInsn(GETSTATIC, "net/minecraft/block/material/Material", Srgs.MATERIALS.get(material), "Lnet/minecraft/block/material/Material;");
        initMethod.visitMethodInsn(INVOKESPECIAL, "net/minecraft/block/Block", "<init>", "(Lnet/minecraft/block/material/Material;)V", false);
        initMethod.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/Block", "func_176223_P", "()Lnet/minecraft/block/state/IBlockState;", false);
        initMethod.visitMethodInsn(INVOKESPECIAL, "net/minecraft/block/BlockStairs", "<init>", "(Lnet/minecraft/block/state/IBlockState;)V", false);

        initMethod.visitVarInsn(ALOAD, 0);
        initMethod.visitInsn(ICONST_1);
        initMethod.visitFieldInsn(PUTFIELD, thisName, "field_149783_u", "Z");

        visitMapColor(mapColor);
    }

    private void visitMaterialAndMapColor(String material, String mapColor) {
        initMethod.visitFieldInsn(GETSTATIC, "net/minecraft/block/material/Material", Srgs.MATERIALS.get(material), "Lnet/minecraft/block/material/Material;");
        if ("INHERIT".equals(mapColor)) {
            initMethod.visitFieldInsn(GETSTATIC, "net/minecraft/block/material/Material", Srgs.MATERIALS.get(material), "Lnet/minecraft/block/material/Material;");
            initMethod.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/material/Material", "func_151565_r", "()Lnet/minecraft/block/material/MapColor;", false);
        } else {
            initMethod.visitFieldInsn(GETSTATIC, "net/minecraft/block/material/MapColor", Srgs.MAP_COLORS.get(mapColor), "Lnet/minecraft/block/material/MapColor;");
        }
    }

    public void visitMapColor(String mapColor) {
        if ("INHERIT".equals(mapColor)) return;

        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "func_180659_g", "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/material/MapColor;", null, null);
        mv.visitCode();
        mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/material/MapColor", Srgs.MAP_COLORS.get(mapColor), "Lnet/minecraft/block/material/MapColor;");
        mv.visitInsn(ARETURN);
        mv.visitMaxs(1, 4);
        mv.visitEnd();
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
        initMethod.visitMethodInsn(INVOKEVIRTUAL, thisName, "func_149663_c", "(Ljava/lang/String;)Lnet/minecraft/block/Block;", false);
        initMethod.visitInsn(POP);
    }

    public void visitItemGroup(String owner, String identifier) {
        String _IDENTIFIER = JavaUtils.lowerUnderscoreToUpperUnderscore(identifier);

        initMethod.visitVarInsn(ALOAD, 0);
        initMethod.visitFieldInsn(GETSTATIC, owner, _IDENTIFIER, "Lnet/minecraft/creativetab/CreativeTabs;");
        initMethod.visitMethodInsn(INVOKEVIRTUAL, thisName, "func_149647_a", "(Lnet/minecraft/creativetab/CreativeTabs;)Lnet/minecraft/block/Block;", false);
        initMethod.visitInsn(POP);
    }

    public void visitSoundType(String soundType) {
        initMethod.visitVarInsn(ALOAD, 0);
        initMethod.visitFieldInsn(GETSTATIC, "net/minecraft/block/SoundType", Srgs.SOUND_TYPES.get(soundType), "Lnet/minecraft/block/SoundType;");
        initMethod.visitFieldInsn(PUTFIELD, thisName, "field_149762_H", "Lnet/minecraft/block/SoundType;");
    }

    public void visitHardness(float hardness) {
        initMethod.visitVarInsn(ALOAD, 0);
        ASMUtils.push(initMethod, hardness);
        initMethod.visitFieldInsn(PUTFIELD, thisName, "field_149782_v", "F");
    }

    public void visitResistance(float resistance) {
        initMethod.visitVarInsn(ALOAD, 0);
        ASMUtils.push(initMethod, resistance);
        initMethod.visitFieldInsn(PUTFIELD, thisName, "field_149781_w", "F");
    }

    public void visitSlipperiness(float slipperiness) {
        initMethod.visitVarInsn(ALOAD, 0);
        ASMUtils.push(initMethod, slipperiness);
        initMethod.visitFieldInsn(PUTFIELD, thisName, "field_149765_K", "F");
    }

    public void visitBrightness(int brightness) {
        initMethod.visitVarInsn(ALOAD, 0);
        ASMUtils.push(initMethod, brightness);
        initMethod.visitFieldInsn(PUTFIELD, thisName, "field_149784_t", "I");
    }

    public void visitOpacity(int opacity) {
        initMethod.visitVarInsn(ALOAD, 0);
        ASMUtils.push(initMethod, opacity);
        initMethod.visitFieldInsn(PUTFIELD, thisName, "field_149786_r", "I");
    }

    public void visitHarvestToolAndLevel(String harvestTool, int harvestLevel) {
        initMethod.visitVarInsn(ALOAD, 0);
        ASMUtils.push(initMethod, harvestTool);
        ASMUtils.push(initMethod, harvestLevel);
        initMethod.visitMethodInsn(INVOKEVIRTUAL, thisName, "setHarvestLevel", "(Ljava/lang/String;I)V", false);
    }

    public void visitInformation(String namespace, String identifier, int count) {
        String prefix = "tile." + namespace + "." + identifier + ".tooltip.";
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "func_190948_a", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Ljava/util/List;Lnet/minecraft/client/util/ITooltipFlag;)V", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Ljava/util/List<Ljava/lang/String;>;Lnet/minecraft/client/util/ITooltipFlag;)V", null);
        {
            AnnotationVisitor av = mv.visitAnnotation("Lnet/minecraftforge/fml/relauncher/SideOnly;", true);
            av.visitEnum("value", "Lnet/minecraftforge/fml/relauncher/Side;", "CLIENT");
            av.visitEnd();
        }
        mv.visitCode();
        for (int i = 0; i < count; i++) {
            mv.visitVarInsn(ALOAD, 3);
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

    public void visitTransparency() {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "func_149662_c", "(Lnet/minecraft/block/state/IBlockState;)Z", null, null);
        mv.visitCode();
        mv.visitInsn(ICONST_0);
        mv.visitInsn(IRETURN);
        mv.visitMaxs(1, 2);
        mv.visitEnd();
    }

    public void visitRenderType(String renderType) {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "func_180664_k", "()Lnet/minecraft/util/BlockRenderLayer;", null, null);
        mv.visitCode();
        mv.visitFieldInsn(GETSTATIC, "net/minecraft/util/BlockRenderLayer", renderType, "Lnet/minecraft/util/BlockRenderLayer;");
        mv.visitInsn(ARETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();
    }

    public void visitOffsetType(String offsetType) {
        cw.visitInnerClass("net/minecraft/block/Block$EnumOffsetType", "net/minecraft/block/Block", "EnumOffsetType", ACC_PUBLIC | ACC_FINAL | ACC_STATIC | ACC_ENUM);

        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "func_176218_Q", "()Lnet/minecraft/block/Block$EnumOffsetType;", null, null);
        mv.visitCode();
        mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/Block$EnumOffsetType", offsetType, "Lnet/minecraft/block/Block$EnumOffsetType;");
        mv.visitInsn(ARETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();
    }

    public void visitNoFullCube() {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "func_149686_d", "(Lnet/minecraft/block/state/IBlockState;)Z", null, null);
        mv.visitCode();
        mv.visitInsn(ICONST_0);
        mv.visitInsn(IRETURN);
        mv.visitMaxs(1, 2);
        mv.visitEnd();
    }

    public void visitBoundingBox(BoundingBox boundingBox) {
        {
            FieldVisitor fv = cw.visitField(ACC_PUBLIC | ACC_FINAL | ACC_STATIC, "AABB", "Lnet/minecraft/util/math/AxisAlignedBB;", null, null);
            fv.visitEnd();
        }

        {
            visitClinitMethod();
            clinitMethod.visitTypeInsn(NEW, "net/minecraft/util/math/AxisAlignedBB");
            clinitMethod.visitInsn(DUP);
            ASMUtils.push(clinitMethod, boundingBox.minX());
            ASMUtils.push(clinitMethod, boundingBox.minY());
            ASMUtils.push(clinitMethod, boundingBox.minZ());
            ASMUtils.push(clinitMethod, boundingBox.maxX());
            ASMUtils.push(clinitMethod, boundingBox.maxY());
            ASMUtils.push(clinitMethod, boundingBox.maxZ());
            clinitMethod.visitMethodInsn(INVOKESPECIAL, "net/minecraft/util/math/AxisAlignedBB", "<init>", "(DDDDDD)V", false);
            clinitMethod.visitFieldInsn(PUTSTATIC, thisName, "AABB", "Lnet/minecraft/util/math/AxisAlignedBB;");
        }

        {
            MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "func_185496_a", "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/util/math/AxisAlignedBB;", null, null);
            mv.visitCode();
            mv.visitFieldInsn(GETSTATIC, thisName, "AABB", "Lnet/minecraft/util/math/AxisAlignedBB;");
            mv.visitInsn(ARETURN);
            mv.visitMaxs(1, 4);
            mv.visitEnd();
        }
    }

    public void visitNoCollision() {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "func_180646_a", "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/util/math/AxisAlignedBB;", null, null);
        mv.visitCode();
        mv.visitInsn(ACONST_NULL);
        mv.visitInsn(ARETURN);
        mv.visitMaxs(1, 4);
        mv.visitEnd();
    }

    public void visitBeaconColor(Color color) {
        {
            FieldVisitor fv = cw.visitField(ACC_PUBLIC | ACC_FINAL | ACC_STATIC, "BEACON_COLOR", "[F", null, null);
            fv.visitEnd();
        }

        {
            visitClinitMethod();
            clinitMethod.visitInsn(ICONST_3);
            clinitMethod.visitIntInsn(NEWARRAY, T_FLOAT);
            clinitMethod.visitInsn(DUP);
            clinitMethod.visitInsn(ICONST_0);
            ASMUtils.push(clinitMethod, (float) color.getRed());
            clinitMethod.visitInsn(FASTORE);
            clinitMethod.visitInsn(DUP);
            clinitMethod.visitInsn(ICONST_1);
            ASMUtils.push(clinitMethod, (float) color.getGreen());
            clinitMethod.visitInsn(FASTORE);
            clinitMethod.visitInsn(DUP);
            clinitMethod.visitInsn(ICONST_2);
            ASMUtils.push(clinitMethod, (float) color.getBlue());
            clinitMethod.visitInsn(FASTORE);
            clinitMethod.visitFieldInsn(PUTSTATIC, thisName, "BEACON_COLOR", "[F");
        }

        {
            MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "getBeaconColorMultiplier", "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockPos;)[F", null, null);
            mv.visitCode();
            mv.visitFieldInsn(GETSTATIC, thisName, "BEACON_COLOR", "[F");
            mv.visitInsn(ARETURN);
            mv.visitMaxs(1, 5);
            mv.visitEnd();
        }
    }

    public void visitBeaconBase() {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "isBeaconBase", "(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockPos;)Z", null, null);
        mv.visitCode();
        mv.visitInsn(ICONST_1);
        mv.visitInsn(IRETURN);
        mv.visitMaxs(1, 4);
        mv.visitEnd();
    }

    public void visitClimbable() {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "isLadder", "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/EntityLivingBase;)Z", null, null);
        mv.visitCode();
        mv.visitInsn(ICONST_1);
        mv.visitInsn(IRETURN);
        mv.visitMaxs(1, 5);
        mv.visitEnd();
    }

    public void visitReplaceable() {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "func_176200_f", "(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;)Z", null, null);
        mv.visitCode();
        mv.visitInsn(ICONST_1);
        mv.visitInsn(IRETURN);
        mv.visitMaxs(1, 3);
        mv.visitEnd();
    }

    public void visitCanConnectRedstone() {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "canConnectRedstone", "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;)Z", null, null);
        mv.visitCode();
        mv.visitInsn(ICONST_1);
        mv.visitInsn(IRETURN);
        mv.visitMaxs(1, 5);
        mv.visitEnd();
    }

    public void visitRedstonePower(int redstonePower) {
        {
            MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "func_149744_f", "(Lnet/minecraft/block/state/IBlockState;)Z", null, null);
            mv.visitCode();
            mv.visitInsn(ICONST_1);
            mv.visitInsn(IRETURN);
            mv.visitMaxs(1, 2);
            mv.visitEnd();
        }
        {
            MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "func_180656_a", "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;)I", null, null);
            mv.visitCode();
            ASMUtils.push(mv, redstonePower);
            mv.visitInsn(IRETURN);
            mv.visitMaxs(1, 5);
            mv.visitEnd();
        }
    }

    public void visitEnchantPowerBonus(float enchantPowerBonus) {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "getEnchantPowerBonus", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)F", null, null);
        mv.visitCode();
        ASMUtils.push(mv, enchantPowerBonus);
        mv.visitInsn(FRETURN);
        mv.visitMaxs(1, 3);
        mv.visitEnd();
    }

    public void visitFlammability(int flammability) {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "getFlammability", "(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;)I", null, null);
        mv.visitCode();
        ASMUtils.push(mv, flammability);
        mv.visitInsn(IRETURN);
        mv.visitMaxs(1, 4);
        mv.visitEnd();
    }

    public void visitFireSpreadSpeed(int fireSpreadSpeed) {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "getFireSpreadSpeed", "(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;)I", null, null);
        mv.visitCode();
        ASMUtils.push(mv, fireSpreadSpeed);
        mv.visitInsn(IRETURN);
        mv.visitMaxs(1, 4);
        mv.visitEnd();
    }

    public void visitPushReaction(String pushReaction) {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "func_149656_h", "(Lnet/minecraft/block/state/IBlockState;)Lnet/minecraft/block/material/EnumPushReaction;", null, null);
        mv.visitCode();
        mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/material/EnumPushReaction", pushReaction, "Lnet/minecraft/block/material/EnumPushReaction;");
        mv.visitInsn(ARETURN);
        mv.visitMaxs(1, 2);
        mv.visitEnd();
    }

    public void visitAiPathNodeType(String aiPathNodeType) {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "getAiPathNodeType", "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/EntityLiving;)Lnet/minecraft/pathfinding/PathNodeType;", null, null);
        mv.visitCode();
        mv.visitFieldInsn(GETSTATIC, "net/minecraft/pathfinding/PathNodeType", aiPathNodeType, "Lnet/minecraft/pathfinding/PathNodeType;");
        mv.visitInsn(ARETURN);
        mv.visitMaxs(1, 5);
        mv.visitEnd();
    }

    public void visitCanPlantPlant(String plantType) {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "canSustainPlant", "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;Lnet/minecraftforge/common/IPlantable;)Z", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 5);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitVarInsn(ALOAD, 3);
        mv.visitVarInsn(ALOAD, 4);
        mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/util/math/BlockPos", "func_177972_a", "(Lnet/minecraft/util/EnumFacing;)Lnet/minecraft/util/math/BlockPos;", false);
        mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraftforge/common/IPlantable", "getPlantType", "(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraftforge/common/EnumPlantType;", true);
        mv.visitFieldInsn(GETSTATIC, "net/minecraftforge/common/EnumPlantType", plantType, "Lnet/minecraftforge/common/EnumPlantType;");
        Label label0 = new Label();
        mv.visitJumpInsn(IF_ACMPNE, label0);
        mv.visitInsn(ICONST_1);
        Label label1 = new Label();
        mv.visitJumpInsn(GOTO, label1);
        mv.visitLabel(label0);
        mv.visitInsn(ICONST_0);
        mv.visitLabel(label1);
        mv.visitInsn(IRETURN);
        mv.visitMaxs(4, 6);
        mv.visitEnd();
    }

    public void visitHorizontalOpposite() {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "func_180642_a", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;FFFILnet/minecraft/entity/EntityLivingBase;)Lnet/minecraft/block/state/IBlockState;", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKEVIRTUAL, thisName, "func_176223_P", "()Lnet/minecraft/block/state/IBlockState;", false);
        mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockHorizontal", "field_185512_D", "Lnet/minecraft/block/properties/PropertyDirection;");
        mv.visitVarInsn(ALOAD, 8);
        mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/entity/EntityLivingBase", "func_174811_aO", "()Lnet/minecraft/util/EnumFacing;", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/util/EnumFacing", "func_176734_d", "()Lnet/minecraft/util/EnumFacing;", false);
        mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/block/state/IBlockState", "func_177226_a", "(Lnet/minecraft/block/properties/IProperty;Ljava/lang/Comparable;)Lnet/minecraft/block/state/IBlockState;", true);
        mv.visitInsn(ARETURN);
        mv.visitMaxs(3, 9);
        mv.visitEnd();
    }

    public void visitDirectionalOpposite() {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "func_180642_a", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;FFFILnet/minecraft/entity/EntityLivingBase;)Lnet/minecraft/block/state/IBlockState;", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKEVIRTUAL, thisName, "func_176223_P", "()Lnet/minecraft/block/state/IBlockState;", false);
        mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/BlockDirectional", "field_176387_N", "Lnet/minecraft/block/properties/PropertyDirection;");
        mv.visitVarInsn(ALOAD, 2);
        mv.visitVarInsn(ALOAD, 8);
        mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/util/EnumFacing", "func_190914_a", "(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/EntityLivingBase;)Lnet/minecraft/util/EnumFacing;", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/util/EnumFacing", "func_176734_d", "()Lnet/minecraft/util/EnumFacing;", false);
        mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/block/state/IBlockState", "func_177226_a", "(Lnet/minecraft/block/properties/IProperty;Ljava/lang/Comparable;)Lnet/minecraft/block/state/IBlockState;", true);
        mv.visitInsn(ARETURN);
        mv.visitMaxs(4, 9);
        mv.visitEnd();
    }

    @Override
    protected void doLast() {
        initMethod.visitInsn(RETURN);
        initMethod.visitMaxs(3, 1);
        initMethod.visitEnd();

        if (clinitMethod != null) {
            clinitMethod.visitInsn(RETURN);
            clinitMethod.visitMaxs(1, 0);
            clinitMethod.visitEnd();
        }
    }
}
