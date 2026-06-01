package com.danmotafs.australopitecos.emergency

import android.content.Context
import android.content.Intent
import android.net.Uri

object WhatsAppAlertSender {

    fun openWhatsApp(
        context: Context,
        phone: String,
        message: String
    ) {
        val cleanPhone = phone
            .replace(" ", "")
            .replace("-", "")
            .replace("(", "")
            .replace(")", "")
            .replace("+", "")

        val encodedMessage = Uri.encode(message)

        val uri = Uri.parse(
            "https://wa.me/55$cleanPhone?text=$encodedMessage"
        )

        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        context.startActivity(intent)
    }
}

