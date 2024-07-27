package net.divinitystudios.limitlesstrialvaults;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_21_R1.CraftWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Block eventBlock = event.getClickedBlock();
        RegistryAccess registryAccess = MinecraftServer.getDefaultRegistryAccess();
        if (eventBlock != null && eventBlock.getType() == Material.VAULT) {
            // Get the NMS world
            CraftWorld craftWorld = (CraftWorld) eventBlock.getWorld();
            // Get the BlockEntity of the block
            BlockEntity blockEntity = craftWorld.getHandle().getBlockEntity(new BlockPos(eventBlock.getX(), eventBlock.getY(), eventBlock.getZ()));

            if (blockEntity != null && blockEntity.getType() == BlockEntityType.VAULT) { // Make sure to use the correct BlockEntityType
                // Get the NBT data of the BlockEntity
                CompoundTag nbtTagCompound = blockEntity.saveWithoutMetadata(registryAccess);

                // Modify the NBT data
                if (nbtTagCompound.contains("server_data")) {
                    CompoundTag serverData = nbtTagCompound.getCompound("server_data");
                    serverData.put("rewarded_players", new CompoundTag());
                }
                // Save the modified NBT data back to the BlockEntity
                blockEntity.loadWithComponents(nbtTagCompound, MinecraftServer.getDefaultRegistryAccess());
                blockEntity.setChanged(); // Notify that the BlockEntity has changed
            }
        }
    }
}
