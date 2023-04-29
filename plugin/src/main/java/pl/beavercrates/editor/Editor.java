package pl.beavercrates.editor;

import de.tr7zw.nbtapi.NBTItem;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.units.qual.C;
import pl.beavercrates.BeaverCrates;
import pl.beavercrates.crates.Crate;
import pl.beavercrates.crates.CrateItem;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Editor {

    private final BeaverCrates plugin = BeaverCrates.getInstance();
    private final Player player;
    private final Inventory inventory;
    private final String crateName;
    private Crate crate;

    public Editor(Player player, String crateName) {
        this.player = player;
        this.inventory = Bukkit.createInventory(player, 54, "BeaverCrates Editor");
        this.crateName = crateName;
        this.crate = plugin.getCrateManager().getCrate(crateName);
        setupInventory();
    }

    private void setupInventory() {
        for(int i = 27; i < 54; i++) {
            inventory.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        }
        if(crate == null) {
            crate = new Crate(crateName, new ArrayList<>(), null, null);
        }
        ItemStack key = crate.getKey();
        int i = 0;
        for(CrateItem ci : crate.getItems()) {
            inventory.setItem(i, ci.getItemStack());
            i++;
        }
        if(crate.getKey() == null) {
            key = new ItemStack(Material.TRIPWIRE_HOOK);
            key.setAmount(1);
            ItemMeta im = key.getItemMeta();
            im.setDisplayName("§eSet crate key");
            List<String> lore = new ArrayList<>();
            lore.add("§aDrag item here");
            lore.add("§ato set crate key!");
            im.setLore(lore);
            key.setItemMeta(im);
            NBTItem nbt = new NBTItem(key);
            nbt.setBoolean("beavercrates.isDefault", true);
            nbt.applyNBT(key);
        }
        ItemStack crateItem = crate.getCrate();
        if(crateItem == null) {
            crateItem = new ItemStack(Material.CHEST);
            crateItem.setAmount(1);
            ItemMeta im = crateItem.getItemMeta();
            im.setDisplayName("§eSet crate item");
            List<String> lore = new ArrayList<>();
            lore.add("§aDrag item here");
            lore.add("§ato set crate item!");
            im.setLore(lore);
            crateItem.setItemMeta(im);
            NBTItem nbt = new NBTItem(crateItem);
            nbt.setBoolean("beavercrates.isDefault", true);
            nbt.applyNBT(crateItem);
        }
        inventory.setItem(38, key);
        updateSave();
        inventory.setItem(42, crateItem);
        player.openInventory(inventory);
    }

    public void save() {
        List<CrateItem> items = new ArrayList<>();
        for(int i = 0; i < 27; i++) {
            if(inventory.getItem(i) == null) continue;
            if(inventory.getItem(i).getType().equals(Material.AIR)) continue;
            items.add(new CrateItem(inventory.getItem(i), 0, 0));
        }
        crate.setItems(items);
        ItemStack key = inventory.getItem(38);
        if(key != null) {
            NBTItem keyNBT = new NBTItem(key);
            if(keyNBT.getBoolean("beavercrates.isDefault")) {
                cantSave("Key is not set");
                return;
            }
            keyNBT.setBoolean("beavercrates.isKey", true);
            keyNBT.setString("beavercrates.id", crate.getName());
            keyNBT.applyNBT(key);
        }
        ItemStack crateItem = inventory.getItem(42);
        if(crateItem != null) {
            NBTItem crateNBT = new NBTItem(crateItem);
            if(crateNBT.getBoolean("beavercrates.isDefault")) {
                cantSave("Crate item is not set");
                return;
            }
            crateNBT.setBoolean("beavercrates.isCrate", true);
            crateNBT.setString("beavercrates.id", crate.getName());
            crateNBT.applyNBT(crateItem);
        }
        crate.setKey(key);
        crate.setCrate(crateItem);
        plugin.getCrateManager().addCrate(crate);
        plugin.getDataHandler().saveCrate(crate);
        player.sendMessage("§aSaved crate: " + crate.getName());
    }

    private void cantSave(String reason) {
        player.sendMessage("§cCan't save this case! " + reason);
    }

    public void updateSave() {
        ItemStack saveItem = new ItemStack(Material.ENDER_EYE);
        ItemMeta im = saveItem.getItemMeta();
        im.setDisplayName("§2§lSave crate");
        List<String> lore = new ArrayList<>();
        lore.add("§aClick to save this crate!");
        ItemStack key = inventory.getItem(38);
        if(key != null) {
            NBTItem keyNBT = new NBTItem(key);
            if(keyNBT.getBoolean("beavercrates.isDefault")) {
                lore.add("§cMissing: CRATE KEY");
            }
        }
        ItemStack crateItem = inventory.getItem(42);
        if(crateItem != null) {
            NBTItem crateNBT = new NBTItem(crateItem);
            if(crateNBT.getBoolean("beavercrates.isDefault")) {
                lore.add("§cMissing: CRATE ITEM");
            }
        }
        im.setLore(lore);
        saveItem.setItemMeta(im);
        inventory.setItem(40, saveItem);
    }

}
