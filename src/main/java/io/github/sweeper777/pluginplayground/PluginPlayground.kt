package io.github.sweeper777.pluginplayground

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.plugin.java.JavaPlugin
import java.text.DecimalFormat
import java.util.logging.Level

class PluginPlayground : JavaPlugin() {
    var poiCommand: POICommand? = null
    val myGUI = MyGUI()

    override fun onEnable() {
        getCommand("whereis")?.setExecutor { sender, _, _, args ->
            val playerToBeLocated = args.firstOrNull()?.let {
                server.getPlayer(it)
            } ?: sender as? Player

            if (playerToBeLocated != null) {
                val message = "${playerToBeLocated.displayName} is at " +
                        "x=${playerToBeLocated.location.blockX}, " +
                        "y=${playerToBeLocated.location.blockY}, " +
                        "z=${playerToBeLocated.location.blockZ}" +
                        (playerToBeLocated.location.world?.name?.let { worldName ->
                            ", in '${worldName}'"
                        } ?: "")
                sender.sendMessage(message)
            } else {
                sender.sendMessage("Invalid Player Specified")
            }
            true
        }

        getCommand("slimechunk")?.setExecutor { sender, _, _, _ ->
            if (sender is Player) {
                if (sender.location.chunk.isSlimeChunk) {
                    sender.sendMessage("You are in a slime chunk!")
                } else {
                    sender.sendMessage("You are NOT in a slime chunk!")
                }
            } else {
                sender.sendMessage("You must be a player to use this command!")
            }
            true
        }

        getCommand("health")?.setExecutor { sender, _, _, args ->
            val player = args.firstOrNull()?.let {
                server.getPlayer(it)
            } ?: sender as? Player

            if (player != null) {
                val formatter = DecimalFormat.getInstance()
                val currentHealth = formatter.format(player.health)
                val maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value
                    ?.let {
                    formatter.format(it)
                } ?: "unknown"
                val message = "${player.displayName}'s health: " +
                        "$currentHealth/$maxHealth"
                sender.sendMessage(message)
            } else {
                sender.sendMessage("Invalid Player Specified")
            }
            true
        }

        getCommand("shutdownin")?.setExecutor(ShutdownCommand(this))
        getCommand("shutdownin")?.setTabCompleter { _, _, _, _ -> emptyList<String>() }

        getCommand("findentity")?.setExecutor { sender, _, _, args ->
            if (sender is Player) {
                val world = sender.location.world
                val entityToFind = args.firstOrNull()
                val entityType = EntityType.values().firstOrNull { it.name.equals(entityToFind, ignoreCase = true) }
                if (world == null || entityType == null) {
                    sender.sendMessage("Can't find any entities!")
                } else if (!entityType.isAlive) {
                    sender.sendMessage("Finding non-alive entities is not allowed!")
                } else {
                    val entitiesFound = world.getEntitiesByClasses(entityType.entityClass)
                    entitiesFound.forEach {
                        sender.sendMessage("${it.name} at x=${it.location.blockX}, y=${it.location.blockY}, z=${it.location.blockZ}")
                    }
                }
            } else {
                sender.sendMessage("You must be a player to use this command!")
            }
            true
        }

        getCommand("findentity")?.tabCompleter =
            MultipleChoiceTabComplete(EntityType.values().filter { it.isAlive }.map { it.name })

        logger.log(Level.INFO, "Reading POI File")
        val poi = POICommand("poi.json")
        poiCommand = poi
        getCommand("poi")?.setExecutor(poi)
        getCommand("poi")?.tabCompleter = MultipleChoiceTabComplete(listOf("add", "remove", "list"))

        server.addRecipe(
            ShapedRecipe(NamespacedKey(this, "elytra"), ItemStack(Material.ELYTRA))
                .shape("n-n", "n n", "n n")
                .setIngredient('n', Material.NETHERITE_INGOT)
                .setIngredient('-', Material.END_ROD)
        )

        getCommand("gui")?.setExecutor { sender, _, _, _ ->
            if (sender is Player) {
                myGUI.openInventory(sender)
            } else {
                sender.sendMessage("Only players can use this command!")
            }
            true
        }

        server.pluginManager.registerEvents(myGUI, this)
        server.pluginManager.registerEvents(object : Listener {
            @EventHandler
            fun onPlayerJoin(e: PlayerJoinEvent) {
                e.player.sendMessage("Welcome ${e.player.name} to the server!")
            }
        }, this)
    }

    override fun onDisable() {
        super.onDisable()
        logger.log(Level.INFO, "Saving POI changes...")
        poiCommand?.file?.commitChanges()
    }
}