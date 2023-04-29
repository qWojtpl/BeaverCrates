package pl.beavercrates.events;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import pl.beavercrates.BeaverCrates;
import pl.beavercrates.crates.Crate;
import pl.beavercrates.crates.CrateOpen;
import pl.beavercrates.editor.Editor;
import pl.beavercrates.editor.EditorMode;

public class Events implements Listener {

    private final BeaverCrates plugin = BeaverCrates.getInstance();

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(!event.getAction().equals(Action.RIGHT_CLICK_AIR) && !event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        Player p = event.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();
        if(item.getType().equals(Material.AIR)) return;
        NBTItem nbtItem = new NBTItem(item);
        if(!nbtItem.getBoolean("beavercrates.isCrate")) return;
        Crate crate = plugin.getCrateManager().getCrate(nbtItem.getString("beavercrates.id"));
        if(crate == null) return;
        plugin.getCrateManager().openCrate(p.getInventory().getHeldItemSlot(), crate, p);
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        for(CrateOpen co : plugin.getCrateManager().getCrateOpens()) {
            if(event.getInventory().equals(co.getInventory())) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        for(CrateOpen co : plugin.getCrateManager().getCrateOpens()) {
            if(event.getInventory().equals(co.getInventory())) {
                event.setCancelled(true);
                return;
            }
        }
        if(event.getClickedInventory() == null) return;
        for(Editor e : plugin.getEditorManager().getEditors()) {
            if(event.getInventory().equals(e.getInventory())) {
                if(event.getClick().equals(ClickType.DOUBLE_CLICK)) {
                    event.setCancelled(true);
                    return;
                }
            }
            if(event.getClickedInventory().equals(e.getInventory())) {
                event.setCancelled(true);
                if(event.getSlot() == 38 || event.getSlot() == 42) {
                    if(event.getCursor() == null) return;
                    if(event.getCursor().getType().equals(Material.AIR)) return;
                    e.getInventory().setItem(event.getSlot(), event.getCursor());
                    e.updateSave();
                } else if(event.getSlot() == 40) {
                    e.save();
                } else if(event.getSlot() == 49) {
                    if(e.getCurrentMode().equals(EditorMode.CONTENT)) {
                        e.setCurrentMode(EditorMode.PERCENTAGE);
                    } else if(e.getCurrentMode().equals(EditorMode.PERCENTAGE)) {
                        e.setCurrentMode(EditorMode.CONTENT);
                    }
                } else if(event.getSlot() < 27) {
                    if(e.getCurrentMode().equals(EditorMode.CONTENT)) {
                        event.setCancelled(false);
                    } else if(e.getCurrentMode().equals(EditorMode.PERCENTAGE)) {
                        event.setCancelled(true);
                        if(e.getInventory().getItem(event.getSlot()) == null) return;
                        if(e.getInventory().getItem(event.getSlot()).equals(Material.AIR)) return;
                        plugin.getEditorManager().getEditorMap().put((Player) event.getWhoClicked(), e);
                        event.getWhoClicked().closeInventory();
                        e.setEditingSlot(event.getSlot());
                        event.getWhoClicked().sendMessage("§aPlease type percentage on chat.");
                    }
                }
                return;
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        for(CrateOpen co : plugin.getCrateManager().getCrateOpens()) {
            if(event.getInventory().equals(co.getInventory())) {
                co.reOpenInventory();
                return;
            }
        }
        for(Editor e : plugin.getEditorManager().getEditors()) {
            if(event.getInventory().equals(e.getInventory())) {
                if(plugin.getEditorManager().getEditorMap().containsKey(event.getPlayer())) return;
                e.save();
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if(event.isCancelled()) return;
        Player player = event.getPlayer();
        if(!plugin.getEditorManager().getEditorMap().containsKey(player)) return;
        Editor editor = plugin.getEditorManager().getEditorMap().get(player);
        event.setCancelled(true);
        plugin.getEditorManager().getEditorMap().remove(player);
        float percentage;
        try {
            percentage = Float.parseFloat(event.getMessage());
        } catch(NumberFormatException e) {
            player.sendMessage("§cFailed!");
            editor.reOpen();
            return;
        }
        editor.getPercentage().put(editor.getEditingSlot(), percentage);
        editor.reOpen();
    }

}
