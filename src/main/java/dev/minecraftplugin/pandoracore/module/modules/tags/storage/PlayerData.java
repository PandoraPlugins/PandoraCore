package dev.minecraftplugin.pandoracore.module.modules.tags.storage;

import com.google.gson.annotations.SerializedName;

import java.util.HashSet;
import java.util.Set;

public class PlayerData {
    @SerializedName("PlayerData")
    Set<TagPlayer> players;

    public PlayerData() {
        players = new HashSet<>();
    }
}
