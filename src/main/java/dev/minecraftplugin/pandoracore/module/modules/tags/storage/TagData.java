package dev.minecraftplugin.pandoracore.module.modules.tags.storage;

import com.google.gson.annotations.SerializedName;

import java.util.HashSet;
import java.util.Set;

public class TagData {
    @SerializedName("Tags")
    Set<Tag> globalTags;

    public TagData() {
        globalTags = new HashSet<>();
    }
}
