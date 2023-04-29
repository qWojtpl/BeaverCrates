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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import pl.beavercrates.BeaverCrates;
import pl.beavercrates.crates.Crate;
import pl.beavercrates.crates.CrateOpen;
import pl.beavercrates.editor.Editor;

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
                if(event.getSlot() == 38 || event.getSlot() == 42) {
                    event.setCancelled(true);
                    if(event.getCursor() == null) return;
                    if(event.getCursor().getType().equals(Material.AIR)) return;
                    e.getInventory().setItem(event.getSlot(), event.getCursor());
                    e.updateSave();
                } else if(event.getSlot() == 40) {
                    e.getPlayer().closeInventory();
                    event.setCancelled(true);
                } else if(event.getSlot() >= 27 && event.getSlot() <= 53) {
                    event.setCancelled(true);
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
                e.save();
            }
        }
    }

}
