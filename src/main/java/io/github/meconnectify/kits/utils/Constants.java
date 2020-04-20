package io.github.meconnectify.kits.utils;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

public class Constants {

    public static String color(String string) {
        return translateAlternateColorCodes('&', string);
    }
}
