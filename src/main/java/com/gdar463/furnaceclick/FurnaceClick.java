package com.gdar463.furnaceclick;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.Arrays;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("furnaceclick")
public class FurnaceClick {
    public static ResourceLocation[] allowedBlocks = {new ResourceLocation("minecraft", "furnace"), new ResourceLocation("minecraft","smoker"), new ResourceLocation("minecraft", "blast_furnace")};

    public FurnaceClick() {
        MinecraftForge.EVENT_BUS.register(EventHandler.class);
    }

    public static class EventHandler {
        @SubscribeEvent
        public static void leftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
            BlockEntity block = event.getWorld().getBlockEntity(event.getPos());
            if (block != null && Arrays.asList(FurnaceClick.allowedBlocks).contains(block.getType().getRegistryName()) && event.getFace() == block.getBlockState().getValue(DirectionProperty.create("facing", Direction.Plane.HORIZONTAL))) {
                LazyOptional<IItemHandler> furnaceCapability = block.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
                if (furnaceCapability.resolve().isPresent()) {
                    IItemHandler furnaceInventory = furnaceCapability.resolve().get();
                    Player player = event.getPlayer();
                    ItemStack output = furnaceInventory.getStackInSlot(2);
                    if (output.getCount() != 0) {
                        player.getInventory().placeItemBackInInventory(furnaceInventory.extractItem(2, output.getCount(), false));
                    }
                }
            }
        }
    }
}
