package com.github.lochnessdragon.locks_and_keys;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.lochnessdragon.locks_and_keys.registry.ModBlocks;
import com.github.lochnessdragon.locks_and_keys.registry.ModItems;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.util.Identifier;

public class LocksAndKeys implements ModInitializer {
	public static final String MOD_ID = "locks-and-keys";
	public static String VERSION = "";
	
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	
	@Override
	public void onInitialize() {
		FabricLoader.getInstance().getModContainer(MOD_ID).ifPresent((container) -> {
			VERSION = container.getMetadata().getVersion().getFriendlyString();
		});
		LOGGER.info("Locks and Keys v{} is initializing! Good luck with the adjusted bosses!", VERSION);
		
		ModItems.register();
		ModBlocks.register();
		
		// add elder guardian eye to loot table
		LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
		    // Let's only modify built-in loot tables and leave data pack loot tables untouched by checking the source.
		    // We also check that the loot table ID is equal to the ID we want.
		    if (source.isBuiltin() && EntityType.ELDER_GUARDIAN.getLootTableId().equals(id)) {
		        LOGGER.info("Adding the Elder Guardian Eye to the Elder Guardian loot table.");
		        LootPool.Builder poolBuilder = LootPool.builder().with(ItemEntry.builder(ModItems.ELDER_GUARDIAN_EYE));
		        
		        tableBuilder.pool(poolBuilder);
		    }
		});
	}
	
	public static Identifier identifier(String id) {
		return new Identifier(MOD_ID, id);
	}

}
