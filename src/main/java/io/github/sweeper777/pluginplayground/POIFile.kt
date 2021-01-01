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

}