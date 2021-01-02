package io.github.sweeper777.pluginplayground

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.UUID


class POIFile(private val path: String) {
    private val poisByUUID: MutableMap<String, MutableList<POI>>

    init {
        val reader = InputStreamReader(FileInputStream(path))
        val typeToken = object : TypeToken<HashMap<String, ArrayList<POI>>>(){}
        poisByUUID = Gson().fromJson(reader, typeToken.type)
    }

    fun getPOI(uuid: UUID): List<POI> = poisByUUID[uuid.toString()] ?: emptyList()

    fun addPOI(uuid: UUID, poi: POI): Boolean {
        val poisOfUUID = poisByUUID[uuid.toString()]
        if (poisOfUUID == null) {
            poisByUUID[uuid.toString()] = ArrayList(mutableListOf(poi))
            return true
        } else {
            if (poisOfUUID.any { it.name == poi.name }) {
                return false
            } else {
                poisOfUUID.add(poi)
                return true
            }
        }
    }

}