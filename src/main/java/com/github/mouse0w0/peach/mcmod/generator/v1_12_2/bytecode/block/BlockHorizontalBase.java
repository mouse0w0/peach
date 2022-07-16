package com.github.mouse0w0.peach.mcmod.generator.v1_12_2.bytecode.block;

import com.github.mouse0w0.peach.mcmod.generator.util.ASMUtils;
import com.github.mouse0w0.peach.mcmod.generator.v1_12_2.bytecode.ClassGenerator;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

public class BlockHorizontalBase extends ClassGenerator {
    public BlockHorizontalBase(String className) {
        super(className);

        MethodVisitor mv;

        cw.visit(V1_8, ACC_PUBLIC | ACC_SUPER, className, null, "net/minecraft/block/BlockHorizontal", null);

        ASMUtils.visitSource(cw);

        {
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(Lnet/minecraft/block/material/Material;Lnet/minecraft/block/material/MapColor;)V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitVarInsn(ALOAD, 2);
            mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/block/BlockHorizontal", "<init>", "(Lnet/minecraft/block/material/Material;Lnet/minecraft/block/material/MapColor;)V", false);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, className, "field_176227_L", "Lnet/minecraft/block/state/BlockStateContainer;");
            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/block/state/BlockStateContainer", "func_177621_b", "()Lnet/minecraft/block/state/IBlockState;", false);
            mv.visitFieldInsn(GETSTATIC, className, "field_185512_D", "Lnet/minecraft/block/properties/PropertyDirection;");
            mv.visitFieldInsn(GETSTATIC, "net/minecraft/util/EnumFacing", "NORTH", "Lnet/minecraft/util/EnumFacing;");
            mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/block/state/IBlockState", "func_177226_a", "(Lnet/minecraft/block/properties/IProperty;Ljava/lang/Comparable;)Lnet/minecraft/block/state/IBlockState;", true);
            mv.visitMethodInsn(INVOKEVIRTUAL, className, "func_180632_j", "(Lnet/minecraft/block/state/IBlockState;)V", false);
            mv.visitInsn(RETURN);
            mv.visitMaxs(4, 3);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "func_185499_a", "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/Rotation;)Lnet/minecraft/block/state/IBlockState;", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 1);
            mv.visitFieldInsn(GETSTATIC, className, "field_185512_D", "Lnet/minecraft/block/properties/PropertyDirection;");
            mv.visitVarInsn(ALOAD, 2);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitFieldInsn(GETSTATIC, className, "field_185512_D", "Lnet/minecraft/block/properties/PropertyDirection;");
            mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/block/state/IBlockState", "func_177229_b", "(Lnet/minecraft/block/properties/IProperty;)Ljava/lang/Comparable;", true);
            mv.visitTypeInsn(CHECKCAST, "net/minecraft/util/EnumFacing");
            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/util/Rotation", "func_185831_a", "(Lnet/minecraft/util/EnumFacing;)Lnet/minecraft/util/EnumFacing;", false);
            mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/block/state/IBlockState", "func_177226_a", "(Lnet/minecraft/block/properties/IProperty;Ljava/lang/Comparable;)Lnet/minecraft/block/state/IBlockState;", true);
            mv.visitInsn(ARETURN);
            mv.visitMaxs(5, 3);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "func_185471_a", "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/Mirror;)Lnet/minecraft/block/state/IBlockState;", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 1);
            mv.visitVarInsn(ALOAD, 2);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitFieldInsn(GETSTATIC, className, "field_185512_D", "Lnet/minecraft/block/properties/PropertyDirection;");
            mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/block/state/IBlockState", "func_177229_b", "(Lnet/minecraft/block/properties/IProperty;)Ljava/lang/Comparable;", true);
            mv.visitTypeInsn(CHECKCAST, "net/minecraft/util/EnumFacing");
            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/util/Mirror", "func_185800_a", "(Lnet/minecraft/util/EnumFacing;)Lnet/minecraft/util/Rotation;", false);
            mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/block/state/IBlockState", "func_185907_a", "(Lnet/minecraft/util/Rotation;)Lnet/minecraft/block/state/IBlockState;", true);
            mv.visitInsn(ARETURN);
            mv.visitMaxs(4, 3);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "func_180642_a", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;FFFILnet/minecraft/entity/EntityLivingBase;)Lnet/minecraft/block/state/IBlockState;", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKEVIRTUAL, className, "func_176223_P", "()Lnet/minecraft/block/state/IBlockState;", false);
            mv.visitFieldInsn(GETSTATIC, className, "field_185512_D", "Lnet/minecraft/block/properties/PropertyDirection;");
            mv.visitVarInsn(ALOAD, 8);
            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/entity/EntityLivingBase", "func_174811_aO", "()Lnet/minecraft/util/EnumFacing;", false);
            mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/block/state/IBlockState", "func_177226_a", "(Lnet/minecraft/block/properties/IProperty;Ljava/lang/Comparable;)Lnet/minecraft/block/state/IBlockState;", true);
            mv.visitInsn(ARETURN);
            mv.visitMaxs(3, 9);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "func_176203_a", "(I)Lnet/minecraft/block/state/IBlockState;", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKEVIRTUAL, className, "func_176223_P", "()Lnet/minecraft/block/state/IBlockState;", false);
            mv.visitFieldInsn(GETSTATIC, className, "field_185512_D", "Lnet/minecraft/block/properties/PropertyDirection;");
            mv.visitVarInsn(ILOAD, 1);
            mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/util/EnumFacing", "func_176731_b", "(I)Lnet/minecraft/util/EnumFacing;", false);
            mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/block/state/IBlockState", "func_177226_a", "(Lnet/minecraft/block/properties/IProperty;Ljava/lang/Comparable;)Lnet/minecraft/block/state/IBlockState;", true);
            mv.visitInsn(ARETURN);
            mv.visitMaxs(3, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "func_176201_c", "(Lnet/minecraft/block/state/IBlockState;)I", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 1);
            mv.visitFieldInsn(GETSTATIC, className, "field_185512_D", "Lnet/minecraft/block/properties/PropertyDirection;");
            mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/block/state/IBlockState", "func_177229_b", "(Lnet/minecraft/block/properties/IProperty;)Ljava/lang/Comparable;", true);
            mv.visitTypeInsn(CHECKCAST, "net/minecraft/util/EnumFacing");
            mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/util/EnumFacing", "func_176736_b", "()I", false);
            mv.visitInsn(IRETURN);
            mv.visitMaxs(2, 2);
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
            mv.visitFieldInsn(GETSTATIC, className, "field_185512_D", "Lnet/minecraft/block/properties/PropertyDirection;");
            mv.visitInsn(AASTORE);
            mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/block/state/BlockStateContainer", "<init>", "(Lnet/minecraft/block/Block;[Lnet/minecraft/block/properties/IProperty;)V", false);
            mv.visitInsn(ARETURN);
            mv.visitMaxs(7, 1);
            mv.visitEnd();
        }
        cw.visitEnd();
    }
}
