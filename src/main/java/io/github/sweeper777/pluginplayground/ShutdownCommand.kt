package io.github.sweeper777.pluginplayground

import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

class ShutdownCommand(private val plugin: JavaPlugin): CommandExecutor {
    var countdown: CountDownTask? = null

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (!sender.isOp) {
            sender.sendMessage("Only Server Operators can use this command!")
            return true
        }

        val cd = this.countdown
        if (cd != null) {
            sender.sendMessage("Server is already shutting down, in ${cd.current} seconds!")
            return true
        }

        val delay = args.firstOrNull()?.toLongOrNull() ?: 60

        val scheduler = Bukkit.getScheduler()
        val newCountdown = CountDownTask(delay, plugin.server)
        countdown = newCountdown
        scheduler.scheduleSyncRepeatingTask(plugin, newCountdown, 0, 20)
        return true
    }
}

class CountDownTask(val start: Long, private val server: Server): Runnable {
    var current: Long = start

    override fun run() {
        if (current < 0L) {
            return
        }
        if (current == 0L) {
            server.dispatchCommand(server.consoleSender, "stop")
            current--
            return
        }
        if (current == start || current == 60L || current == 30L || current <= 10L) {
            server.broadcastMessage("Server shutting down in $current seconds... Please be prepared!")
        }
        current--
    }
}