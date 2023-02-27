package com.github.lochnessdragon.locks_and_keys.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.block.BlockState;
import net.minecraft.structure.StructurePiece;
import net.minecraft.util.math.BlockBox;
import net.minecraft.world.StructureWorldAccess;

@Mixin(StructurePiece.class)
public abstract class StructurePieceInvoker {
	@Invoker("addBlock")
	public abstract void invokeAddBlock(StructureWorldAccess world, BlockState block, int x, int y, int z, BlockBox box);
}
