package io.github.sweeper777.pluginplayground

import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.text.DecimalFormat

class PluginPlayground : JavaPlugin() {
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
    }
}