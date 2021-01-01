package io.github.sweeper777.pluginplayground

data class POILocation(val x: Int, val y: Int, val z: Int, val world: String)
data class POI(val name: String, val location: POILocation)