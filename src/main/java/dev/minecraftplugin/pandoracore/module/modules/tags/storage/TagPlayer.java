package dev.minecraftplugin.pandoracore.module.modules.tags.storage;

import java.util.Set;
import java.util.UUID;

public class TagPlayer {
    public UUID player;
    public Tag enabledTag;
    public Set<Tag> customTags;
    public int availableCustomTags;
}
