package pl.beavercrates.util;

import org.bukkit.entity.Player;
import pl.beavercrates.BeaverCrates;

import javax.annotation.Nullable;

public class PlayerUtil {

    @Nullable
    public static Player getPlayer(String name) {
        for(Player p : BeaverCrates.getInstance().getServer().getOnlinePlayers()) {
            if(p.getName().equals(name)) return p;
        }
        return null;
    }

}
