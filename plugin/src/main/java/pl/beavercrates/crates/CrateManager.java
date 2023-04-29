package pl.beavercrates.crates;

import de.tr7zw.nbtapi.NBTItem;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.beavercrates.BeaverCrates;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
public class CrateManager {

    private final BeaverCrates plugin = BeaverCrates.getInstance();
    private final HashMap<String, Crate> crates = new HashMap<>();
    private final List<CrateOpen> crateOpens = new ArrayList<>();

    public void addCrate(Crate crate) {
        if(getCrate(crate.getName()) != null) {
            crates.remove(getCrate(crate.getName()));
        }
        crates.put(crate.getName(), crate);
        plugin.getLogger().info("Added crate: " + crate.getName());
    }

    public void openCrate(int slot, Crate crate, Player player) {
        ItemStack item = new ItemStack(player.getInventory().getItemInMainHand());
        ItemStack crateItem = new ItemStack(crate.getCrate());
        crateItem.setAmount(1);
        item.setAmount(1);
        if(!item.equals(crateItem)) return;
        item = new ItemStack(player.getInventory().getItemInMainHand());
        item.setAmount(item.getAmount() - 1);
        player.getInventory().setItem(slot, item);
        // Find and take key
        ItemStack key = new ItemStack(crate.getKey());
        key.setAmount(1);
        boolean found = false;
        int keySlot = -1;
        for(int i = 0; i < 36; i++) {
            if(player.getInventory().getItem(i) == null) continue;
            item = new ItemStack(player.getInventory().getItem(i));
            item.setAmount(1);
            if(item.equals(key)) {
                found = true;
                keySlot = i;
                break;
            }
        }
        if(!found) {
            player.sendMessage("§cYou don't have a key!");
            return;
        }
        item = new ItemStack(player.getInventory().getItem(keySlot));
        item.setAmount(player.getInventory().getItem(keySlot).getAmount() - 1);
        player.getInventory().setItem(keySlot, item);
        //
        crateOpens.add(new CrateOpen(player, crate));
    }

    public void giveCrate(Crate crate, Player player) {
        ItemStack is = new ItemStack(crate.getCrate());
        is.setAmount(1);
        addItem(player, is);
    }

    public void giveKey(Crate crate, Player player) {
        ItemStack is = new ItemStack(crate.getKey());
        is.setAmount(1);
        addItem(player, is);
    }

    public void addItem(Player player, ItemStack item) {
        HashMap<Integer, ItemStack> rest = player.getInventory().addItem(new ItemStack(item));
        for(Integer key : rest.keySet()) {
            player.getWorld().dropItemNaturally(player.getLocation(), rest.get(key));
        }
    }

    @Nullable
    public Crate getCrate(String name) {
        for(String key : crates.keySet()) {
            if(crates.get(key).getName().equals(name)) {
                return crates.get(key);
            }
        }
        return null;
    }

}
