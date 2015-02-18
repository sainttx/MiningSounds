package me.sainttx.miningsounds;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.EnumMap;
import java.util.Map;

/**
 * Created by Matthew on 18/02/2015.
 */
public class MiningSoundsPlugin extends JavaPlugin implements Listener {

    /*
     * A map which stores all the materials and their respective sound
     */
    private Map<Material, Sound> sounds = new EnumMap<Material, Sound>(Material.class);

    @Override
    public void onEnable() {
        saveDefaultConfig();
        fillSoundMap();

        getServer().getPluginManager().registerEvents(this, this);
    }

    /**
     * Clears and fills the sound map with new sounds
     * loaded from config for materials that will be broken
     */
    public void fillSoundMap() {
        sounds.clear();

        for (String mat : getConfig().getKeys(false)) {
            Material material = Material.valueOf(mat.toUpperCase());
            Sound sound = Sound.valueOf(getConfig().getString(mat));

            if (material == null) {
                getLogger().warning("Material definition \"" + mat + "\" is invalid and was skipped.");
            } else if (sound == null) {
                getLogger().warning("Sound definition for material \"" + mat + "\" is invalid and was skipped.");
            } else {
                sounds.put(material, sound);
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block broken = event.getBlock();

        if (sounds.containsKey(broken.getType())) {
            Sound toPlay = sounds.get(broken.getType());
            player.playSound(broken.getLocation(), toPlay, 1F, 1F);
        }
    }
}
