package dev.minecraftplugin.pandoracore.configuration;

import com.google.gson.Gson;
import dev.minecraftplugin.pandoracore.PandoraCore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

@SuppressWarnings("all")
public class Config<T> {
    private final Gson gson;
    private final File configurationFile;
    private T configuration;

    public Config(String name, Gson gson, PandoraCore plugin, T defaults) {
        this.gson = gson;
        configurationFile = new File(plugin.getDataFolder(), name.endsWith(".json") ? name : name + ".json");
        try {
            if (!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdirs();
            if (!configurationFile.exists()) {
                configurationFile.mkdirs();
                configurationFile.createNewFile();
                configuration = defaults;
                saveConfig();
            } else
                configuration = (T) gson.fromJson(new FileReader(configurationFile), defaults.getClass());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public File getConfigurationFile() {
        return configurationFile;
    }

    public T getConfiguration() {
        return configuration;
    }

    public void saveConfig() {
        try {
            final String json = gson.toJson(configuration);
            configurationFile.delete();
            Files.write(configurationFile.toPath(), json.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadConfig() {
        try {
            configuration = (T) gson.fromJson(new FileReader(configurationFile), configuration.getClass());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
