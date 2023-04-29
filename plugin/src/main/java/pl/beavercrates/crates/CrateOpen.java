package pl.beavercrates.crates;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pl.beavercrates.BeaverCrates;
import pl.beavercrates.util.RandomNumber;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CrateOpen {

    private final BeaverCrates plugin = BeaverCrates.getInstance();
    private final Player player;
    private final Inventory inventory;
    private final Crate crate;
    private int openTask;
    private int level;
    private int delay;
    private boolean isOpening = false;

    public CrateOpen(Player player, Crate crate) {
        this.player = player;
        this.crate = crate;
        this.inventory = Bukkit.createInventory(player, 27, "BeaverCrates");
        setupInventory();
    }

    private void setupInventory() {
        isOpening = true;
        for(int i = 0; i < 27; i++) {
            getInventory().setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        }
        getInventory().setItem(4, new ItemStack(Material.HOPPER));
        for(int i = 10; i <= 16; i++) {
            getInventory().setItem(i, getRandomItem());
        }
        player.openInventory(getInventory());
        createTask();
    }

    private void createTask() {
        openTask = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            if(inventory == null) {
                cancelOpenTask();
                return;
            }
            player.playSound(player.getLocation(), Sound.BLOCK_DISPENSER_DISPENSE, 1.0F, 1.0F);
            for(int i = 10; i <= 16; i++) {
                if((i + 1) > 16) {
                    getInventory().setItem(i, getRandomItem());
                } else {
                    getInventory().setItem(i, getInventory().getItem(i + 1));
                }
            }
            level++;
            if(level >= 60) {
                cancelOpenTask();
                closePlayerInventory();
                return;
            }
            int newdelay = level/8;
            if(newdelay != delay) {
                cancelOpenTask();
                delay = newdelay;
                createTask();
            }
        }, 2L * delay, 2L * delay);
    }

    private void cancelOpenTask() {
        plugin.getServer().getScheduler().cancelTask(openTask);
    }

    private void closePlayerInventory() {
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            plugin.getCrateManager().addItem(player, inventory.getItem(13));
            isOpening = false;
            player.closeInventory();
        }, 40L);
    }

    public void reOpenInventory() {
        if(!isOpening) return;
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            player.openInventory(inventory);
        }, 2L);
    }

    private ItemStack getRandomItem() {
        List<ItemStack> items = new ArrayList<>(crate.getItems());
        return items.get(RandomNumber.randomInt(0, items.size() - 1));
    }

}
