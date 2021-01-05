package io.github.sweeper777.pluginplayground

import org.bukkit.entity.Player

import org.bukkit.Material

import org.bukkit.inventory.ItemStack

import org.bukkit.entity.HumanEntity

import java.util.Arrays

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.*

import org.bukkit.inventory.Inventory


class MyGUI : Listener {
    private val inv: Inventory = Bukkit.createInventory(null, 9, "My GUI")

    init {
        initializeItems()
    }

    private fun initializeItems() {
        inv.addItem(
            createGuiItem(
                Material.DIAMOND,
                "Item 1",
                "§aFirst line of the lore",
                "§bSecond line of the lore"
            )
        )
        inv.addItem(
            createGuiItem(
                Material.IRON_INGOT,
                "§bItem 2",
                "§aFirst line of the lore",
                "§bSecond line of the lore"
            )
        )
    }

    private fun createGuiItem(material: Material, name: String, vararg lore: String): ItemStack {
        val item = ItemStack(material, 1)
        val meta = item.itemMeta

        meta?.setDisplayName(name)

        meta?.lore = lore.toList()
        item.itemMeta = meta
        return item
    }

    fun openInventory(ent: HumanEntity) {
        ent.openInventory(inv)
    }

    @EventHandler
    fun onInventoryClick(e: InventoryClickEvent) {
        val p = e.whoClicked as Player
        if (e.inventory === inv) {
            e.isCancelled = true
        }

        p.sendMessage("Click Type: ${e.click}")
        p.sendMessage("Action Type: ${e.action}")
    }

    @EventHandler
    fun onInventoryClick(e: InventoryDragEvent) {
        if (e.inventory === inv) {
            e.isCancelled = true
        }
    }
}