package io.github.meconnectify.kits;

import io.github.meconnectify.kits.command.KitCommand;
import io.github.meconnectify.kits.handler.GUIHandler;
import io.github.meconnectify.kits.manager.DataManager;
import io.github.meconnectify.kits.manager.KitManager;
import io.github.meconnectify.kits.utils.Configs;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class KitsPlugin extends JavaPlugin {

    @Getter private Configs configs;
    @Getter private KitManager kitManager;
    @Getter private DataManager dataManager;

    @Override public void onEnable() {
        setupConfigurations();
        kitManager = new KitManager(this);
        dataManager = new DataManager(this);

        getCommand("kit").setExecutor(new KitCommand(this));
        getServer().getPluginManager().registerEvents(new GUIHandler(this), this);
    }

    private void setupConfigurations() {
        configs = new Configs(this);
        String[] files = {"data", "kits"};

        for (String string : files) {
            File file = new File(getDataFolder(), string + ".yml");
            if (!file.exists()) {
                configs.getConfig(string).copyDefaults(true).save();
                return;
            }
            configs.getConfig(string).get();
        }

    }
}
