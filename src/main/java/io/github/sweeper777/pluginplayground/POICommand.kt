package io.github.sweeper777.pluginplayground

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class POICommand(poiFilePath: String): CommandExecutor {
    val file = POIFile(poiFilePath)

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            if (args.firstOrNull() == "add" && args.size == 2) {
                val success = file.addPOI(sender.uniqueId, POI(args[1], POILocation(sender.location)))
                if (success) {
                    sender.sendMessage("Successfully added POI '${args[1]}'")
                } else {
                    sender.sendMessage("Unable to add POI '${args[1]}'")
                }
            } else if (args.firstOrNull() == "remove" && args.size == 2) {
                val success = file.removePOI(sender.uniqueId, args[1])
                if (success) {
                    sender.sendMessage("Successfully removed POI '${args[1]}'")
                } else {
                    sender.sendMessage("Unable to remove POI '${args[1]}'")
                }
            } else if (args.firstOrNull() == "list" && args.size == 1) {
                val pois = file.getPOI(sender.uniqueId)
                if (pois.isNotEmpty()) {
                    for (poi in pois) {
                        sender.sendMessage("'${poi.name}' at x=${poi.location.x}, y=${poi.location.y}, z=${poi.location.z}, in '${poi.location.world}'")
                    }
                } else {
                    sender.sendMessage("You have no POIs!")
                }
            } else {
                sender.sendMessage("Invalid Arguments!")
            }
        } else {
            sender.sendMessage("Only Players can use this command!")
        }
        return true
    }
}