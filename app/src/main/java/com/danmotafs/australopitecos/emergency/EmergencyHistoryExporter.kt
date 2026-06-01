package com.danmotafs.australopitecos.emergency

import android.content.Context
import java.io.File

object EmergencyHistoryExporter {

    fun exportCsv(
        context: Context
    ): File {

        val incidents =
            EmergencyIncidentStorage.getIncidents(context)

        val file = File(
            context.cacheDir,
            "australopitecos_historico.csv"
        )

        val builder = StringBuilder()

        builder.append(
            "DataHora,Localizacao\n"
        )

        incidents.forEach { incident ->

            builder.append(
                "\"${incident.timestamp}\","
            )

            builder.append(
                "\"${incident.locationLink}\"\n"
            )
        }

        file.writeText(
            builder.toString()
        )

        return file
    }
}

