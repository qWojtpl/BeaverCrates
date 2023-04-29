package pl.beavercrates.crates;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

@Getter
public class Crate {

    private final String name;
    private final HashMap<Integer, ItemStack> items;
    private final ItemStack key;

    public Crate(String name, HashMap<Integer, ItemStack> items, ItemStack key) {
        this.name = name;
        this.items = items;
        this.key = key;
    }

}
