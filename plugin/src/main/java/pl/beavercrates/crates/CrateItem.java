package pl.beavercrates.crates;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Getter
public class CrateItem {

    private final ItemStack itemStack;
    private final int numberMin;
    private final int numberMax;

    public CrateItem(ItemStack itemStack, int numberMin, int numberMax) {
        this.itemStack = itemStack;
        this.numberMin = numberMin;
        this.numberMax = numberMax;
    }

}
