package io.github.smile_ns.permsdefender;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class PermsDefender extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        File config = new File("plugins" + File.separator + "PermsDefender" + File.separator + "config.yml");
        if (!config.exists()) {
            new ConfigManager(this).init();
            this.getLogger().info("Have made \"config.yml\".");
        }

        Bukkit.getServer().getPluginManager().registerEvents(new CommandBarrier(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
