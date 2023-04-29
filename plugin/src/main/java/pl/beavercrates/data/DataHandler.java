package pl.beavercrates.data;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import pl.beavercrates.BeaverCrates;
import pl.beavercrates.crates.Crate;
import pl.beavercrates.crates.CrateItem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataHandler {

    private final BeaverCrates plugin = BeaverCrates.getInstance();

    public void loadConfig() {
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        if(!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }
        loadCrates();
    }

    public void loadCrates() {
        plugin.getCrateManager().getCrates().clear();
        File crateFile = getCrateFile();
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(crateFile);
        ConfigurationSection section = yml.getConfigurationSection("crates");
        if(section == null) return;
        for(String name : section.getKeys(false)) {
            ItemStack crateItem = null;
            if(yml.getConfigurationSection("crates." + name + ".crate") != null) {
                crateItem = ItemStack.deserialize(
                        yml.getConfigurationSection("crates." + name + ".crate").getValues(true));
            }
            ItemStack key = null;
            if(yml.getConfigurationSection("crates." + name + ".key") != null) {
                key = ItemStack.deserialize(
                        yml.getConfigurationSection("crates." + name + ".key").getValues(true));
            }
            ConfigurationSection section1 = yml.getConfigurationSection("crates." + name + ".items");
            if(section1 == null) continue;
            List<CrateItem> items = new ArrayList<>();
            for(String itemID : section1.getKeys(false)) {
                if(yml.getConfigurationSection("crates." + name + ".items.item.") == null) continue;
                items.add(new CrateItem(ItemStack.deserialize(yml.getConfigurationSection("crates." + name + ".items.item." + itemID).getValues(true)), 0, 0));
            }
            Crate crate = new Crate(name, items, crateItem, key);
            plugin.getCrateManager().addCrate(crate);
        }
    }

    public void saveCrate(Crate crate) {
        File crateFile = getCrateFile();
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(crateFile);
        String path = "crates." + crate.getName();
        yml.set(path, null);
        yml.set(path + ".key", crate.getKey().serialize());
        yml.set(path + ".crate", crate.getCrate().serialize());
        List<CrateItem> items = crate.getItems();
        int i = 0;
        for(CrateItem ci : items) {
            yml.set(path + ".items." + i + ".item", ci.getItemStack().serialize());
            i++;
        }
        try {
            yml.save(crateFile);
        } catch(IOException e) {
            plugin.getLogger().severe("Cannot save crate: " + crate.getName());
        }
    }

    public File getCrateFile() {
        File crateFile = new File(plugin.getDataFolder(), "crates.yml");
        if(!crateFile.exists()) {
            plugin.saveResource("crates.yml", false);
        }
        return crateFile;
    }

}
