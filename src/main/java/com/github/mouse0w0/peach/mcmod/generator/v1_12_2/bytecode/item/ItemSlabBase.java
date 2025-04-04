package com.github.mouse0w0.peach.mcmod.generator.v1_12_2.bytecode.item;

import com.github.mouse0w0.peach.mcmod.generator.util.ASMUtils;
import com.github.mouse0w0.peach.mcmod.generator.v1_12_2.bytecode.ClassGenerator;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

public class ItemSlabBase extends ClassGenerator {
    public ItemSlabBase(String className, String slabBlockClassName, String slabTypeClassName) {
        super(className);

        String slabTypeDescriptor = ASMUtils.getDescriptor(slabTypeClassName);

        MethodVisitor mv;
        AnnotationVisitor av;

        cw.visit(V1_8, ACC_PUBLIC | ACC_SUPER, className, null, "net/minecraft/item/ItemBlock", null);

        ASMUtils.visitSource(cw);

        {
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(Lnet/minecraft/block/Block;)V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/item/ItemBlock", "<init>", "(Lnet/minecraft/block/Block;)V", false);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/Block", "getRegistryName", "()Lnet/minecraft/util/ResourceLocation;", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, className, "setRegistryName", "(Lnet/minecraft/util/ResourceLocation;)Lnet/minecraftforge/registries/IForgeRegistryEntry;", false);
            mv.visitInsn(POP);
            mv.visitInsn(RETURN);
            mv.visitMaxs(2, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "func_180614_a", "(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumHand;Lnet/minecraft/util/EnumFacing;FFF)Lnet/minecraft/util/EnumActionResult;", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 1);
            mv.visitVarInsn(ALOAD, 4);
            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/entity/player/EntityPlayer", "func_184586_b", "(Lnet/minecraft/util/EnumHand;)Lnet/minecraft/item/ItemStack;", false);
            mv.visitVarInsn(ASTORE, 9);
            mv.visitVarInsn(ALOAD, 9);
            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "func_190926_b", "()Z", false);
            Label label0 = new Label();
            mv.visitJumpInsn(IFNE, label0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitVarInsn(ALOAD, 3);
            mv.visitVarInsn(ALOAD, 5);
            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/util/math/BlockPos", "func_177972_a", "(Lnet/minecraft/util/EnumFacing;)Lnet/minecraft/util/math/BlockPos;", false);
            mv.visitVarInsn(ALOAD, 5);
            mv.visitVarInsn(ALOAD, 9);
            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/entity/player/EntityPlayer", "func_175151_a", "(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;Lnet/minecraft/item/ItemStack;)Z", false);
            mv.visitJumpInsn(IFEQ, label0);
            mv.visitVarInsn(ALOAD, 2);
            mv.visitVarInsn(ALOAD, 3);
            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/world/World", "func_180495_p", "(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;", false);
            mv.visitVarInsn(ASTORE, 10);
            mv.visitVarInsn(ALOAD, 10);
            mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/block/state/IBlockState", "func_177230_c", "()Lnet/minecraft/block/Block;", true);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, className, "field_150939_a", "Lnet/minecraft/block/Block;");
            Label label1 = new Label();
            mv.visitJumpInsn(IF_ACMPNE, label1);
            mv.visitVarInsn(ALOAD, 10);
            mv.visitFieldInsn(GETSTATIC, slabBlockClassName, "TYPE", "Lnet/minecraft/block/properties/PropertyEnum;");
            mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/block/state/IBlockState", "func_177229_b", "(Lnet/minecraft/block/properties/IProperty;)Ljava/lang/Comparable;", true);
            mv.visitTypeInsn(CHECKCAST, slabTypeClassName);
            mv.visitVarInsn(ASTORE, 11);
            mv.visitVarInsn(ALOAD, 5);
            mv.visitFieldInsn(GETSTATIC, "net/minecraft/util/EnumFacing", "UP", "Lnet/minecraft/util/EnumFacing;");
            Label label2 = new Label();
            mv.visitJumpInsn(IF_ACMPNE, label2);
            mv.visitVarInsn(ALOAD, 11);
            mv.visitFieldInsn(GETSTATIC, slabTypeClassName, "BOTTOM", slabTypeDescriptor);
            Label label3 = new Label();
            mv.visitJumpInsn(IF_ACMPEQ, label3);
            mv.visitLabel(label2);
            mv.visitVarInsn(ALOAD, 5);
            mv.visitFieldInsn(GETSTATIC, "net/minecraft/util/EnumFacing", "DOWN", "Lnet/minecraft/util/EnumFacing;");
            mv.visitJumpInsn(IF_ACMPNE, label1);
            mv.visitVarInsn(ALOAD, 11);
            mv.visitFieldInsn(GETSTATIC, slabTypeClassName, "TOP", slabTypeDescriptor);
            mv.visitJumpInsn(IF_ACMPNE, label1);
            mv.visitLabel(label3);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, className, "makeState", "()Lnet/minecraft/block/state/IBlockState;", false);
            mv.visitVarInsn(ASTORE, 12);
            mv.visitVarInsn(ALOAD, 12);
            mv.visitVarInsn(ALOAD, 2);
            mv.visitVarInsn(ALOAD, 3);
            mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/block/state/IBlockState", "func_185890_d", "(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/util/math/AxisAlignedBB;", true);
            mv.visitVarInsn(ASTORE, 13);
            mv.visitVarInsn(ALOAD, 13);
            mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/Block", "field_185506_k", "Lnet/minecraft/util/math/AxisAlignedBB;");
            Label label4 = new Label();
            mv.visitJumpInsn(IF_ACMPEQ, label4);
            mv.visitVarInsn(ALOAD, 2);
            mv.visitVarInsn(ALOAD, 13);
            mv.visitVarInsn(ALOAD, 3);
            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/util/math/AxisAlignedBB", "func_186670_a", "(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/util/math/AxisAlignedBB;", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/world/World", "func_72855_b", "(Lnet/minecraft/util/math/AxisAlignedBB;)Z", false);
            mv.visitJumpInsn(IFEQ, label4);
            mv.visitVarInsn(ALOAD, 2);
            mv.visitVarInsn(ALOAD, 3);
            mv.visitVarInsn(ALOAD, 12);
            mv.visitIntInsn(BIPUSH, 11);
            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/world/World", "func_180501_a", "(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;I)Z", false);
            mv.visitJumpInsn(IFEQ, label4);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, className, "field_150939_a", "Lnet/minecraft/block/Block;");
            mv.visitVarInsn(ALOAD, 12);
            mv.visitVarInsn(ALOAD, 2);
            mv.visitVarInsn(ALOAD, 3);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/Block", "getSoundType", "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/Entity;)Lnet/minecraft/block/SoundType;", false);
            mv.visitVarInsn(ASTORE, 14);
            mv.visitVarInsn(ALOAD, 2);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitVarInsn(ALOAD, 3);
            mv.visitVarInsn(ALOAD, 14);
            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/SoundType", "func_185841_e", "()Lnet/minecraft/util/SoundEvent;", false);
            mv.visitFieldInsn(GETSTATIC, "net/minecraft/util/SoundCategory", "BLOCKS", "Lnet/minecraft/util/SoundCategory;");
            mv.visitVarInsn(ALOAD, 14);
            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/SoundType", "func_185843_a", "()F", false);
            mv.visitInsn(FCONST_1);
            mv.visitInsn(FADD);
            mv.visitInsn(FCONST_2);
            mv.visitInsn(FDIV);
            mv.visitVarInsn(ALOAD, 14);
            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/SoundType", "func_185847_b", "()F", false);
            mv.visitLdcInsn(Float.valueOf("0.8"));
            mv.visitInsn(FMUL);
            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/world/World", "func_184133_a", "(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/SoundEvent;Lnet/minecraft/util/SoundCategory;FF)V", false);
            mv.visitVarInsn(ALOAD, 9);
            mv.visitInsn(ICONST_1);
            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "func_190918_g", "(I)V", false);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitTypeInsn(INSTANCEOF, "net/minecraft/entity/player/EntityPlayerMP");
            mv.visitJumpInsn(IFEQ, label4);
            mv.visitFieldInsn(GETSTATIC, "net/minecraft/advancements/CriteriaTriggers", "field_193137_x", "Lnet/minecraft/advancements/critereon/PlacedBlockTrigger;");
            mv.visitVarInsn(ALOAD, 1);
            mv.visitTypeInsn(CHECKCAST, "net/minecraft/entity/player/EntityPlayerMP");
            mv.visitVarInsn(ALOAD, 3);
            mv.visitVarInsn(ALOAD, 9);
            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/advancements/critereon/PlacedBlockTrigger", "func_193173_a", "(Lnet/minecraft/entity/player/EntityPlayerMP;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/item/ItemStack;)V", false);
            mv.visitLabel(label4);
            mv.visitFieldInsn(GETSTATIC, "net/minecraft/util/EnumActionResult", "SUCCESS", "Lnet/minecraft/util/EnumActionResult;");
            mv.visitInsn(ARETURN);
            mv.visitLabel(label1);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitVarInsn(ALOAD, 9);
            mv.visitVarInsn(ALOAD, 2);
            mv.visitVarInsn(ALOAD, 3);
            mv.visitVarInsn(ALOAD, 5);
            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/util/math/BlockPos", "func_177972_a", "(Lnet/minecraft/util/EnumFacing;)Lnet/minecraft/util/math/BlockPos;", false);
            mv.visitMethodInsn(INVOKESPECIAL, className, "tryPlace", "(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z", false);
            Label label5 = new Label();
            mv.visitJumpInsn(IFEQ, label5);
            mv.visitFieldInsn(GETSTATIC, "net/minecraft/util/EnumActionResult", "SUCCESS", "Lnet/minecraft/util/EnumActionResult;");
            Label label6 = new Label();
            mv.visitJumpInsn(GOTO, label6);
            mv.visitLabel(label5);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitVarInsn(ALOAD, 2);
            mv.visitVarInsn(ALOAD, 3);
            mv.visitVarInsn(ALOAD, 4);
            mv.visitVarInsn(ALOAD, 5);
            mv.visitVarInsn(FLOAD, 6);
            mv.visitVarInsn(FLOAD, 7);
            mv.visitVarInsn(FLOAD, 8);
            mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/item/ItemBlock", "func_180614_a", "(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumHand;Lnet/minecraft/util/EnumFacing;FFF)Lnet/minecraft/util/EnumActionResult;", false);
            mv.visitLabel(label6);
            mv.visitInsn(ARETURN);
            mv.visitLabel(label0);
            mv.visitFieldInsn(GETSTATIC, "net/minecraft/util/EnumActionResult", "FAIL", "Lnet/minecraft/util/EnumActionResult;");
            mv.visitInsn(ARETURN);
            mv.visitMaxs(9, 15);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "func_179222_a", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/item/ItemStack;)Z", null, null);
            {
                av = mv.visitAnnotation("Lnet/minecraftforge/fml/relauncher/SideOnly;", true);
                av.visitEnum("value", "Lnet/minecraftforge/fml/relauncher/Side;", "CLIENT");
                av.visitEnd();
            }
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 1);
            mv.visitVarInsn(ALOAD, 2);
            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/world/World", "func_180495_p", "(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;", false);
            mv.visitVarInsn(ASTORE, 6);
            mv.visitVarInsn(ALOAD, 6);
            mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/block/state/IBlockState", "func_177230_c", "()Lnet/minecraft/block/Block;", true);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, className, "field_150939_a", "Lnet/minecraft/block/Block;");
            Label label0 = new Label();
            mv.visitJumpInsn(IF_ACMPNE, label0);
            mv.visitVarInsn(ALOAD, 6);
            mv.visitFieldInsn(GETSTATIC, slabBlockClassName, "TYPE", "Lnet/minecraft/block/properties/PropertyEnum;");
            mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/block/state/IBlockState", "func_177229_b", "(Lnet/minecraft/block/properties/IProperty;)Ljava/lang/Comparable;", true);
            mv.visitTypeInsn(CHECKCAST, slabTypeClassName);
            mv.visitVarInsn(ASTORE, 7);
            mv.visitVarInsn(ALOAD, 3);
            mv.visitFieldInsn(GETSTATIC, "net/minecraft/util/EnumFacing", "UP", "Lnet/minecraft/util/EnumFacing;");
            Label label1 = new Label();
            mv.visitJumpInsn(IF_ACMPNE, label1);
            mv.visitVarInsn(ALOAD, 7);
            mv.visitFieldInsn(GETSTATIC, slabTypeClassName, "BOTTOM", slabTypeDescriptor);
            Label label2 = new Label();
            mv.visitJumpInsn(IF_ACMPEQ, label2);
            mv.visitLabel(label1);
            mv.visitVarInsn(ALOAD, 3);
            mv.visitFieldInsn(GETSTATIC, "net/minecraft/util/EnumFacing", "DOWN", "Lnet/minecraft/util/EnumFacing;");
            mv.visitJumpInsn(IF_ACMPNE, label0);
            mv.visitVarInsn(ALOAD, 7);
            mv.visitFieldInsn(GETSTATIC, slabTypeClassName, "TOP", slabTypeDescriptor);
            mv.visitJumpInsn(IF_ACMPNE, label0);
            mv.visitLabel(label2);
            mv.visitInsn(ICONST_1);
            mv.visitInsn(IRETURN);
            mv.visitLabel(label0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitVarInsn(ALOAD, 2);
            mv.visitVarInsn(ALOAD, 3);
            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/util/math/BlockPos", "func_177972_a", "(Lnet/minecraft/util/EnumFacing;)Lnet/minecraft/util/math/BlockPos;", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/world/World", "func_180495_p", "(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;", false);
            mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/block/state/IBlockState", "func_177230_c", "()Lnet/minecraft/block/Block;", true);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, className, "field_150939_a", "Lnet/minecraft/block/Block;");
            Label label3 = new Label();
            mv.visitJumpInsn(IF_ACMPEQ, label3);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitVarInsn(ALOAD, 2);
            mv.visitVarInsn(ALOAD, 3);
            mv.visitVarInsn(ALOAD, 4);
            mv.visitVarInsn(ALOAD, 5);
            mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/item/ItemBlock", "func_179222_a", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/item/ItemStack;)Z", false);
            Label label4 = new Label();
            mv.visitJumpInsn(IFEQ, label4);
            mv.visitLabel(label3);
            mv.visitInsn(ICONST_1);
            Label label5 = new Label();
            mv.visitJumpInsn(GOTO, label5);
            mv.visitLabel(label4);
            mv.visitInsn(ICONST_0);
            mv.visitLabel(label5);
            mv.visitInsn(IRETURN);
            mv.visitMaxs(6, 8);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PRIVATE, "tryPlace", "(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 3);
            mv.visitVarInsn(ALOAD, 4);
            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/world/World", "func_180495_p", "(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;", false);
            mv.visitVarInsn(ASTORE, 5);
            mv.visitVarInsn(ALOAD, 5);
            mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/block/state/IBlockState", "func_177230_c", "()Lnet/minecraft/block/Block;", true);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, className, "field_150939_a", "Lnet/minecraft/block/Block;");
            Label label0 = new Label();
            mv.visitJumpInsn(IF_ACMPNE, label0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, className, "makeState", "()Lnet/minecraft/block/state/IBlockState;", false);
            mv.visitVarInsn(ASTORE, 6);
            mv.visitVarInsn(ALOAD, 6);
            mv.visitVarInsn(ALOAD, 3);
            mv.visitVarInsn(ALOAD, 4);
            mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/block/state/IBlockState", "func_185890_d", "(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/util/math/AxisAlignedBB;", true);
            mv.visitVarInsn(ASTORE, 7);
            mv.visitVarInsn(ALOAD, 7);
            mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/Block", "field_185506_k", "Lnet/minecraft/util/math/AxisAlignedBB;");
            Label label1 = new Label();
            mv.visitJumpInsn(IF_ACMPEQ, label1);
            mv.visitVarInsn(ALOAD, 3);
            mv.visitVarInsn(ALOAD, 7);
            mv.visitVarInsn(ALOAD, 4);
            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/util/math/AxisAlignedBB", "func_186670_a", "(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/util/math/AxisAlignedBB;", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/world/World", "func_72855_b", "(Lnet/minecraft/util/math/AxisAlignedBB;)Z", false);
            mv.visitJumpInsn(IFEQ, label1);
            mv.visitVarInsn(ALOAD, 3);
            mv.visitVarInsn(ALOAD, 4);
            mv.visitVarInsn(ALOAD, 6);
            mv.visitIntInsn(BIPUSH, 11);
            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/world/World", "func_180501_a", "(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;I)Z", false);
            mv.visitJumpInsn(IFEQ, label1);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, className, "field_150939_a", "Lnet/minecraft/block/Block;");
            mv.visitVarInsn(ALOAD, 6);
            mv.visitVarInsn(ALOAD, 3);
            mv.visitVarInsn(ALOAD, 4);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/Block", "getSoundType", "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/Entity;)Lnet/minecraft/block/SoundType;", false);
            mv.visitVarInsn(ASTORE, 8);
            mv.visitVarInsn(ALOAD, 3);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitVarInsn(ALOAD, 4);
            mv.visitVarInsn(ALOAD, 8);
            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/SoundType", "func_185841_e", "()Lnet/minecraft/util/SoundEvent;", false);
            mv.visitFieldInsn(GETSTATIC, "net/minecraft/util/SoundCategory", "BLOCKS", "Lnet/minecraft/util/SoundCategory;");
            mv.visitVarInsn(ALOAD, 8);
            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/SoundType", "func_185843_a", "()F", false);
            mv.visitInsn(FCONST_1);
            mv.visitInsn(FADD);
            mv.visitInsn(FCONST_2);
            mv.visitInsn(FDIV);
            mv.visitVarInsn(ALOAD, 8);
            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/SoundType", "func_185847_b", "()F", false);
            mv.visitLdcInsn(Float.valueOf("0.8"));
            mv.visitInsn(FMUL);
            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/world/World", "func_184133_a", "(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/SoundEvent;Lnet/minecraft/util/SoundCategory;FF)V", false);
            mv.visitVarInsn(ALOAD, 2);
            mv.visitInsn(ICONST_1);
            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "func_190918_g", "(I)V", false);
            mv.visitLabel(label1);
            mv.visitInsn(ICONST_1);
            mv.visitInsn(IRETURN);
            mv.visitLabel(label0);
            mv.visitInsn(ICONST_0);
            mv.visitInsn(IRETURN);
            mv.visitMaxs(8, 9);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PRIVATE, "makeState", "()Lnet/minecraft/block/state/IBlockState;", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, className, "field_150939_a", "Lnet/minecraft/block/Block;");
            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/Block", "func_176223_P", "()Lnet/minecraft/block/state/IBlockState;", false);
            mv.visitFieldInsn(GETSTATIC, slabBlockClassName, "TYPE", "Lnet/minecraft/block/properties/PropertyEnum;");
            mv.visitFieldInsn(GETSTATIC, slabTypeClassName, "DOUBLE", slabTypeDescriptor);
            mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/block/state/IBlockState", "func_177226_a", "(Lnet/minecraft/block/properties/IProperty;Ljava/lang/Comparable;)Lnet/minecraft/block/state/IBlockState;", true);
            mv.visitInsn(ARETURN);
            mv.visitMaxs(3, 1);
            mv.visitEnd();
        }
        cw.visitEnd();
    }
}
