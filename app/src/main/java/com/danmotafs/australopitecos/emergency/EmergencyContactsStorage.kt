package com.danmotafs.australopitecos.emergency

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

object EmergencyContactsStorage {

    private const val PREFS_NAME = "australopitecos_contacts"
    private const val KEY_CONTACTS = "contacts"

    fun saveContacts(
        context: Context,
        contacts: List<EmergencyContact>
    ) {

        val array = JSONArray()

        contacts.forEach { contact ->

            val obj = JSONObject().apply {
                put("name", contact.name)
                put("phone", contact.phone)
            }

            array.put(obj)
        }

        context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
            .edit()
            .putString(KEY_CONTACTS, array.toString())
            .apply()
    }

    fun getContacts(
        context: Context
    ): MutableList<EmergencyContact> {

        val json =
            context.getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
            )
                .getString(KEY_CONTACTS, "[]")
                ?: "[]"

        val array = JSONArray(json)

        val contacts =
            mutableListOf<EmergencyContact>()

        for (i in 0 until array.length()) {

            val obj = array.getJSONObject(i)

            contacts.add(
                EmergencyContact(
                    name = obj.getString("name"),
                    phone = obj.getString("phone")
                )
            )
        }

        return contacts
    }
}

