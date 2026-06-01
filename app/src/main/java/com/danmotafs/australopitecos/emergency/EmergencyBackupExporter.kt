package com.danmotafs.australopitecos.emergency

import android.content.Context
import android.util.Base64
import java.io.File

object EmergencyBackupExporter {

    fun exportBackup(
        context: Context
    ): File {

        val incidents = EmergencyIncidentStorage.getIncidents(context)

        val rawContent = buildString {
            appendLine("AUSTRALOPITECOS_BACKUP_V1")
            appendLine("TOTAL_INCIDENTS=${incidents.size}")
            appendLine()

            incidents.forEachIndexed { index, incident ->
                appendLine("INCIDENT_${index + 1}")
                appendLine("TIMESTAMP=${incident.timestamp}")
                appendLine("LOCATION=${incident.locationLink}")
                appendLine("MESSAGE=${incident.message.replace("\n", "\\n")}")
                appendLine("---")
            }
        }

        val encodedContent = Base64.encodeToString(
            rawContent.toByteArray(Charsets.UTF_8),
            Base64.NO_WRAP
        )

        val file = File(
            context.cacheDir,
            "australopitecos_backup.enc.txt"
        )

        file.writeText(encodedContent)

        return file
    }
}

