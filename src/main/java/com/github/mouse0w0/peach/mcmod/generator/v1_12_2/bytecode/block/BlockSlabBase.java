package com.github.mouse0w0.peach.mcmod.generator.v1_12_2.bytecode.block;

import com.github.mouse0w0.peach.mcmod.generator.util.ASMUtils;
import com.github.mouse0w0.peach.mcmod.generator.v1_12_2.bytecode.ClassGenerator;
import org.objectweb.asm.*;

import static org.objectweb.asm.Opcodes.*;

public class BlockSlabBase extends ClassGenerator {
    public BlockSlabBase(String className, String slabTypeClassName) {
        super(className);

        String slabTypeDescriptor = ASMUtils.getDescriptor(slabTypeClassName);

        FieldVisitor fv;
        MethodVisitor mv;
        AnnotationVisitor av;

        cw.visit(V1_8, ACC_PUBLIC | ACC_SUPER, className, null, "net/minecraft/block/Block", null);

        ASMUtils.visitSource(cw);

        {
            fv = cw.visitField(ACC_PUBLIC | ACC_FINAL | ACC_STATIC, "TYPE", "Lnet/minecraft/block/properties/PropertyEnum;", "Lnet/minecraft/block/properties/PropertyEnum<" + slabTypeDescriptor + ">;", null);
            fv.visitEnd();
        }
        {
            fv = cw.visitField(ACC_PUBLIC | ACC_FINAL | ACC_STATIC, "AABB_BOTTOM_HALF", "Lnet/minecraft/util/math/AxisAlignedBB;", null, null);
            fv.visitEnd();
        }
        {
            fv = cw.visitField(ACC_PUBLIC | ACC_FINAL | ACC_STATIC, "AABB_TOP_HALF", "Lnet/minecraft/util/math/AxisAlignedBB;", null, null);
            fv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(Lnet/minecraft/block/material/Material;Lnet/minecraft/block/material/MapColor;)V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitVarInsn(ALOAD, 2);
            mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/block/Block", "<init>", "(Lnet/minecraft/block/material/Material;Lnet/minecraft/block/material/MapColor;)V", false);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, className, "field_176227_L", "Lnet/minecraft/block/state/BlockStateContainer;");
            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/state/BlockStateContainer", "func_177621_b", "()Lnet/minecraft/block/state/IBlockState;", false);
            mv.visitFieldInsn(GETSTATIC, className, "TYPE", "Lnet/minecraft/block/properties/PropertyEnum;");
            mv.visitFieldInsn(GETSTATIC, slabTypeClassName, "BOTTOM", slabTypeDescriptor);
            mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/block/state/IBlockState", "func_177226_a", "(Lnet/minecraft/block/properties/IProperty;Ljava/lang/Comparable;)Lnet/minecraft/block/state/IBlockState;", true);
            mv.visitMethodInsn(INVOKEVIRTUAL, className, "func_180632_j", "(Lnet/minecraft/block/state/IBlockState;)V", false);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitIntInsn(SIPUSH, 255);
            mv.visitFieldInsn(PUTFIELD, className, "field_149786_r", "I");
            mv.visitInsn(RETURN);
            mv.visitMaxs(4, 3);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PROTECTED, "func_180661_e", "()Lnet/minecraft/block/state/BlockStateContainer;", null, null);
            mv.visitCode();
            mv.visitTypeInsn(NEW, "net/minecraft/block/state/BlockStateContainer");
            mv.visitInsn(DUP);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitInsn(ICONST_1);
            mv.visitTypeInsn(ANEWARRAY, "net/minecraft/block/properties/IProperty");
            mv.visitInsn(DUP);
            mv.visitInsn(ICONST_0);
            mv.visitFieldInsn(GETSTATIC, className, "TYPE", "Lnet/minecraft/block/properties/PropertyEnum;");
            mv.visitInsn(AASTORE);
            mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/block/state/BlockStateContainer", "<init>", "(Lnet/minecraft/block/Block;[Lnet/minecraft/block/properties/IProperty;)V", false);
            mv.visitInsn(ARETURN);
            mv.visitMaxs(7, 1);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "func_176203_a", "(I)Lnet/minecraft/block/state/IBlockState;", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKEVIRTUAL, className, "func_176223_P", "()Lnet/minecraft/block/state/IBlockState;", false);
            mv.visitFieldInsn(GETSTATIC, className, "TYPE", "Lnet/minecraft/block/properties/PropertyEnum;");
            mv.visitVarInsn(ILOAD, 1);
            mv.visitMethodInsn(INVOKESTATIC, slabTypeClassName, "byMetadata", "(I)" + slabTypeDescriptor, false);
            mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/block/state/IBlockState", "func_177226_a", "(Lnet/minecraft/block/properties/IProperty;Ljava/lang/Comparable;)Lnet/minecraft/block/state/IBlockState;", true);
            mv.visitInsn(ARETURN);
            mv.visitMaxs(3, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "func_176201_c", "(Lnet/minecraft/block/state/IBlockState;)I", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 1);
            mv.visitFieldInsn(GETSTATIC, className, "TYPE", "Lnet/minecraft/block/properties/PropertyEnum;");
            mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/block/state/IBlockState", "func_177229_b", "(Lnet/minecraft/block/properties/IProperty;)Ljava/lang/Comparable;", true);
            mv.visitTypeInsn(CHECKCAST, slabTypeClassName);
            mv.visitMethodInsn(INVOKEVIRTUAL, slabTypeClassName, "ordinal", "()I", false);
            mv.visitInsn(IRETURN);
            mv.visitMaxs(2, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "canSilkHarvest", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/entity/player/EntityPlayer;)Z", null, null);
            mv.visitCode();
            mv.visitInsn(ICONST_0);
            mv.visitInsn(IRETURN);
            mv.visitMaxs(1, 5);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "func_185496_a", "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/util/math/AxisAlignedBB;", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 1);
            mv.visitFieldInsn(GETSTATIC, className, "TYPE", "Lnet/minecraft/block/properties/PropertyEnum;");
            mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/block/state/IBlockState", "func_177229_b", "(Lnet/minecraft/block/properties/IProperty;)Ljava/lang/Comparable;", true);
            mv.visitTypeInsn(CHECKCAST, slabTypeClassName);
            mv.visitVarInsn(ASTORE, 4);
            mv.visitVarInsn(ALOAD, 4);
            mv.visitFieldInsn(GETSTATIC, slabTypeClassName, "DOUBLE", slabTypeDescriptor);
            Label label0 = new Label();
            mv.visitJumpInsn(IF_ACMPNE, label0);
            mv.visitFieldInsn(GETSTATIC, className, "field_185505_j", "Lnet/minecraft/util/math/AxisAlignedBB;");
            mv.visitInsn(ARETURN);
            mv.visitLabel(label0);
            mv.visitVarInsn(ALOAD, 4);
            mv.visitFieldInsn(GETSTATIC, slabTypeClassName, "TOP", slabTypeDescriptor);
            Label label1 = new Label();
            mv.visitJumpInsn(IF_ACMPNE, label1);
            mv.visitFieldInsn(GETSTATIC, className, "AABB_TOP_HALF", "Lnet/minecraft/util/math/AxisAlignedBB;");
            mv.visitInsn(ARETURN);
            mv.visitLabel(label1);
            mv.visitFieldInsn(GETSTATIC, className, "AABB_BOTTOM_HALF", "Lnet/minecraft/util/math/AxisAlignedBB;");
            mv.visitInsn(ARETURN);
            mv.visitMaxs(2, 5);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "func_185481_k", "(Lnet/minecraft/block/state/IBlockState;)Z", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKESTATIC, className, "isDouble", "(Lnet/minecraft/block/state/IBlockState;)Z", false);
            Label label0 = new Label();
            mv.visitJumpInsn(IFNE, label0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitFieldInsn(GETSTATIC, className, "TYPE", "Lnet/minecraft/block/properties/PropertyEnum;");
            mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/block/state/IBlockState", "func_177229_b", "(Lnet/minecraft/block/properties/IProperty;)Ljava/lang/Comparable;", true);
            mv.visitFieldInsn(GETSTATIC, slabTypeClassName, "TOP", slabTypeDescriptor);
            Label label1 = new Label();
            mv.visitJumpInsn(IF_ACMPNE, label1);
            mv.visitLabel(label0);
            mv.visitInsn(ICONST_1);
            Label label2 = new Label();
            mv.visitJumpInsn(GOTO, label2);
            mv.visitLabel(label1);
            mv.visitInsn(ICONST_0);
            mv.visitLabel(label2);
            mv.visitInsn(IRETURN);
            mv.visitMaxs(2, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "func_193383_a", "(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;)Lnet/minecraft/block/state/BlockFaceShape;", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 2);
            mv.visitMethodInsn(INVOKESTATIC, className, "isDouble", "(Lnet/minecraft/block/state/IBlockState;)Z", false);
            Label label0 = new Label();
            mv.visitJumpInsn(IFEQ, label0);
            mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/state/BlockFaceShape", "SOLID", "Lnet/minecraft/block/state/BlockFaceShape;");
            mv.visitInsn(ARETURN);
            mv.visitLabel(label0);
            mv.visitVarInsn(ALOAD, 4);
            mv.visitFieldInsn(GETSTATIC, "net/minecraft/util/EnumFacing", "UP", "Lnet/minecraft/util/EnumFacing;");
            Label label1 = new Label();
            mv.visitJumpInsn(IF_ACMPNE, label1);
            mv.visitVarInsn(ALOAD, 2);
            mv.visitFieldInsn(GETSTATIC, className, "TYPE", "Lnet/minecraft/block/properties/PropertyEnum;");
            mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/block/state/IBlockState", "func_177229_b", "(Lnet/minecraft/block/properties/IProperty;)Ljava/lang/Comparable;", true);
            mv.visitFieldInsn(GETSTATIC, slabTypeClassName, "TOP", slabTypeDescriptor);
            mv.visitJumpInsn(IF_ACMPNE, label1);
            mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/state/BlockFaceShape", "SOLID", "Lnet/minecraft/block/state/BlockFaceShape;");
            mv.visitInsn(ARETURN);
            mv.visitLabel(label1);
            mv.visitVarInsn(ALOAD, 4);
            mv.visitFieldInsn(GETSTATIC, "net/minecraft/util/EnumFacing", "DOWN", "Lnet/minecraft/util/EnumFacing;");
            Label label2 = new Label();
            mv.visitJumpInsn(IF_ACMPNE, label2);
            mv.visitVarInsn(ALOAD, 2);
            mv.visitFieldInsn(GETSTATIC, className, "TYPE", "Lnet/minecraft/block/properties/PropertyEnum;");
            mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/block/state/IBlockState", "func_177229_b", "(Lnet/minecraft/block/properties/IProperty;)Ljava/lang/Comparable;", true);
            mv.visitFieldInsn(GETSTATIC, slabTypeClassName, "BOTTOM", slabTypeDescriptor);
            mv.visitJumpInsn(IF_ACMPNE, label2);
            mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/state/BlockFaceShape", "SOLID", "Lnet/minecraft/block/state/BlockFaceShape;");
            mv.visitInsn(ARETURN);
            mv.visitLabel(label2);
            mv.visitFieldInsn(GETSTATIC, "net/minecraft/block/state/BlockFaceShape", "UNDEFINED", "Lnet/minecraft/block/state/BlockFaceShape;");
            mv.visitInsn(ARETURN);
            mv.visitMaxs(2, 5);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "doesSideBlockRendering", "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;)Z", null, null);
            mv.visitCode();
            mv.visitFieldInsn(GETSTATIC, "net/minecraftforge/common/ForgeModContainer", "disableStairSlabCulling", "Z");
            Label label0 = new Label();
            mv.visitJumpInsn(IFEQ, label0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitVarInsn(ALOAD, 2);
            mv.visitVarInsn(ALOAD, 3);
            mv.visitVarInsn(ALOAD, 4);
            mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/block/Block", "doesSideBlockRendering", "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;)Z", false);
            mv.visitInsn(IRETURN);
            mv.visitLabel(label0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/block/state/IBlockState", "func_185914_p", "()Z", true);
            Label label1 = new Label();
            mv.visitJumpInsn(IFEQ, label1);
            mv.visitInsn(ICONST_1);
            mv.visitInsn(IRETURN);
            mv.visitLabel(label1);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitFieldInsn(GETSTATIC, className, "TYPE", "Lnet/minecraft/block/properties/PropertyEnum;");
            mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/block/state/IBlockState", "func_177229_b", "(Lnet/minecraft/block/properties/IProperty;)Ljava/lang/Comparable;", true);
            mv.visitTypeInsn(CHECKCAST, slabTypeClassName);
            mv.visitVarInsn(ASTORE, 5);
            mv.visitVarInsn(ALOAD, 5);
            mv.visitFieldInsn(GETSTATIC, slabTypeClassName, "TOP", slabTypeDescriptor);
            Label label2 = new Label();
            mv.visitJumpInsn(IF_ACMPNE, label2);
            mv.visitVarInsn(ALOAD, 4);
            mv.visitFieldInsn(GETSTATIC, "net/minecraft/util/EnumFacing", "UP", "Lnet/minecraft/util/EnumFacing;");
            Label label3 = new Label();
            mv.visitJumpInsn(IF_ACMPEQ, label3);
            mv.visitLabel(label2);
            mv.visitVarInsn(ALOAD, 5);
            mv.visitFieldInsn(GETSTATIC, slabTypeClassName, "BOTTOM", slabTypeDescriptor);
            Label label4 = new Label();
            mv.visitJumpInsn(IF_ACMPNE, label4);
            mv.visitVarInsn(ALOAD, 4);
            mv.visitFieldInsn(GETSTATIC, "net/minecraft/util/EnumFacing", "DOWN", "Lnet/minecraft/util/EnumFacing;");
            mv.visitJumpInsn(IF_ACMPNE, label4);
            mv.visitLabel(label3);
            mv.visitInsn(ICONST_1);
            Label label5 = new Label();
            mv.visitJumpInsn(GOTO, label5);
            mv.visitLabel(label4);
            mv.visitInsn(ICONST_0);
            mv.visitLabel(label5);
            mv.visitInsn(IRETURN);
            mv.visitMaxs(5, 6);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "func_180642_a", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;FFFILnet/minecraft/entity/EntityLivingBase;)Lnet/minecraft/block/state/IBlockState;", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKEVIRTUAL, className, "func_176223_P", "()Lnet/minecraft/block/state/IBlockState;", false);
            mv.visitVarInsn(ASTORE, 9);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitVarInsn(ALOAD, 2);
            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/world/World", "func_180495_p", "(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;", false);
            mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/block/state/IBlockState", "func_177230_c", "()Lnet/minecraft/block/Block;", true);
            mv.visitVarInsn(ALOAD, 0);
            Label label0 = new Label();
            mv.visitJumpInsn(IF_ACMPNE, label0);
            mv.visitVarInsn(ALOAD, 9);
            mv.visitFieldInsn(GETSTATIC, className, "TYPE", "Lnet/minecraft/block/properties/PropertyEnum;");
            mv.visitFieldInsn(GETSTATIC, slabTypeClassName, "DOUBLE", slabTypeDescriptor);
            mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/block/state/IBlockState", "func_177226_a", "(Lnet/minecraft/block/properties/IProperty;Ljava/lang/Comparable;)Lnet/minecraft/block/state/IBlockState;", true);
            mv.visitInsn(ARETURN);
            mv.visitLabel(label0);
            mv.visitVarInsn(ALOAD, 3);
            mv.visitFieldInsn(GETSTATIC, "net/minecraft/util/EnumFacing", "DOWN", "Lnet/minecraft/util/EnumFacing;");
            Label label1 = new Label();
            mv.visitJumpInsn(IF_ACMPEQ, label1);
            mv.visitVarInsn(ALOAD, 3);
            mv.visitFieldInsn(GETSTATIC, "net/minecraft/util/EnumFacing", "UP", "Lnet/minecraft/util/EnumFacing;");
            Label label2 = new Label();
            mv.visitJumpInsn(IF_ACMPEQ, label2);
            mv.visitVarInsn(FLOAD, 5);
            mv.visitInsn(F2D);
            mv.visitLdcInsn(new Double("0.5"));
            mv.visitInsn(DCMPG);
            mv.visitJumpInsn(IFGT, label1);
            mv.visitLabel(label2);
            mv.visitVarInsn(ALOAD, 9);
            Label label3 = new Label();
            mv.visitJumpInsn(GOTO, label3);
            mv.visitLabel(label1);
            mv.visitVarInsn(ALOAD, 9);
            mv.visitFieldInsn(GETSTATIC, className, "TYPE", "Lnet/minecraft/block/properties/PropertyEnum;");
            mv.visitFieldInsn(GETSTATIC, slabTypeClassName, "TOP", slabTypeDescriptor);
            mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/block/state/IBlockState", "func_177226_a", "(Lnet/minecraft/block/properties/IProperty;Ljava/lang/Comparable;)Lnet/minecraft/block/state/IBlockState;", true);
            mv.visitLabel(label3);
            mv.visitInsn(ARETURN);
            mv.visitMaxs(4, 10);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "quantityDropped", "(Lnet/minecraft/block/state/IBlockState;ILjava/util/Random;)I", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKESTATIC, className, "isDouble", "(Lnet/minecraft/block/state/IBlockState;)Z", false);
            Label label0 = new Label();
            mv.visitJumpInsn(IFEQ, label0);
            mv.visitInsn(ICONST_2);
            Label label1 = new Label();
            mv.visitJumpInsn(GOTO, label1);
            mv.visitLabel(label0);
            mv.visitInsn(ICONST_1);
            mv.visitLabel(label1);
            mv.visitInsn(IRETURN);
            mv.visitMaxs(1, 4);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "func_149662_c", "(Lnet/minecraft/block/state/IBlockState;)Z", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKESTATIC, className, "isDouble", "(Lnet/minecraft/block/state/IBlockState;)Z", false);
            mv.visitInsn(IRETURN);
            mv.visitMaxs(1, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "func_149686_d", "(Lnet/minecraft/block/state/IBlockState;)Z", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKESTATIC, className, "isDouble", "(Lnet/minecraft/block/state/IBlockState;)Z", false);
            mv.visitInsn(IRETURN);
            mv.visitMaxs(1, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "func_176225_a", "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;)Z", null, null);
            {
                av = mv.visitAnnotation("Lnet/minecraftforge/fml/relauncher/SideOnly;", true);
                av.visitEnum("value", "Lnet/minecraftforge/fml/relauncher/Side;", "CLIENT");
                av.visitEnd();
            }
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKESTATIC, className, "isDouble", "(Lnet/minecraft/block/state/IBlockState;)Z", false);
            Label label0 = new Label();
            mv.visitJumpInsn(IFEQ, label0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitVarInsn(ALOAD, 2);
            mv.visitVarInsn(ALOAD, 3);
            mv.visitVarInsn(ALOAD, 4);
            mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/block/Block", "func_176225_a", "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;)Z", false);
            mv.visitInsn(IRETURN);
            mv.visitLabel(label0);
            mv.visitVarInsn(ALOAD, 4);
            mv.visitFieldInsn(GETSTATIC, "net/minecraft/util/EnumFacing", "UP", "Lnet/minecraft/util/EnumFacing;");
            Label label1 = new Label();
            mv.visitJumpInsn(IF_ACMPEQ, label1);
            mv.visitVarInsn(ALOAD, 4);
            mv.visitFieldInsn(GETSTATIC, "net/minecraft/util/EnumFacing", "DOWN", "Lnet/minecraft/util/EnumFacing;");
            mv.visitJumpInsn(IF_ACMPEQ, label1);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitVarInsn(ALOAD, 2);
            mv.visitVarInsn(ALOAD, 3);
            mv.visitVarInsn(ALOAD, 4);
            mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/block/Block", "func_176225_a", "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;)Z", false);
            mv.visitJumpInsn(IFNE, label1);
            mv.visitInsn(ICONST_0);
            mv.visitInsn(IRETURN);
            mv.visitLabel(label1);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitVarInsn(ALOAD, 2);
            mv.visitVarInsn(ALOAD, 3);
            mv.visitVarInsn(ALOAD, 4);
            mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/block/Block", "func_176225_a", "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;)Z", false);
            mv.visitInsn(IRETURN);
            mv.visitMaxs(5, 5);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC | ACC_STATIC, "isDouble", "(Lnet/minecraft/block/state/IBlockState;)Z", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETSTATIC, className, "TYPE", "Lnet/minecraft/block/properties/PropertyEnum;");
            mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/block/state/IBlockState", "func_177229_b", "(Lnet/minecraft/block/properties/IProperty;)Ljava/lang/Comparable;", true);
            mv.visitFieldInsn(GETSTATIC, slabTypeClassName, "DOUBLE", slabTypeDescriptor);
            Label label0 = new Label();
            mv.visitJumpInsn(IF_ACMPNE, label0);
            mv.visitInsn(ICONST_1);
            Label label1 = new Label();
            mv.visitJumpInsn(GOTO, label1);
            mv.visitLabel(label0);
            mv.visitInsn(ICONST_0);
            mv.visitLabel(label1);
            mv.visitInsn(IRETURN);
            mv.visitMaxs(2, 1);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null);
            mv.visitCode();
            mv.visitLdcInsn("type");
            mv.visitLdcInsn(Type.getType(slabTypeDescriptor));
            mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/block/properties/PropertyEnum", "func_177709_a", "(Ljava/lang/String;Ljava/lang/Class;)Lnet/minecraft/block/properties/PropertyEnum;", false);
            mv.visitFieldInsn(PUTSTATIC, className, "TYPE", "Lnet/minecraft/block/properties/PropertyEnum;");
            mv.visitTypeInsn(NEW, "net/minecraft/util/math/AxisAlignedBB");
            mv.visitInsn(DUP);
            mv.visitInsn(DCONST_0);
            mv.visitInsn(DCONST_0);
            mv.visitInsn(DCONST_0);
            mv.visitInsn(DCONST_1);
            mv.visitLdcInsn(new Double("0.5"));
            mv.visitInsn(DCONST_1);
            mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/util/math/AxisAlignedBB", "<init>", "(DDDDDD)V", false);
            mv.visitFieldInsn(PUTSTATIC, className, "AABB_BOTTOM_HALF", "Lnet/minecraft/util/math/AxisAlignedBB;");
            mv.visitTypeInsn(NEW, "net/minecraft/util/math/AxisAlignedBB");
            mv.visitInsn(DUP);
            mv.visitInsn(DCONST_0);
            mv.visitLdcInsn(new Double("0.5"));
            mv.visitInsn(DCONST_0);
            mv.visitInsn(DCONST_1);
            mv.visitInsn(DCONST_1);
            mv.visitInsn(DCONST_1);
            mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/util/math/AxisAlignedBB", "<init>", "(DDDDDD)V", false);
            mv.visitFieldInsn(PUTSTATIC, className, "AABB_TOP_HALF", "Lnet/minecraft/util/math/AxisAlignedBB;");
            mv.visitInsn(RETURN);
            mv.visitMaxs(14, 0);
            mv.visitEnd();
        }
        cw.visitEnd();
    }
}
