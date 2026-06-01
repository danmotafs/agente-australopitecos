package com.danmotafs.australopitecos.emergency

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun EmergencyContactScreen() {
    val context = LocalContext.current

    var contactName by remember { mutableStateOf("") }
    var contactPhone by remember { mutableStateOf("") }

    var contacts by remember {
        mutableStateOf(EmergencyContactsStorage.getContacts(context))
    }

    var statusMessage by remember {
        mutableStateOf("Contatos carregados: ${contacts.size}")
    }

    Column {
        Text(
            text = "Contatos de emergência",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = contactName,
            onValueChange = { contactName = it },
            label = { Text("Nome do contato") }
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = contactPhone,
            onValueChange = { contactPhone = it },
            label = { Text("Telefone com DDD") }
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                if (contacts.size >= 5) {
                    statusMessage = "Limite de 5 contatos atingido"
                    return@Button
                }

                if (contactName.isBlank() || contactPhone.isBlank()) {
                    statusMessage = "Preencha nome e telefone"
                    return@Button
                }

                val updatedContacts = contacts.toMutableList()

                updatedContacts.add(
                    EmergencyContact(
                        name = contactName.trim(),
                        phone = contactPhone.trim()
                    )
                )

                EmergencyContactsStorage.saveContacts(context, updatedContacts)

                contacts = updatedContacts
                contactName = ""
                contactPhone = ""
                statusMessage = "Contato adicionado com sucesso"
            }
        ) {
            Text("Adicionar contato")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = statusMessage,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Contatos cadastrados:",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (contacts.isEmpty()) {
            Text("Nenhum contato cadastrado")
        } else {
            contacts.forEachIndexed { index, contact ->
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "${index + 1}. ${contact.name}",
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = contact.phone,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Button(
                    onClick = {
                        val updatedContacts = contacts.toMutableList()
                        updatedContacts.removeAt(index)

                        EmergencyContactsStorage.saveContacts(context, updatedContacts)

                        contacts = updatedContacts
                        statusMessage = "Contato removido"
                    }
                ) {
                    Text("Remover")
                }
            }
        }
    }
}