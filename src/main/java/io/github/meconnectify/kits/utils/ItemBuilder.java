package io.github.meconnectify.kits.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static io.github.meconnectify.kits.utils.Constants.color;

public class ItemBuilder {

    private final Material material;
    private ItemMeta meta;

    private ItemBuilder(Material type) {
        this.material = type;
    }

    public static ItemBuilder of(Material type) {
        return new ItemBuilder(type);
    }

    public ItemBuilder name(String name) {
        if (meta == null) meta = new ItemStack(material).getItemMeta();

        meta.setDisplayName(color(name));
        return this;
    }

    public ItemBuilder lore(List<String> lore) {
        List<String> cache = new ArrayList<>();
        lore.forEach(s -> cache.add(color(s)));

        if (meta == null) meta = new ItemStack(material).getItemMeta();
        meta.setLore(cache);

        return this;
    }

    public ItemStack build() {
        ItemStack stack = new ItemStack(material);
        stack.setItemMeta(meta);
        return stack;
    }
}