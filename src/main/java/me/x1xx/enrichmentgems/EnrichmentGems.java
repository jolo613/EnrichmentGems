package me.x1xx.enrichmentgems;

import me.x1xx.enrichmentgems.commands.GiveCommand;
import me.x1xx.enrichmentgems.listener.GemListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class EnrichmentGems extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        new GiveCommand(this);
        Bukkit.getPluginManager().registerEvents(new GemListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
