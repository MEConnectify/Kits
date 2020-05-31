package io.github.meconnectify.kits.command;

import io.github.meconnectify.kits.KitsPlugin;
import io.github.meconnectify.kits.manager.KitManager;
import io.github.meconnectify.kits.utils.Configs;
import io.github.meconnectify.kits.utils.ItemBuilder;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.rmi.AlreadyBoundException;

import static io.github.meconnectify.kits.utils.Constants.color;
import static java.util.Arrays.asList;
import static org.apache.commons.lang.WordUtils.capitalize;

public class KitCommand implements CommandExecutor {

    private final Configs.Config config;
    private final KitManager kitManager;

    public KitCommand(KitsPlugin plugin) {
        config = plugin.getConfigs().getConfig("kits");
        kitManager = plugin.getKitManager();
    }

    @Override public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        //Check if the sender is not a player and cancel
        if (!(sender instanceof Player)) return true;
        //Cast CommandSender to player
        Player player = (Player) sender;

        if (args.length == 0) {
            //Create the inventory to add all the kits to
            Inventory inventory = Bukkit.createInventory(null, 27, color("&cKit GUI"));

            /*
            If the config is empty or there's a typo
            the code will throw an error, so we check
            for the error and cancel the operation
             */
            try {
                //Apply all the kits to the inventory
                config.getSection("kits").forEach(kit -> {
                    String path = "kits." + kit;
                    inventory.setItem(config.getInt(path + ".slot"),
                            ItemBuilder.of(
                                    config.getMaterial(path + ".material"))
                                    .name("&c" + capitalize(kit))
                                    .lore(asList("", "&c&lLeftClick &r&7to claim")).build());
                });
                player.openInventory(inventory);
            } catch (NullPointerException ex) {
                player.sendMessage(color("&cYour kits are not setup correctly."));
            }
            return true;
        }

        /*
        Check if the player has permission or not before
        letting them execute any editing commands
         */
        if (!player.hasPermission("kits.admin")) {
            player.sendMessage(color("&cNo Perms."));
            return true;
        }

        if (args[0].equalsIgnoreCase("create")) {
            if (args.length < 5) {
                player.sendMessage(color("&cUsage: /kit create <name> <slot> <material> <seconds>"));
                return true;
            }
            Material material = Material.getMaterial(args[3]);

            if (material == null) {
                player.sendMessage(color("&cYou've entered an invalid material."));
                return true;
            }

            if (!NumberUtils.isNumber(args[2]) || !NumberUtils.isNumber(args[4])) {
                player.sendMessage(color("&cYou've entered an invalid number."));
                return true;
            }

            /*
            Attempt to create the kit, Throws an exception
            when the kit exists.
             */
            try {
                kitManager.createKit(args[1].toLowerCase(), NumberUtils.toInt(args[2]), material.name(), player.getInventory(), NumberUtils.toInt(args[4]));
                player.sendMessage(color("&cSuccessfully created kit."));
            } catch (AlreadyBoundException ex) {
                player.sendMessage(color("&cThis kit already exists"));
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("remove")) {
            if (args.length < 2) {
                player.sendMessage(color("&cUsage: /kit remove <kit>"));
                return true;
            }



            /*
            Attempt to remove the kit, Throws an exception
            when the kit does not exist.
             */
            try {
                kitManager.removeKit(args[1].toLowerCase());
                player.sendMessage(color("&cSuccessfully removed kit."));
            } catch (NullPointerException ex) {
                player.sendMessage(color("&cThis kit does not exist."));
            }
            return true;
        }
        return true;
    }
}
