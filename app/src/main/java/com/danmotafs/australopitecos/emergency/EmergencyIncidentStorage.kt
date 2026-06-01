package com.danmotafs.australopitecos.emergency

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

object EmergencyIncidentStorage {

    private const val PREFS_NAME = "australopitecos_incidents"
    private const val KEY_INCIDENTS = "incidents"

    fun saveIncident(
        context: Context,
        incident: EmergencyIncident
    ) {
        val prefs = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )

        val existingJson =
            prefs.getString(KEY_INCIDENTS, "[]") ?: "[]"

        val array = JSONArray(existingJson)

        val obj = JSONObject().apply {
            put("timestamp", incident.timestamp)
            put("locationLink", incident.locationLink)
            put("message", incident.message)
        }

        array.put(obj)

        prefs.edit()
            .putString(KEY_INCIDENTS, array.toString())
            .apply()
    }

    fun getIncidents(
        context: Context
    ): List<EmergencyIncident> {

        val prefs = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )

        val json =
            prefs.getString(KEY_INCIDENTS, "[]") ?: "[]"

        val array = JSONArray(json)

        val incidents = mutableListOf<EmergencyIncident>()

        for (i in 0 until array.length()) {

            val obj = array.getJSONObject(i)

            incidents.add(
                EmergencyIncident(
                    timestamp = obj.getString("timestamp"),
                    locationLink = obj.getString("locationLink"),
                    message = obj.getString("message")
                )
            )
        }

        return incidents.reversed()
    }
}

