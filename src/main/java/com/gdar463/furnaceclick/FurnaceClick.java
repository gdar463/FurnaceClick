package com.gdar463.furnaceclick;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.DirectionProperty;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("furnaceclick")
public class FurnaceClick {

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public static ResourceLocation[] allowedBlocks = {new ResourceLocation("minecraft", "furnace"), new ResourceLocation("minecraft","smoker"), new ResourceLocation("minecraft", "blast_furnace")};

    public FurnaceClick() {
        MinecraftForge.EVENT_BUS.register(EventHandler.class);
    }

    public static class EventHandler {
        @SubscribeEvent
        public static void leftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
            TileEntity block = event.getWorld().getBlockEntity(event.getPos());
            if (block != null && Arrays.asList(FurnaceClick.allowedBlocks).contains(block.getType().getRegistryName()) && event.getFace() == block.getBlockState().getValue(DirectionProperty.create("facing", Direction.Plane.HORIZONTAL))) {
                LazyOptional<IItemHandler> furnaceCapability = block.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
                if (furnaceCapability.resolve().isPresent()) {
                    IItemHandler furnaceInventory = furnaceCapability.resolve().get();
                    PlayerEntity player = event.getPlayer();
                    ItemStack output = furnaceInventory.getStackInSlot(2);
                    if (output.getCount() != 0) {
                        player.inventory.placeItemBackInInventory(event.getWorld(), furnaceInventory.extractItem(2, output.getCount(), false));
                    }
                }
            }
        }
    }
}
