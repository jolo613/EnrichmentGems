package me.x1xx.enrichmentgems.gems.types;

import com.azortis.azortislib.experimental.inventory.item.Item;
import com.azortis.azortislib.xseries.XMaterial;
import me.x1xx.enrichmentgems.RomanNumber;
import me.x1xx.enrichmentgems.gems.Gem;
import me.x1xx.enrichmentgems.gems.GemType;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Coal implements Gem {
    @Override
    public void addGems(int amount, int tier, Player player) {
        String numerals = RomanNumber.toRoman(tier);
        ItemStack item = XMaterial.COAL.parseItem().get();
        // Adding in glow


        net.minecraft.server.v1_12_R1.ItemStack nmsI = CraftItemStack.asNMSCopy(item);
        NBTTagCompound compound = new NBTTagCompound();
        compound.setString("egem.gemtype", GemType.COAL.name());
        compound.setInt("egem.tier", tier);
        nmsI.setTag(compound);
        ItemStack itemStack = new Item(CraftItemStack.asBukkitCopy(nmsI))
                .name("&a&lEnrichment Gem: &f&l&nCoal " + numerals)
                .lore("&7Replaces nearby stone with ores.",
                        "",
                        "&7Creates &b" + getMinimum(tier) + "-" + getMaximum(tier) + "&e Coal Ore&7.",
                        "",
                        "&b&lRight-Click&f onto a stone block to use.")
                .amount(amount)
                .getItemStack();

        itemStack.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        ItemMeta meta = itemStack.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(meta);
        player.getInventory().addItem(itemStack);
    }

    @Override
    public int getMaxTier() {
        return 6;
    }

    @Override
    public int getMinimum(int tier) {
        switch (tier) {
            case 2:
                return 4;
            case 3:
                return 7;
            case 4:
                return 12;
            case 5:
                return 21;
            case 6:
                return 35;
            case 1:
            default:
                return 2;
        }
    }

    @Override
    public int getMaximum(int tier) {
        switch (tier) {
            case 2:
                return 7;
            case 3:
                return 10;
            case 4:
                return 18;
            case 5:
                return 32;
            case 6:
                return 48;
            case 1:
            default:
                return 5;
        }
    }

    @Override
    public int getRadius(int tier) {
        switch (tier) {
            case 4:
                return 3;
            case 5:
            case 6:
                return 4;
            case 2:
            case 3:
            case 1:
            default:
                return 2;
        }
    }

    @Override
    public Material getReplaceableMaterial() {
        return Material.COAL_ORE;
    }
}
