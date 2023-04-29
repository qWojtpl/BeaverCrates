package pl.beavercrates.editor;

import lombok.Getter;
import org.bukkit.entity.Player;
import pl.beavercrates.BeaverCrates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
public class EditorManager {

    private final BeaverCrates plugin = BeaverCrates.getInstance();
    private final List<Editor> editors = new ArrayList<>();
    private final HashMap<Player, Editor> editorMap = new HashMap<>();

    public void createEditor(Player player, String crateName) {
        editors.add(new Editor(player, crateName));
    }

}
