package io.github.smile_ns.permsdefender;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class ConfigManager {

    private final Plugin plugin;
    private FileConfiguration config;

    public ConfigManager(Plugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    public List<String> getDefendedCmds() {
        return config.getStringList("defended-cmds");
    }

    public List<String> getDefendedCmdsWithExecute() {
        return config.getStringList("defended-cmds-with-execute");
    }

    public boolean isEnableCmdBlockLog() {
        return config.getBoolean("enable-cmd-block-log");
    }

    public void init() {
        String[] cmds = {
                "lp", "luckperms", "op", "deop", "kick",
                "ban", "ban-ip", "pardon", "pardon-ip", "whitelist"
        };
        config.set("defended-cmds", cmds);

        String[] cmdsOnBlock = {
                "op", "deop", "kick", "ban", "ban-ip",
                "pardon", "pardon-ip", "whitelist"
        };
        config.set("defended-cmds-with-execute", cmdsOnBlock);
        config.set("enable-cmd-block-log", true);

        saveConfig();
    }

    public void saveConfig() {
        plugin.saveConfig();
        plugin.reloadConfig();
        config = plugin.getConfig();
    }
}
