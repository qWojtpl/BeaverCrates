package pl.beavercrates.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.beavercrates.BeaverCrates;
import pl.beavercrates.crates.Crate;
import pl.beavercrates.crates.CrateManager;
import pl.beavercrates.permissions.PermissionManager;
import pl.beavercrates.util.PlayerUtil;

public class Commands implements CommandExecutor {

    private final BeaverCrates plugin = BeaverCrates.getInstance();
    private final PermissionManager pm = plugin.getPermissionManager();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            if(!sender.hasPermission(pm.getPermission("beavercrates.manage"))) {
                sender.sendMessage("§cYou don't have permission!");
                return true;
            }
        }
        if(args.length > 0) {
            if(args[0].equalsIgnoreCase("give")) {
                if(args.length > 1) {
                    boolean giveKey = false;
                    if(args[1].equalsIgnoreCase("key")) {
                        giveKey = true;
                    }
                    if(args.length > 2) {
                        Crate crate = plugin.getCrateManager().getCrate(args[1]);
                        if(crate == null) {
                            sender.sendMessage("§cCrate not found...");
                            return true;
                        }
                        if(args.length == 3) {
                            if(!(sender instanceof Player)) {
                                sender.sendMessage("§cCorrect usage: /bcrates get <key|case> <name> <player>");
                                return true;
                            }
                            if(giveKey) {
                                plugin.getCrateManager().giveKey(crate, (Player) sender);
                            } else {
                                plugin.getCrateManager().giveCrate(crate, (Player) sender);
                            }
                        } else {
                            Player p = PlayerUtil.getPlayer(args[2]);
                            if(p == null) {
                                sender.sendMessage("§cCan't found player: " + args[2]);
                                return true;
                            }
                            if(giveKey) {
                                plugin.getCrateManager().giveKey(crate, p);
                            } else {
                                plugin.getCrateManager().giveCrate(crate, p);
                            }
                        }
                    }
                } else {
                    sender.sendMessage("§cCorrect usage: /bcrates get <key|case> <name> [player]");
                }
            }
        }
        return true;
    }

    private void showHelp(CommandSender sender) {

    }

}
