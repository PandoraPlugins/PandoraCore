package dev.minecraftplugin.pandoracore.module.modules.PandoraTools;

import org.bukkit.Material;

import java.util.List;

public class ItemData {

    private Material material;
    private String name;
    private List<String> lore;
    private String nbtKey;

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getLore() {
        return lore;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public ItemData(Material material, String name, List<String> lore, String nbtKey) {
        this.material = material;
        this.name = name;
        this.lore = lore;
        this.nbtKey = nbtKey;
    }

}
