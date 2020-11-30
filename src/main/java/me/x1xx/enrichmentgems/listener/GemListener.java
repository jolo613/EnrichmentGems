package me.x1xx.enrichmentgems.listener;

import me.x1xx.enrichmentgems.EnrichmentGems;
import me.x1xx.enrichmentgems.gems.GemType;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class GemListener implements Listener {
    private final EnrichmentGems gems;

    public GemListener(EnrichmentGems gems) {
        this.gems = gems;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onRightClick(PlayerInteractEvent event) {
        if (event.getItem() != null && event.getClickedBlock() != null
                && event.getItem().hasItemMeta() && (event.getAction() == Action.LEFT_CLICK_BLOCK
                || event.getAction() == Action.RIGHT_CLICK_BLOCK)
                && event.getClickedBlock().getType() == Material.STONE) {
            ItemStack itemStack = event.getItem();
            net.minecraft.server.v1_12_R1.ItemStack nmsI = CraftItemStack.asNMSCopy(itemStack);
            if (nmsI.hasTag()) {
                NBTTagCompound compound = nmsI.getTag();
                // stupid IDE throws null errors even after I checked if the tag exists.
                assert compound != null;
                if (compound.hasKey("egem.gemtype")) {
                    GemType type = GemType.fromString(compound.getString("egem.gemtype"));
                    if (type == null) {
                        Bukkit.getLogger().warning("Gemtype on EGem was null! Player: " + event.getPlayer().getName());
                        return;
                    }
                    int tier = compound.getInt("egem.tier");
                    itemStack.setAmount(itemStack.getAmount() - 1);
                    type.getGem().doAction(tier, event.getClickedBlock(), gems);
                }
            }
        }
    }
}
