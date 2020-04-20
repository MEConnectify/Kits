package io.github.meconnectify.kits.handler;

import io.github.meconnectify.kits.KitsPlugin;
import io.github.meconnectify.kits.manager.DataManager;
import io.github.meconnectify.kits.manager.KitManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import static io.github.meconnectify.kits.utils.Constants.color;
import static org.bukkit.ChatColor.stripColor;
import static org.bukkit.Material.AIR;

public class GUIHandler implements Listener {

    private final KitManager kitManager;
    private final DataManager dataManager;

    public GUIHandler(KitsPlugin plugin) {
        this.kitManager = plugin.getKitManager();
        this.dataManager = plugin.getDataManager();
    }

    @EventHandler void onInventoryClick(InventoryClickEvent event) {
        //Check if the clicked inventory or item is null
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == AIR) return;
        if (event.getCurrentItem().getItemMeta().getDisplayName() == null) return;

        if (stripColor(event.getView().getTitle()).equals("Kit GUI")) {
            Player player = (Player) event.getWhoClicked();
            //Make a variable with the kit name
            String kit = stripColor(event.getCurrentItem().getItemMeta().getDisplayName()).toLowerCase();

            //Check if the player has permission to claim this kit
            if (!player.hasPermission("kits." + kit)) {
                player.sendMessage(color("&cYou dont have perms to claim this kit."));
                event.setCancelled(true);
                player.closeInventory();
                return;
            }

            //Check if the player is still on cooldown
            if (dataManager.isOnCooldown(player.getUniqueId(), kit)) {
                player.sendMessage(color("&cYou are still on cooldown."));
                event.setCancelled(true);
                player.closeInventory();
                return;
            }

            //Attempt to give the player the kit
            try {
                kitManager.giveKit(player, kit);
                dataManager.setOnCooldown(player.getUniqueId(), kit);
                player.sendMessage(color("&cSuccessfully received kit"));
            } catch (Exception ex) {
                player.sendMessage(color("&cAn error occurred while giving you the kit."));
            }
            //Cancel the inventory interaction and close the inventory
            event.setCancelled(true);
            player.closeInventory();
        }
    }
}
