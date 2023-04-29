package pl.beavercrates.crates;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

@Getter
public class Crate {

    private final String name;
    private final List<ItemStack> items;
    private final ItemStack key;

    public Crate(String name, List<ItemStack> items, ItemStack key) {
        this.name = name;
        this.items = items;
        this.key = key;
    }

}
