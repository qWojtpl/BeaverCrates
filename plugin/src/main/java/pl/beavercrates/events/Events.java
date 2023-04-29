package pl.beavercrates.events;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import pl.beavercrates.BeaverCrates;
import pl.beavercrates.crates.Crate;
import pl.beavercrates.crates.CrateOpen;

public class Events implements Listener {

    private final BeaverCrates plugin = BeaverCrates.getInstance();

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(!event.getAction().equals(Action.RIGHT_CLICK_AIR) && !event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        Player p = event.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();
        if(!item.getType().equals(Material.CHEST)) return;
        NBTItem nbtItem = new NBTItem(item);
        if(!nbtItem.getBoolean("beavercrates.isCrate")) return;
        Crate crate = plugin.getCrateManager().getCrate(nbtItem.getString("beavercrates.id"));;
        if(crate == null) return;
        plugin.getCrateManager().openCrate(p.getInventory().getHeldItemSlot(), crate, p);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        for(CrateOpen co : plugin.getCrateManager().getCrateOpens()) {
            if(event.getInventory().equals(co.getInventory())) {
                co.reOpenInventory();
            }
        }
    }

}
