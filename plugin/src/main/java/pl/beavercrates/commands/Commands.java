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
            if(!sender.hasPermission(pm.getPermission("bcrates.manage"))) {
                sender.sendMessage("§cYou don't have permission!");
                return true;
            }
        }
        if(args.length > 0) {
            if(args[0].equalsIgnoreCase("get")) {
                if(args.length > 1) {
                    boolean giveKey = false;
                    if(args[1].equalsIgnoreCase("key")) {
                        giveKey = true;
                    }
                    if(args.length > 2) {
                        Crate crate = plugin.getCrateManager().getCrate(args[2]);
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
                            Player p = PlayerUtil.getPlayer(args[3]);
                            if(p == null) {
                                sender.sendMessage("§cCan't found player: " + args[3]);
                                return true;
                            }
                            if(giveKey) {
                                plugin.getCrateManager().giveKey(crate, p);
                            } else {
                                plugin.getCrateManager().giveCrate(crate, p);
                            }
                        }
                    } else {
                        sender.sendMessage("§cCorrect usage: /bcrates get <key|case> <name> [player]");
                    }
                } else {
                    sender.sendMessage("§cCorrect usage: /bcrates get <key|case> <name> [player]");
                }
            } else if(args[0].equalsIgnoreCase("editor")) {
                if(!(sender instanceof Player)) {
                    sender.sendMessage("§cYou must be a player!");
                    return true;
                }
                if(args.length > 1) {
                    plugin.getEditorManager().createEditor((Player) sender, args[1]);
                } else {
                    sender.sendMessage("§cCorrect usage: /bcrates editor <name>");
                }
            }
        }
        return true;
    }

    private void showHelp(CommandSender sender) {

    }

}
