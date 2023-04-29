package pl.beavercrates;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import pl.beavercrates.commands.Commands;
import pl.beavercrates.crates.CrateManager;
import pl.beavercrates.data.DataHandler;
import pl.beavercrates.events.Events;
import pl.beavercrates.permissions.PermissionManager;

@Getter
public final class BeaverCrates extends JavaPlugin {

    private static BeaverCrates main;
    private PermissionManager permissionManager;
    private CrateManager crateManager;
    private DataHandler dataHandler;

    @Override
    public void onEnable() {
        main = this;
        this.permissionManager = new PermissionManager();
        this.crateManager = new CrateManager();
        this.dataHandler = new DataHandler();
        dataHandler.loadConfig();
        getServer().getPluginManager().registerEvents(new Events(), this);
        getCommand("beavercrates").setExecutor(new Commands());
        getLogger().info("Loaded!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Bye!");
    }

    public static BeaverCrates getInstance() {
        return main;
    }

}
