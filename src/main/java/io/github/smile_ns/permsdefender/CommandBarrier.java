package io.github.smile_ns.permsdefender;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;

public class CommandBarrier implements Listener {

    private final Plugin plugin;
    private final ConfigManager manager;

    public CommandBarrier(Plugin plugin) {
        this.plugin = plugin;
        this.manager = new ConfigManager(plugin);
    }

    @EventHandler
    public void onSendCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("permsdefender.defender")) return;

        String cmd = event.getMessage().toLowerCase().substring(1);
        if (isUnusableCmd(cmd, manager.getDefendedCmds())) {
            plugin.getLogger().info(player.getName() + " tried to execute the command, \"/" + cmd + "\".");
            player.sendMessage(ChatColor.RED + "You have no permission to execute this command.");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onExecuteByBlockSender(ServerCommandEvent event) {
        CommandSender sender = event.getSender();
        if (!(sender instanceof BlockCommandSender)) return;

        BlockCommandSender block = (BlockCommandSender) sender;
        String cmd = event.getCommand().toLowerCase();
        if (!cmd.startsWith("execute") && !cmd.startsWith("/execute")) return;
        if (!cmd.contains(" run ")) return;

        String[] cmdSplit = cmd.split(" run ");

        for (String split : Arrays.copyOfRange(cmdSplit, 1, cmdSplit.length)) {
            List<String> defendedList = manager.getDefendedCmdsWithExecute();
            if(isUnusableCmd(split, defendedList)) {
                event.setCancelled(true);

                if (manager.isEnableCmdBlockLog())
                    plugin.getLogger().info("The command block(" + block.getBlock().getLocation() +
                                    ") tried to execute the command, \"" + cmd + "\"");
            }
        }
    }

    private static boolean isUnusableCmd(String cmd, List<String> defendedList) {
        for (String defended : defendedList) {
            String[] defendedSplit = defended.split("(?<= \\*)|(?= \\*)");
            if (cmd.startsWith(defendedSplit[0])) {

                String[] cmdSplit =
                        cmd.split("(?<=" + defendedSplit[0] + ")|(?=" + defendedSplit[0] + ")");
                return cmdSplit.length != 1 || defendedSplit.length <= 1;
            }
        }

        return false;
    }
}
