package pl.beavercrates.data;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.beavercrates.BeaverCrates;
import pl.beavercrates.crates.Crate;

import java.util.ArrayList;
import java.util.List;

public class DataHandler {

    private final BeaverCrates plugin = BeaverCrates.getInstance();

    public void loadConfig() {
        loadCrates();
    }

    public void loadCrates() {
        plugin.getCrateManager().getCrates().clear();
        List<ItemStack> items = new ArrayList<>();
        items.add(new ItemStack(Material.DIRT));
        items.add(new ItemStack(Material.STONE));
        items.add(new ItemStack(Material.GRASS_BLOCK));
        ItemStack key = new ItemStack(Material.TRIPWIRE_HOOK);
        ItemMeta im = key.getItemMeta();
        im.setDisplayName("§e§lTest key");
        key.setItemMeta(im);
        NBTItem nbtItem = new NBTItem(key);
        nbtItem.setBoolean("beavervcrates.isKey", true);
        nbtItem.setString("beavercrates.id", "test");
        nbtItem.applyNBT(key);
        Crate c = new Crate("test", items, key);
        plugin.getCrateManager().addCrate(c);
    }

}
