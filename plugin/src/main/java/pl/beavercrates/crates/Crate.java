package pl.beavercrates.crates;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
@Setter
public class Crate {

    private String name;
    private List<CrateItem> items;
    private ItemStack crate;
    private ItemStack key;

    public Crate(String name, List<CrateItem> items, ItemStack crate, ItemStack key) {
        this.name = name;
        this.items = items;
        this.crate = crate;
        this.key = key;
    }

}
