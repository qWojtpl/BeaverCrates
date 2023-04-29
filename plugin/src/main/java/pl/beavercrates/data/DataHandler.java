package pl.beavercrates.data;

import pl.beavercrates.BeaverCrates;

public class DataHandler {

    private final BeaverCrates plugin = BeaverCrates.getInstance();

    public void loadConfig() {
        loadCrates();
    }

    public void loadCrates() {
        plugin.getCrateManager().getCrates().clear();
    }

}
