package com.github.lochnessdragon.locks_and_keys.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.github.lochnessdragon.locks_and_keys.registry.ModBlocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChainBlock;
import net.minecraft.structure.StrongholdGenerator;
import net.minecraft.structure.StructurePiece;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;

@Mixin(StrongholdGenerator.PortalRoom.class)
public abstract class PortalRoomMixin {
	
	@Inject(method = "generate(Lnet/minecraft/world/StructureWorldAccess;Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Lnet/minecraft/util/random/Random;Lnet/minecraft/util/math/BlockBox;Lnet/minecraft/util/math/ChunkPos;Lnet/minecraft/util/math/BlockPos;)V", at = @At("TAIL"))
	public void spawnLock(final StructureWorldAccess world, final StructureAccessor structureAccessor, final ChunkGenerator chunkGenerator, final Random random, final BlockBox chunkBox, final ChunkPos chunkPos, final BlockPos pivot, final CallbackInfo info) {
		BlockState chainBlockX = Blocks.CHAIN.getDefaultState().with(ChainBlock.AXIS, Axis.X);
		BlockState chainBlockZ = Blocks.CHAIN.getDefaultState().with(ChainBlock.AXIS, Axis.Z);
		BlockState endPortalLock = ModBlocks.END_PORTAL_LOCK.getDefaultState();
		
		StructurePieceInvoker structurePiece = (StructurePieceInvoker) (Object) this;
		structurePiece.invokeAddBlock(world, chainBlockZ, 5, 3, 9, chunkBox);
		structurePiece.invokeAddBlock(world, chainBlockZ, 5, 3, 11, chunkBox);
		
		structurePiece.invokeAddBlock(world, chainBlockX, 4, 3, 10, chunkBox);
		structurePiece.invokeAddBlock(world, chainBlockX, 6, 3, 10, chunkBox);
		
		structurePiece.invokeAddBlock(world, endPortalLock, 5, 3, 10, chunkBox);
	}
}
