package io.github.sweeper777.pluginplayground

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.EntityType

class MultipleChoiceTabComplete(val choices: List<String>) : TabCompleter {

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ) = when {
            args.size > 1 -> emptyList()
            args.isEmpty() -> choices
            else -> choices.filter { it.startsWith(args.first(), ignoreCase = true) }
        }
}