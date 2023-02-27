package com.github.lochnessdragon.locks_and_keys.registry;

import com.github.lochnessdragon.locks_and_keys.LocksAndKeys;
import com.github.lochnessdragon.locks_and_keys.blocks.EndPortalLockBlock;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModBlocks {
	public static final Block END_PORTAL_LOCK = new EndPortalLockBlock(FabricBlockSettings.of(Material.AMETHYST).strength(-1.0f, 3600000.0f).dropsNothing());
	
	public static void register() {
		Registry.register(Registries.BLOCK, LocksAndKeys.identifier("end_portal_lock"), END_PORTAL_LOCK);
	}
}
