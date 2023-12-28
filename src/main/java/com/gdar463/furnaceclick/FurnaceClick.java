package com.gdar463.furnaceclick;

import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.capabilities.Capabilities;
import net.neoforged.neoforge.common.util.LazyOptional;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;

import java.util.Arrays;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(FurnaceClick.MODID)
public class FurnaceClick {
    public static final String MODID = "furnaceclick";

    public static ResourceLocation[] allowedBlocks = {new ResourceLocation("minecraft", "furnace"), new ResourceLocation("minecraft","smoker"), new ResourceLocation("minecraft", "blast_furnace")};

    public FurnaceClick() {
        NeoForge.EVENT_BUS.register(EventHandler.class);
    }

    public static class EventHandler {
        @SubscribeEvent
        public static void leftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
            BlockEntity block = event.getLevel().getBlockEntity(event.getPos());
            if (block != null && Arrays.asList(FurnaceClick.allowedBlocks).contains(BuiltInRegistries.BLOCK.getKey(block.getBlockState().getBlock())) && event.getFace() == block.getBlockState().getValue(DirectionProperty.create("facing", Direction.Plane.HORIZONTAL))) {
                LazyOptional<IItemHandler> furnaceCapability = block.getCapability(Capabilities.ITEM_HANDLER);
                if (furnaceCapability.resolve().isPresent()) {
                    IItemHandler furnaceInventory = furnaceCapability.resolve().get();
                    Player player = event.getEntity();
                    ItemStack output = furnaceInventory.getStackInSlot(2);
                    if (output.getCount() != 0) {
                        player.getInventory().placeItemBackInInventory(furnaceInventory.extractItem(2, output.getCount(), false));
                    }
                }
            }
        }
    }
}
