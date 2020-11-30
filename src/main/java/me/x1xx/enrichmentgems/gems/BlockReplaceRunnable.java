package me.x1xx.enrichmentgems.gems;

import me.x1xx.enrichmentgems.EnrichmentGems;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;

import java.util.Iterator;
import java.util.List;

public class BlockReplaceRunnable implements Runnable {
    private final EnrichmentGems plugin;
    private final Iterator<Block> blockList;
    private final int threadID;
    private final Gem gem;

    public BlockReplaceRunnable(EnrichmentGems plugin, List<Block> blocks, Gem gem) {
        this.plugin = plugin;
        this.blockList = blocks.iterator();
        threadID = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this, 20, 10).getTaskId();
        this.gem = gem;
    }

    @Override
    public void run() {
        if (blockList.hasNext()) {
            Block block = blockList.next();
            Bukkit.getScheduler().callSyncMethod(plugin, () -> {
                block.setType(gem.getReplaceableMaterial());
                block.getWorld().playSound(block.getLocation(), Sound.BLOCK_STONE_BREAK, 1.0f, 1.0f);
                block.getWorld().spawnParticle(Particle.BLOCK_CRACK, block.getLocation(), 15, block.getState().getData());
                return false;
            });
        } else {
            Bukkit.getScheduler().cancelTask(threadID);
        }
    }
}
