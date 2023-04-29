package pl.beavercrates.permissions;

import org.bukkit.permissions.Permission;
import pl.beavercrates.BeaverCrates;

import java.util.HashMap;

public class PermissionManager {

    private final HashMap<String, Permission> permissions = new HashMap<>();

    public PermissionManager() {

    }

    public void registerPermission(String permission, String description) {
        Permission perm = new Permission(permission, description);
        BeaverCrates.getInstance().getServer().getPluginManager().removePermission(perm);
        BeaverCrates.getInstance().getServer().getPluginManager().addPermission(perm);
        permissions.put(permission, perm);
    }

    public Permission getPermission(String permission) {
        return permissions.getOrDefault(permission, null);
    }

}
