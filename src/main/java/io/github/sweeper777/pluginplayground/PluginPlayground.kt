package io.github.sweeper777.pluginplayground

import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

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
    }
}