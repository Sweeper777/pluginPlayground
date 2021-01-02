package io.github.sweeper777.pluginplayground

import org.bukkit.Location

data class POILocation(val x: Int, val y: Int, val z: Int, val world: String) {
    constructor(location: Location) : this(
        location.blockX, location.blockY, location.blockZ, location.world?.name ?: "unknown"
    )
}
data class POI(val name: String, val location: POILocation)