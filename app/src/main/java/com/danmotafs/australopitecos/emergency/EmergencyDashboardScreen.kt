package com.danmotafs.australopitecos.emergency

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val DeepForestGreen = Color(0xFF0B5D47)
private val AlertOrange = Color(0xFFF39C12)
private val EmergencyRed = Color(0xFFE74C3C)
private val CardDark = Color(0xFF18201D)
private val TextLight = Color(0xFFF5F7FA)
private val TextMuted = Color(0xFFB8C4BE)

@Composable
fun EmergencyDashboardScreen(
    monitorActive: Boolean,
    alertSent: Boolean
) {
    val context = LocalContext.current
    val incidents = EmergencyIncidentStorage.getIncidents(context)
    val contacts = EmergencyContactsStorage.getContacts(context)

    val totalIncidents = incidents.size
    val totalContacts = contacts.size
    val lastIncident = incidents.firstOrNull()

    val statusTitle: String
    val statusDetail: String
    val statusColor: Color

    if (alertSent) {
        statusTitle = "🔴 ALERTA ENVIADO"
        statusDetail = "Último protocolo de emergência acionado"
        statusColor = EmergencyRed
    } else if (monitorActive) {
        statusTitle = "🟢 PROTEGIDO"
        statusDetail = "Monitoramento ativo aguardando palavra-gatilho"
        statusColor = DeepForestGreen
    } else {
        statusTitle = "🟠 EM ESPERA"
        statusDetail = "Monitoramento pausado"
        statusColor = AlertOrange
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Painel de Segurança",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DashboardCard(
                title = "INCIDENTES",
                value = totalIncidents.toString(),
                subtitle = "registrados",
                modifier = Modifier.weight(1f)
            )

            DashboardCard(
                title = "CONTATOS",
                value = totalContacts.toString(),
                subtitle = "cadastrados",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DashboardCard(
                title = "HISTÓRICO",
                value = "ON",
                subtitle = "local ativo",
                modifier = Modifier.weight(1f)
            )

            DashboardCard(
                title = "BACKUP",
                value = "OK",
                subtitle = "protegido",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        StatusCard(
            title = "Status operacional",
            status = statusTitle,
            detail = statusDetail,
            color = statusColor
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Último incidente",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (lastIncident != null) {
            Text(
                text = lastIncident.timestamp,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = lastIncident.locationLink,
                style = MaterialTheme.typography.bodySmall
            )
        } else {
            Text(
                text = "Nenhum incidente registrado",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Divider()
    }
}

@Composable
private fun DashboardCard(
    title: String,
    value: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(
                color = CardDark,
                shape = RoundedCornerShape(18.dp)
            )
            .padding(16.dp)
    ) {
        Text(
            text = title,
            color = TextMuted,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = value,
            color = TextLight,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = subtitle,
            color = TextMuted,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun StatusCard(
    title: String,
    status: String,
    detail: String,
    color: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = color,
                shape = RoundedCornerShape(18.dp)
            )
            .padding(16.dp)
    ) {
        Text(
            text = title,
            color = TextLight,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = status,
            color = TextLight,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = detail,
            color = TextLight,
            style = MaterialTheme.typography.bodySmall
        )
    }
}
