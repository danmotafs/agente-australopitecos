package com.danmotafs.australopitecos.emergency

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object EmergencyMessageBuilder {

    fun build(
        contact: EmergencyContact?,
        locationLink: String
    ): String {

        val timestamp = SimpleDateFormat(
            "dd/MM/yyyy HH:mm:ss",
            Locale.getDefault()
        ).format(Date())

        return """
🚨 ALERTA AUSTRALOPITECOS

Possível situação de risco detectada.

Data/Hora:
$timestamp

Contato:
${contact?.name ?: "Não cadastrado"}

Telefone:
${contact?.phone ?: "Não cadastrado"}

Localização:
$locationLink

Mensagem gerada automaticamente pelo protocolo Australopitecos.
        """.trimIndent()
    }
}