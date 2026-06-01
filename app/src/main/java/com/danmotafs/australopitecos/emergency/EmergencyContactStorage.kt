package com.danmotafs.australopitecos.emergency

import android.content.Context

object EmergencyContactStorage {

    private const val PREFS_NAME = "australopitecos_emergency_contacts"
    private const val KEY_NAME = "contact_name"
    private const val KEY_PHONE = "contact_phone"

    fun saveContact(context: Context, contact: EmergencyContact) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        prefs.edit()
            .putString(KEY_NAME, contact.name)
            .putString(KEY_PHONE, contact.phone)
            .apply()
    }

    fun getContact(context: Context): EmergencyContact? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        val name = prefs.getString(KEY_NAME, null)
        val phone = prefs.getString(KEY_PHONE, null)

        return if (!name.isNullOrBlank() && !phone.isNullOrBlank()) {
            EmergencyContact(name = name, phone = phone)
        } else {
            null
        }
    }
}

