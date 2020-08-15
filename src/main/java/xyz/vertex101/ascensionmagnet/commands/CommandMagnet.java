package xyz.vertex101.ascensionmagnet.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.vertex101.ascensionmagnet.AscensionMagnet;

public class CommandMagnet implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("magnet")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only players can use this command.");
            } else {
                Player player = (Player)sender;
                if (args.length == 1) {
                    String arg = args[0].toLowerCase();
                    if (arg.equals("on")) {
                        if (!AscensionMagnet.getPlugin().magnetPlayers.contains(player)) {
                            AscensionMagnet.getPlugin().magnetPlayers.add(player);
                            player.sendMessage("You turned on your magnet.");
                        } else {
                            player.sendMessage("Your magnet is already on!");
                        }
                    } else if (arg.equals("off")) {
                        if (AscensionMagnet.getPlugin().magnetPlayers.contains(player)) {
                            AscensionMagnet.getPlugin().magnetPlayers.remove(player);
                            player.sendMessage("You turned off your magnet.");
                        } else {
                            player.sendMessage("Your magnet is already off!");
                        }
                    }
                } else {
                    sender.sendMessage("Usage: /magnet <on|off>");
                }
            }
            return true;
        }
        return false;
    }
}
