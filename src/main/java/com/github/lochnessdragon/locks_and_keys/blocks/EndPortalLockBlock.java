package com.github.lochnessdragon.locks_and_keys.blocks;

import com.github.lochnessdragon.locks_and_keys.LocksAndKeys;
import com.github.lochnessdragon.locks_and_keys.registry.ModItems;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.EndPortalFrameBlock;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.raid.Raid;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;

public class EndPortalLockBlock extends Block {
	public static final BooleanProperty HAS_NETHER_STAR = BooleanProperty.of("has_nether_star");
	public static final BooleanProperty HAS_ELDER_GUARDIAN_EYE = BooleanProperty.of("has_elder_guardian_eye");
	public static final BooleanProperty HAS_ECHO_SHARD = BooleanProperty.of("has_echo_shard");
	public static final BooleanProperty HAS_PILLAGER_BANNER = BooleanProperty.of("has_pillager_banner");
	public static final BooleanProperty HAS_TOTEM_OF_UNDYING = BooleanProperty.of("has_totem_of_undying");
	
	public EndPortalLockBlock(Settings settings) {
		super(settings);
		this.setDefaultState((BlockState)((BlockState)this.stateManager.getDefaultState())
				.with(HAS_NETHER_STAR, false)
				.with(HAS_ELDER_GUARDIAN_EYE, false)
				.with(HAS_ECHO_SHARD, false)
				.with(HAS_PILLAGER_BANNER, false)
				.with(HAS_TOTEM_OF_UNDYING, false));
	}
	
	@Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HAS_NETHER_STAR);
        builder.add(HAS_ELDER_GUARDIAN_EYE);
        builder.add(HAS_ECHO_SHARD);
        builder.add(HAS_PILLAGER_BANNER);
        builder.add(HAS_TOTEM_OF_UNDYING);
    }
	
	// TODO: Use syncWorldEvent to summon particles + stuff
	@Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		ItemStack itemStack = player.getStackInHand(hand);
        if (hand == Hand.MAIN_HAND && !EndPortalLockBlock.isAcceptedItem(itemStack) && EndPortalLockBlock.isAcceptedItem(player.getStackInHand(Hand.OFF_HAND))) {
            return ActionResult.PASS;
        }
        
        if (EndPortalLockBlock.isAcceptedItem(itemStack)) {
        	boolean success = false;
            if(itemStack.isOf(Items.NETHER_STAR)) {
            	if (!state.get(HAS_NETHER_STAR)) {
            		world.setBlockState(pos, state.with(HAS_NETHER_STAR, true));
            		success = true;
            	}
            } else if (itemStack.isOf(ModItems.ELDER_GUARDIAN_EYE)) {
            	if(!state.get(HAS_ELDER_GUARDIAN_EYE)) {
            		world.setBlockState(pos, state.with(HAS_ELDER_GUARDIAN_EYE, true));
            		success = true;
            	}
            } else if (itemStack.isOf(Items.ECHO_SHARD)) {
            	if(!state.get(HAS_ECHO_SHARD)) {
            		world.setBlockState(pos, state.with(HAS_ECHO_SHARD, true));
            		success = true;
            	}
            } else if (EndPortalLockBlock.isPillagerBanner(itemStack)) {
            	if(!state.get(HAS_PILLAGER_BANNER)) {
            		world.setBlockState(pos, state.with(HAS_PILLAGER_BANNER, true));
            		success = true;
            	}
            } else if (itemStack.isOf(Items.TOTEM_OF_UNDYING)) {
            	if(!state.get(HAS_TOTEM_OF_UNDYING)) {
            		world.setBlockState(pos, state.with(HAS_TOTEM_OF_UNDYING, true));
            		success = true;
            	}
            }
            
            if (success == true) {
            	player.playSound(SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE, 1, 1);
            	if (!player.getAbilities().creativeMode) {
            		itemStack.decrement(1);
            	}
            	
            	BlockState updatedState = world.getBlockState(pos);
            	
            	if (updatedState.get(HAS_NETHER_STAR) && updatedState.get(HAS_ELDER_GUARDIAN_EYE) && updatedState.get(HAS_ECHO_SHARD) && updatedState.get(HAS_PILLAGER_BANNER) && updatedState.get(HAS_TOTEM_OF_UNDYING)) {
            		LocksAndKeys.LOGGER.info("Player: {} has unlocked the ender portal at: {}", player.getName().getString(), pos);
            		
            		for(int x = -1; x < 2; x++) {
            			BlockPos adjPos = pos.add(x, 0, 0);
            			if(world.getBlockState(adjPos).getBlock() == Blocks.CHAIN) {
            				world.breakBlock(adjPos, false);
            			}
            		}
            		
            		for(int z = -1; z < 2; z++) {
            			BlockPos adjPos = pos.add(0, 0, z);
            			if(world.getBlockState(adjPos).getBlock() == Blocks.CHAIN) {
            				world.breakBlock(adjPos, false);
            			}
            		}
            		
            		world.breakBlock(pos, false);
            		player.playSound(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);
            		
            		if(world.isClient) {
            			MinecraftClient.getInstance().particleManager.addEmitter(player, ParticleTypes.TOTEM_OF_UNDYING, 30);
            		}
            		
            		// check ender portal predicate in order to create the portal
            		BlockPattern.Result result = EndPortalFrameBlock.getCompletedFramePattern().searchAround(world, pos);
                    if (result != null) {
                        BlockPos posIter = result.getFrontTopLeft().add(-3, 0, -3);
                        for (int i = 0; i < 3; ++i) {
                            for (int j = 0; j < 3; ++j) {
                                world.setBlockState(posIter.add(i, 0, j), Blocks.END_PORTAL.getDefaultState(), Block.NOTIFY_LISTENERS);
                            }
                        }
                        world.syncGlobalEvent(WorldEvents.END_PORTAL_OPENED, posIter.add(1, 0, 1), 0);
                    }
            	}
            	
            	return ActionResult.success(world.isClient);
            } else {
            	player.playSound(SoundEvents.ENTITY_VILLAGER_NO, 1, 1);
            	player.sendMessage(Text.translatable("block.locks-and-keys.end_portal_lock.item_already_filled"), true);
            }
        } else {
        	player.playSound(SoundEvents.ENTITY_VILLAGER_NO, 1, 1);
        	player.sendMessage(Text.translatable("block.locks-and-keys.end_portal_lock.wrong_item"), true);
        }
        
        return ActionResult.PASS;
    }
	
	private static boolean isPillagerBanner(ItemStack stack) {
		// are/is equal checks stack count as well, we only want to check if the item in question is an ominous banner
		return ItemStack.canCombine(stack, Raid.getOminousBanner());
	}
	
	public static boolean isAcceptedItem(ItemStack stack) {
		return stack.isOf(Items.NETHER_STAR) || stack.isOf(ModItems.ELDER_GUARDIAN_EYE) || stack.isOf(Items.ECHO_SHARD) || isPillagerBanner(stack) || stack.isOf(Items.TOTEM_OF_UNDYING);
	}
}
