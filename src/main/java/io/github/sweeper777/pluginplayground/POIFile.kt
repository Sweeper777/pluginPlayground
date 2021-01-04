package io.github.sweeper777.pluginplayground

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.*
import java.lang.Exception
import java.nio.file.Files
import java.nio.file.Paths
import java.util.UUID


class POIFile(private val path: String) {
    private val poisByUUID: HashMap<String, ArrayList<POI>> = try {
        val reader = InputStreamReader(FileInputStream(path))
        val typeToken = object : TypeToken<HashMap<String, ArrayList<POI>>>(){}
        Gson().fromJson(reader, typeToken.type)
    } catch (ex: Exception) {
        HashMap()
    }

    fun getPOI(uuid: UUID): List<POI> = poisByUUID[uuid.toString()] ?: emptyList()

    fun addPOI(uuid: UUID, poi: POI): Boolean {
        val poisOfUUID = poisByUUID[uuid.toString()]
        return if (poisOfUUID == null) {
            poisByUUID[uuid.toString()] = ArrayList(mutableListOf(poi))
            true
        } else {
            if (poisOfUUID.any { it.name == poi.name }) {
                false
            } else {
                poisOfUUID.add(poi)
                true
            }
        }
    }

    fun removePOI(uuid: UUID, name: String): Boolean {
        val poisOfUUID = poisByUUID[uuid.toString()]
        return poisOfUUID?.removeIf { it.name == name } ?: false
    }

    fun commitChanges() {
        val typeToken = object : TypeToken<HashMap<String, ArrayList<POI>>>(){}
        println(poisByUUID)
        val jsonString = Gson().toJson(poisByUUID, typeToken.type)
        Files.writeString(Paths.get(path), jsonString)
    }
}