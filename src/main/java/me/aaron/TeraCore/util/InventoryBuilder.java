package me.aaron.TeraCore.util;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryBuilder {

    private final Inventory inventory;

    public InventoryBuilder(int size, String title) {
        this.inventory = Bukkit.createInventory(null, size, title);
    }

    public InventoryBuilder(InventoryType type, String title) {
        this.inventory = Bukkit.createInventory(null, type, title);
    }

    public InventoryBuilder setItem(int slot, ItemStack item) {
        inventory.setItem(slot, item);
        return this;
    }

    public InventoryBuilder fill(ItemStack item) {
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, item);
        }
        return this;
    }

    public InventoryBuilder fillEmpty(ItemStack item) {
        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null || inventory.getItem(i).getType() == Material.AIR) {
                inventory.setItem(i, item);
            }
        }
        return this;
    }

    public Inventory build() {
        return inventory;
    }
}