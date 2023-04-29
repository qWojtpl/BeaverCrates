package pl.beavercrates.crates;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@Getter
public class CrateOpen {

    private final Player player;
    private final Inventory inventory;

    public CrateOpen(Player player) {
        this.player = player;
        this.inventory = Bukkit.createInventory(player, 3, "BeaverCrates");
        setupInventory();
    }

    private void setupInventory() {
        for(int i = 0; i < 27; i++) {
            getInventory().setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        }
        getInventory().setItem(4, new ItemStack(Material.HOPPER));
        player.openInventory(getInventory());
    }

}
