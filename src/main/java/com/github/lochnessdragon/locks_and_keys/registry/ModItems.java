package com.github.lochnessdragon.locks_and_keys.registry;

import com.github.lochnessdragon.locks_and_keys.LocksAndKeys;
import com.github.lochnessdragon.locks_and_keys.items.ElderGuardianEye;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Rarity;

public class ModItems {
	public static final Item ELDER_GUARDIAN_EYE = new ElderGuardianEye(new FabricItemSettings().rarity(Rarity.EPIC));
	public static final Item END_PORTAL_LOCK_ITEM = new BlockItem(ModBlocks.END_PORTAL_LOCK, new FabricItemSettings());
	
	public static void register() {
		Registry.register(Registries.ITEM, LocksAndKeys.identifier("elder_guardian_eye"), ELDER_GUARDIAN_EYE);
		Registry.register(Registries.ITEM, LocksAndKeys.identifier("end_portal_lock"), END_PORTAL_LOCK_ITEM);
		
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(content -> {
			content.addAfter(Items.SPIDER_EYE, ELDER_GUARDIAN_EYE);
		});
		
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(content -> {
			content.addAfter(Items.END_PORTAL_FRAME, END_PORTAL_LOCK_ITEM);
		});
	}
}
