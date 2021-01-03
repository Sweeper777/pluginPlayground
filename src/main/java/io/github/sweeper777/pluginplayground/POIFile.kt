package io.github.sweeper777.pluginplayground

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.bukkit.Bukkit
import java.io.*
import java.lang.Exception
import java.util.UUID
import java.util.logging.Level


class POIFile(private val path: String) {
    private val poisByUUID: MutableMap<String, MutableList<POI>>

    init {
        poisByUUID = try {
            val reader = InputStreamReader(FileInputStream(path))
            val typeToken = object : TypeToken<HashMap<String, ArrayList<POI>>>(){}
            Gson().fromJson<HashMap<String, MutableList<POI>>>(reader, typeToken.type)
        } catch (ex: Exception) {
            HashMap()
        }
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

    fun removePOI(uuid: UUID, name: String): Boolean {
        val poisOfUUID = poisByUUID[uuid.toString()]
        return poisOfUUID?.removeIf { it.name == name } ?: false
    }

    fun commitChanges() {
        val typeToken = object : TypeToken<HashMap<String, ArrayList<POI>>>(){}
        Gson().toJson(poisByUUID, typeToken.type, OutputStreamWriter(FileOutputStream(path)))
    }
}