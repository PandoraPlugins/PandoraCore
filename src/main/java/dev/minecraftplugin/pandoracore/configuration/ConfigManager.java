package dev.minecraftplugin.pandoracore.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.minecraftplugin.pandoracore.PandoraCore;

@SuppressWarnings("all")
public class ConfigManager {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private final PandoraCore plugin;

    public ConfigManager(PandoraCore plugin) {
        this.plugin = plugin;
    }

    public <T> Config<T> loadConfig(String name, T defaults) {
        return new Config<T>(name, gson, plugin, defaults);
    }
}


