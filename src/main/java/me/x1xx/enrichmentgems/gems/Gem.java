package me.x1xx.enrichmentgems.gems;

import me.x1xx.enrichmentgems.EnrichmentGems;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public interface Gem {


    void addGems(int amount, int tier, Player player);

    int getMaxTier();

    default void doAction(int tier, Block block, EnrichmentGems gems) {
        // We add 1 to maximum for inclusive-exclusive random generator stupid thingy
        final int amount = ThreadLocalRandom.current().nextInt(getMinimum(tier), getMaximum(tier) + 1);
        // We have the number of items we wish to turn, start an async runnable to run this async.

        final World world = block.getWorld();
        Bukkit.getScheduler().runTaskAsynchronously(gems, () -> {
            List<Block> blockList = new ArrayList<>();
            // Get blocks in a radius
            for (int x = block.getX() - getRadius(tier); x <= block.getX() + getRadius(tier); x++) {
                for (int y = block.getY() - getRadius(tier); y <= block.getY() + getRadius(tier); y++) {
                    for (int z = block.getZ() - getRadius(tier); z <= block.getZ() + getRadius(tier); z++) {
                        Block blockAt = world.getBlockAt(x, y, z);
                        if (blockAt.getType() == Material.STONE)
                            blockList.add(blockAt);
                    }
                }
            }

            // Shuffle the collection
            List<Block> replaceable = new ArrayList<>();
            for (int i = 0; i < amount; i++) {
                if (blockList.size() < 1) break;
                replaceable.add(blockList.remove(ThreadLocalRandom.current().nextInt(blockList.size())));
            }
            // Start the other async task which replaces blocks
            new BlockReplaceRunnable(gems, replaceable, this);
        });
    }

    int getMinimum(int tier);

    int getMaximum(int tier);

    int getRadius(int tier);

    Material getReplaceableMaterial();
}
