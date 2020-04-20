package io.github.meconnectify.kits.utils;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.bukkit.configuration.file.YamlConfiguration.loadConfiguration;

public class Configs {

    private final Map<String, Config> configs = new HashMap<>();
    private final Plugin main;

    public Configs(Plugin main) {
        this.main = main;
    }

    public Config getConfig(String name) {
        if (!configs.containsKey(name + ".yml"))
            configs.put(name + ".yml", new Config(name + ".yml"));

        return configs.get(name + ".yml");
    }

    public class Config {

        private String name;
        private File file;
        private YamlConfiguration config;

        Config(String name) {
            this.name = name;
        }

        public Config save() {
            if (this.config == null || this.file == null)
                return this;

            try {
                if (config.getConfigurationSection("").getKeys(true).size() != 0)
                    config.save(this.file);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            return this;
        }


        public Config saveDefaultConfig() {
            file = new File(main.getDataFolder(), this.name);
            main.saveResource(this.name, false);
            return this;
        }

        public Set<String> getSection(String section) {
            return get().getConfigurationSection(section).getKeys(false);
        }

        public Config reload() {
            if (file == null)
                this.file = new File(main.getDataFolder(), this.name);

            this.config = loadConfiguration(file);

            Reader defConfigStream;
            try {
                defConfigStream = new InputStreamReader(main.getResource(this.name), UTF_8);

                YamlConfiguration defConfig = loadConfiguration(defConfigStream);
                this.config.setDefaults(defConfig);
            } catch (NullPointerException ignored) {
            }

            return this;
        }

        public Config copyDefaults(boolean force) {
            get().options().copyDefaults(force);
            return this;
        }

        public <T> Config set(String key, T value) {
            get().set(key, value);
            return this;
        }

        public <T> Config setAndSave(String key, T value) {
            get().set(key, value);
            save();
            return this;
        }

        public YamlConfiguration get() {
            if (this.config == null)
                reload();

            return this.config;
        }

        public String get(String key) {
            return get().getString(key);
        }

        public Integer getInt(String key) {
            return get().getInt(key);
        }

        public Double getDouble(String key) {
            return get().getDouble(key);
        }

        public boolean getBoolean(String key) {
            return get().getBoolean(key);
        }

        public List<String> getStringList(String key) {
            return new ArrayList<>(get().getStringList(key));
        }

        public Material getMaterial(String key) {
            return Material.getMaterial(config.getString(key));
        }

        public <T> T getT(String key) {
            return (T) get().get(key);
        }

        public String text() {
            return config.toString();
        }
    }
}
