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

    private fun createGuiItem(material: Material, name: String, vararg lore: String): ItemStack {
        val item = ItemStack(material, 1)
        val meta = item.itemMeta

        meta?.setDisplayName(name)

        meta?.lore = lore.toList()
        item.itemMeta = meta
        return item
    }

}