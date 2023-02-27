package com.github.lochnessdragon.locks_and_keys.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.github.lochnessdragon.locks_and_keys.LocksAndKeys;
import com.github.lochnessdragon.locks_and_keys.registry.ModBlocks;
import com.google.common.base.Predicates;

import net.minecraft.block.Blocks;
import net.minecraft.block.EndPortalFrameBlock;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.item.EnderEyeItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.predicate.block.BlockStatePredicate;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Direction;

@Mixin(EnderEyeItem.class)
public abstract class EnderEyeItemMixin {
	
	private static BlockPattern BLOCKED_FRAME = BlockPatternBuilder.start()
												.aisle("?vvv?", ">???<", ">?L?<", ">???<", "?^^^?")
												.where('?', CachedBlockPosition.matchesBlockState(BlockStatePredicate.ANY))
												.where('^', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Blocks.END_PORTAL_FRAME)
														.with(EndPortalFrameBlock.EYE, Predicates.equalTo(true))
														.with(EndPortalFrameBlock.FACING, Predicates.equalTo(Direction.SOUTH))))
												.where('>', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Blocks.END_PORTAL_FRAME)
														.with(EndPortalFrameBlock.EYE, Predicates.equalTo(true))
														.with(EndPortalFrameBlock.FACING, Predicates.equalTo(Direction.WEST))))
												.where('v', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Blocks.END_PORTAL_FRAME)
														.with(EndPortalFrameBlock.EYE, Predicates.equalTo(true))
														.with(EndPortalFrameBlock.FACING, Predicates.equalTo(Direction.NORTH))))
												.where('<', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Blocks.END_PORTAL_FRAME)
														.with(EndPortalFrameBlock.EYE, Predicates.equalTo(true))
														.with(EndPortalFrameBlock.FACING, Predicates.equalTo(Direction.EAST))))
												.where('L', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(ModBlocks.END_PORTAL_LOCK))).build();
	
	@Inject(method = "useOnBlock(Lnet/minecraft/item/ItemUsageContext;)Lnet/minecraft/util/ActionResult;", at = @At(value = "INVOKE", target="net/minecraft/block/EndPortalFrameBlock.getCompletedFramePattern()Lnet/minecraft/block/pattern/BlockPattern;"), cancellable = true)//, at = @At(value = "INVOKE", target="Lnet/minecraft/block/pattern/BlockPattern;searchAround(Lnet/minecraft/world/WorldView;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/pattern/BlockPattern/Result;"), cancellable = true)
	public void preventUseIfLocked(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
		BlockPattern.Result isBlockedFrame = BLOCKED_FRAME.searchAround(context.getWorld(), context.getBlockPos());
		if(isBlockedFrame != null) {
			// we've found a match, block creating a portal and send a message to the player
			LocksAndKeys.LOGGER.info("Blocking a portal summon at pos: {}", context.getBlockPos());
			context.getPlayer().sendMessage(Text.translatable("block.locks-and-keys.end_portal_lock.locked"), true);
			context.getPlayer().playSound(SoundEvents.ENTITY_GHAST_WARN, 1, 1);
			cir.setReturnValue(ActionResult.CONSUME);
		}
	}
}
