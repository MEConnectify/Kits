package io.github.meconnectify.kits.manager;

import io.github.meconnectify.kits.KitsPlugin;
import io.github.meconnectify.kits.utils.Configs;
import io.github.meconnectify.kits.utils.InventorySerializer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.rmi.AlreadyBoundException;

import static io.github.meconnectify.kits.utils.InventorySerializer.*;
import static org.bukkit.Material.AIR;

public class KitManager {

    private final Configs.Config config;

    public KitManager(KitsPlugin plugin) {
        this.config = plugin.getConfigs().getConfig("kits");
    }

    //Function used to create kits and save them to the config
    public void createKit(String name, int slot, String material, PlayerInventory inventory, int seconds) throws AlreadyBoundException {
        /*
        Checking if 1 variable of the kit exists, if it does it
        throws an exception meaning the kit exists.
         */
        if (config.get("kits." + name + ".slot") != null) throw new AlreadyBoundException();

        //Saving the path instead of writing it every time
        String path = "kits." + name;

        //Setting variables for the kit
        config.set(path + ".slot", slot);
        config.set(path + ".cooldown", seconds);
        config.set(path + ".material", material);
        config.set(path + ".inventory", toBase64(inventory));
        config.set(path + ".armor", itemStackArrayToBase64(inventory.getArmorContents()));

        //Saving the configuration
        config.save();
    }

    //Function to delete a kit from the config
    public void removeKit(String name) throws NullPointerException {
        /*
        Checking if 1 variable of the doesn't kit exists, if it doesn't it
        throws an exception meaning the kit doesn't exist.
         */
        if (config.get("kits." + name + ".slot") == null) throw new NullPointerException();

        //Deleting the kit from the configuration
        config.setAndSave("kits." + name, null);
    }

    public void giveKit(Player player, String kit) throws Exception {
        String path = "kits." + kit;
        Inventory inventory;
        ItemStack[] armor;

        /*
        Attempt to get the itemstack arrays from the config,
        If it fails, It'll throw an exception
         */
        inventory = InventorySerializer.fromBase64(config.get(path + ".inventory"));
        armor = InventorySerializer.itemStackArrayFromBase64(config.get(path + ".armor"));

        /*
        Of course making a loop to do this would be easier,
        But this is more performance friendly and it's not
        a huge task so it won't effect our code length
         */
        ItemStack[] contents = new ItemStack[]{
                new ItemStack(AIR), new ItemStack(AIR), new ItemStack(AIR), new ItemStack(AIR), new ItemStack(AIR), new ItemStack(AIR)
                , new ItemStack(AIR), new ItemStack(AIR), new ItemStack(AIR), new ItemStack(AIR), new ItemStack(AIR), new ItemStack(AIR)
                , new ItemStack(AIR), new ItemStack(AIR), new ItemStack(AIR), new ItemStack(AIR), new ItemStack(AIR), new ItemStack(AIR)
                , new ItemStack(AIR), new ItemStack(AIR), new ItemStack(AIR), new ItemStack(AIR), new ItemStack(AIR), new ItemStack(AIR)
                , new ItemStack(AIR), new ItemStack(AIR), new ItemStack(AIR), new ItemStack(AIR), new ItemStack(AIR), new ItemStack(AIR)
                , new ItemStack(AIR), new ItemStack(AIR), new ItemStack(AIR), new ItemStack(AIR)};

        /*
        Getting the inventory contents and setting them to
        the array that we made, incase one of them is null
        it will not throw when applying to the inventory
        because we already set the empty ones to air
         */
        for (int i = 0; i < inventory.getContents().length; i++) {
            if (inventory.getContents()[i] != null)
                contents[i] = inventory.getContents()[i];
        }

        /*
        We basically apply the items to the inventory now
         */
        player.getInventory().setArmorContents(armor);
        player.getInventory().addItem(contents);

        /*
        Lets not forget setting the player on cooldown
         */
        //TODO: Set player on cooldown
    }

    public int getCooldown(String kit) {
        return config.getInt("kits." + kit + ".cooldown");
    }
}
