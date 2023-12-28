package com.gdar463.furnaceclick;

import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

import java.util.Arrays;

public class FurnaceClick implements ModInitializer {
	public static Identifier[] allowedBlocks = {new Identifier("minecraft", "furnace"), new Identifier("minecraft", "smoker"), new Identifier("minecraft", "blast_furnace")};

	@Override
	public void onInitialize(ModContainer mod) {
		AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
			BlockEntity block = world.getBlockEntity(pos);
			if (Arrays.asList(FurnaceClick.allowedBlocks).contains(Registry.BLOCK.getId(world.getBlockState(pos).getBlock())) && direction == world.getBlockState(pos).get(Properties.HORIZONTAL_FACING)) {
				Inventory furnaceInventory = (Inventory) block;
				if (furnaceInventory != null && furnaceInventory.getStack(2).getCount() > 0) {
					player.getInventory().offerOrDrop(furnaceInventory.removeStack(2));
				}
			}
			return ActionResult.PASS;
		});
	}
}
