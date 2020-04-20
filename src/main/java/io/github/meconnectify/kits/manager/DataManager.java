package io.github.meconnectify.kits.manager;

import io.github.meconnectify.kits.KitsPlugin;
import io.github.meconnectify.kits.utils.Configs;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class DataManager {

    private final Configs.Config config;
    private final KitManager kitManager;

    public DataManager(KitsPlugin plugin) {
        this.config = plugin.getConfigs().getConfig("data");
        this.kitManager = plugin.getKitManager();
    }

    //Set the player on kit cooldown
    public void setOnCooldown(UUID uuid, String kit) {
        config.set(uuid.toString() + "." + kit, new Date());
    }

    public boolean isOnCooldown(UUID uuid, String kit) {
        /*
        Set the kit in a variable and check
        if it's null or not
         */
        Date date = config.getT(uuid.toString() + "." + kit);
        if (date == null) return false;

        /*
        Now we make a calendar and add
        the kit cooldown time to it
         */
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(config.getT(uuid.toString() + "." + kit));
        calendar.add(Calendar.SECOND, kitManager.getCooldown(kit));

        //Comparing the time we made to the current time
        return calendar.getTime().after(new Date());
    }
}
