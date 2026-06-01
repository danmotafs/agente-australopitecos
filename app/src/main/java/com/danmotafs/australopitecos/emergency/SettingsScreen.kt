package com.danmotafs.australopitecos.emergency

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val CardDark = Color(0xFF18201D)
private val DeepForestGreen = Color(0xFF0B5D47)
private val TextLight = Color(0xFFF5F7FA)
private val TextMuted = Color(0xFFB8C4BE)

private enum class SettingsSection {
    MENU,
    CONTACTS,
    HISTORY,
    EXPORT,
    BACKUP,
    ABOUT
}

@Composable
fun SettingsScreen() {
    var currentSection by remember {
        mutableStateOf(SettingsSection.MENU)
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        when (currentSection) {
            SettingsSection.MENU -> {
                SettingsMenu(
                    onContactsClick = {
                        currentSection = SettingsSection.CONTACTS
                    },
                    onHistoryClick = {
                        currentSection = SettingsSection.HISTORY
                    },
                    onExportClick = {
                        currentSection = SettingsSection.EXPORT
                    },
                    onBackupClick = {
                        currentSection = SettingsSection.BACKUP
                    },
                    onAboutClick = {
                        currentSection = SettingsSection.ABOUT
                    }
                )
            }

            SettingsSection.CONTACTS -> {
                SettingsSectionHeader(
                    title = "Contatos de emergência",
                    onBackClick = {
                        currentSection = SettingsSection.MENU
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                EmergencyContactScreen()
            }

            SettingsSection.HISTORY -> {
                SettingsSectionHeader(
                    title = "Histórico de incidentes",
                    onBackClick = {
                        currentSection = SettingsSection.MENU
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                EmergencyHistoryScreen()
            }

            SettingsSection.EXPORT -> {
                SettingsSectionHeader(
                    title = "Exportação CSV",
                    onBackClick = {
                        currentSection = SettingsSection.MENU
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Use o botão abaixo para exportar o histórico em formato CSV.",
                    color = TextMuted,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                EmergencyHistoryScreen()
            }

            SettingsSection.BACKUP -> {
                SettingsSectionHeader(
                    title = "Backup protegido",
                    onBackClick = {
                        currentSection = SettingsSection.MENU
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Use o botão abaixo para gerar e compartilhar o backup protegido dos registros.",
                    color = TextMuted,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                EmergencyHistoryScreen()
            }

            SettingsSection.ABOUT -> {
                SettingsSectionHeader(
                    title = "Sobre o Australopitecos",
                    onBackClick = {
                        currentSection = SettingsSection.MENU
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                AboutAustralopitecosCard()
            }
        }
    }
}

@Composable
private fun SettingsMenu(
    onContactsClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onExportClick: () -> Unit,
    onBackupClick: () -> Unit,
    onAboutClick: () -> Unit
) {
    Text(
        text = "Configurações",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold
    )

    Spacer(modifier = Modifier.height(12.dp))

    SettingsCard(
        title = "Contatos de emergência",
        description = "Gerencie os contatos que receberão alertas.",
        onClick = onContactsClick
    )

    Spacer(modifier = Modifier.height(8.dp))

    SettingsCard(
        title = "Histórico de incidentes",
        description = "Consulte registros locais de acionamento.",
        onClick = onHistoryClick
    )

    Spacer(modifier = Modifier.height(8.dp))

    SettingsCard(
        title = "Exportação CSV",
        description = "Compartilhe o histórico em formato aberto.",
        onClick = onExportClick
    )

    Spacer(modifier = Modifier.height(8.dp))

    SettingsCard(
        title = "Backup protegido",
        description = "Exporte uma cópia protegida dos registros.",
        onClick = onBackupClick
    )

    Spacer(modifier = Modifier.height(8.dp))

    SettingsCard(
        title = "Sobre o Australopitecos",
        description = "Segurança: nosso instinto mais antigo.",
        onClick = onAboutClick
    )

    Spacer(modifier = Modifier.height(12.dp))

    Divider()
}

@Composable
private fun SettingsSectionHeader(
    title: String,
    onBackClick: () -> Unit
) {
    Button(
        onClick = onBackClick
    ) {
        Text("← Voltar às configurações")
    }

    Spacer(modifier = Modifier.height(16.dp))

    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun SettingsCard(
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = CardDark,
                shape = RoundedCornerShape(18.dp)
            )
            .clickable {
                onClick()
            }
            .padding(16.dp)
    ) {
        Text(
            text = title,
            color = TextLight,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = description,
            color = TextMuted,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun AboutAustralopitecosCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = DeepForestGreen,
                shape = RoundedCornerShape(18.dp)
            )
            .padding(16.dp)
    ) {
        Text(
            text = "AUSTRALOPITECOS",
            color = TextLight,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Segurança: nosso instinto mais antigo",
            color = TextLight,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Aplicativo de alerta pessoal com palavra-gatilho, GPS, modo stealth, WhatsApp, histórico local, exportação CSV e backup protegido.",
            color = TextLight,
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Versão MVP local",
            color = TextMuted,
            style = MaterialTheme.typography.bodySmall
        )
    }
}
