package com.danmotafs.australopitecos.emergency

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider

@Composable
fun EmergencyHistoryScreen() {
    val context = LocalContext.current
    val incidents = EmergencyIncidentStorage.getIncidents(context)

    Column {
        Text(
            text = "Histórico de incidentes",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                val csvFile = EmergencyHistoryExporter.exportCsv(context)

                val uri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.provider",
                    csvFile
                )

                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/csv"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }

                val chooser = Intent.createChooser(
                    shareIntent,
                    "Compartilhar histórico Australopitecos"
                )

                context.startActivity(chooser)
            }
        ) {
            Text("Exportar CSV")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                val backupFile = EmergencyBackupExporter.exportBackup(context)

                val uri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.provider",
                    backupFile
                )

                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }

                val chooser = Intent.createChooser(
                    shareIntent,
                    "Compartilhar backup protegido Australopitecos"
                )

                context.startActivity(chooser)
            }
        ) {
            Text("Exportar backup protegido")
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (incidents.isEmpty()) {
            Text(
                text = "Nenhum incidente registrado",
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            incidents.forEach { incident ->
                Text(
                    text = incident.timestamp,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = incident.locationLink,
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(8.dp))

                Divider()

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}