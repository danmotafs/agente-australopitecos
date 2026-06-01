package com.danmotafs.australopitecos

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognizerIntent
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.danmotafs.australopitecos.emergency.EmergencyContactScreen
import com.danmotafs.australopitecos.emergency.EmergencyContactsStorage
import com.danmotafs.australopitecos.emergency.EmergencyDashboardScreen
import com.danmotafs.australopitecos.emergency.EmergencyHistoryScreen
import com.danmotafs.australopitecos.emergency.EmergencyIncident
import com.danmotafs.australopitecos.emergency.EmergencyIncidentStorage
import com.danmotafs.australopitecos.emergency.EmergencyMessageBuilder
import com.danmotafs.australopitecos.emergency.WhatsAppAlertSender
import com.danmotafs.australopitecos.emergency.SettingsScreen
import com.danmotafs.australopitecos.location.LocationProvider
import com.danmotafs.australopitecos.trigger.TriggerMatcher
import com.danmotafs.australopitecos.ui.theme.AustralopitecosTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeoutOrNull
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AustralopitecosTheme {
                var stealthMode by rememberSaveable { mutableStateOf(false) }

                var emergencyMessage by rememberSaveable {
                    mutableStateOf("Nenhuma mensagem de emergência gerada")
                }

                var showSettings by rememberSaveable {
                    mutableStateOf(false)
                }

                var splashVisible by rememberSaveable {
                    mutableStateOf(true)
                }

                LaunchedEffect(Unit) {
                    delay(1500L)
                    splashVisible = false
                }

                LaunchedEffect(stealthMode) {
                    val controller = WindowInsetsControllerCompat(window, window.decorView)

                    if (stealthMode) {
                        WindowCompat.setDecorFitsSystemWindows(window, false)
                        controller.systemBarsBehavior =
                            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                        controller.hide(WindowInsetsCompat.Type.systemBars())
                    } else {
                        controller.show(WindowInsetsCompat.Type.systemBars())
                        WindowCompat.setDecorFitsSystemWindows(window, true)
                    }
                }

                if (splashVisible) {
                    ProfessionalSplashScreen()
                } else if (stealthMode) {
                    StealthScreen(
                        onExitStealth = {
                            stealthMode = false
                        }
                    )
                } else {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        TriggerMonitorScreen(
                            modifier = Modifier.padding(innerPadding),
                            emergencyMessage = emergencyMessage,
                            showSettings = showSettings,
                            onOpenSettings = {
                                showSettings = true
                            },
                            onCloseSettings = {
                                showSettings = false
                            },
                            onEmergencyMessageGenerated = { generatedMessage ->
                                emergencyMessage = generatedMessage
                            },
                            onSilentEmergencyActivated = {
                                stealthMode = true
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TriggerMonitorScreen(
    modifier: Modifier = Modifier,
    emergencyMessage: String,
    showSettings: Boolean,
    onOpenSettings: () -> Unit,
    onCloseSettings: () -> Unit,
    onEmergencyMessageGenerated: (String) -> Unit,
    onSilentEmergencyActivated: () -> Unit
) {
    val context = LocalContext.current

    var inputText by remember { mutableStateOf("") }
    var resultText by remember { mutableStateOf("Nenhum teste realizado") }
    var monitorActive by remember { mutableStateOf(false) }
    var lastRecognizedText by remember { mutableStateOf("Nenhuma fala reconhecida ainda") }
    var lastLocationLink by rememberSaveable {
        mutableStateOf("Localização ainda não capturada")
    }

    fun captureLocationAndActivateStealth() {
        LocationProvider.getCurrentLocation(context) { mapsLink ->
            lastLocationLink = mapsLink

            val contacts = EmergencyContactsStorage.getContacts(context)
            val primaryContact = contacts.firstOrNull()

            val generatedMessage = EmergencyMessageBuilder.build(
                contact = primaryContact,
                locationLink = mapsLink
            )

            onEmergencyMessageGenerated(generatedMessage)

            val timestamp = SimpleDateFormat(
                "dd/MM/yyyy HH:mm:ss",
                Locale.getDefault()
            ).format(Date())

            EmergencyIncidentStorage.saveIncident(
                context = context,
                incident = EmergencyIncident(
                    timestamp = timestamp,
                    locationLink = mapsLink,
                    message = generatedMessage
                )
            )

            resultText = """
Modo seguro ativado

$mapsLink
""".trimIndent()

            if (primaryContact != null) {
                WhatsAppAlertSender.openWhatsApp(
                    context = context,
                    phone = primaryContact.phone,
                    message = generatedMessage
                )
            } else {
                resultText = "Nenhum contato de emergência cadastrado"
            }

            onSilentEmergencyActivated()
        }
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            captureLocationAndActivateStealth()
        } else {
            resultText = "Permissão de localização negada"
        }
    }

    fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun activateSilentEmergencyProtocol() {
        monitorActive = false
        resultText = "Capturando localização..."

        if (hasLocationPermission()) {
            captureLocationAndActivateStealth()
        } else {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    fun analyzeText(text: String) {
        if (TriggerMatcher.isEmergencyTrigger(text)) {
            activateSilentEmergencyProtocol()
        } else {
            resultText = "Nenhum gatilho detectado"
        }
    }

    fun createSpeechIntent(): Intent {
        return Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale("pt", "BR"))
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Monitoramento ativo: fale normalmente")
        }
    }

    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val spokenText = result.data
                ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                ?.firstOrNull()
                ?: ""

            inputText = spokenText
            lastRecognizedText = spokenText
            analyzeText(spokenText)
        } else {
            lastRecognizedText = "Nenhum áudio reconhecido"

            if (monitorActive) {
                resultText = "Monitorando... aguardando nova fala"
            }
        }
    }

    val audioPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            monitorActive = true
            resultText = "🟢 Monitoramento ativo"
        } else {
            monitorActive = false
            resultText = "Permissão de microfone negada"
        }
    }

    fun hasAudioPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun startMonitoring() {
        if (hasAudioPermission()) {
            monitorActive = true
            resultText = "🟢 Monitoramento ativo"
        } else {
            audioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    fun stopMonitoring() {
        monitorActive = false
        resultText = "Monitoramento pausado"
    }

    LaunchedEffect(monitorActive, resultText) {
        if (monitorActive && resultText != "Modo seguro ativado") {
            speechLauncher.launch(createSpeechIntent())
        }
    }

    if (showSettings) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Button(onClick = onCloseSettings) {
                Text("← Voltar ao Painel")
            }

            Spacer(modifier = Modifier.height(16.dp))

            SettingsScreen()
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.logo_mobile_australopitecos),
            contentDescription = "Logo Australopitecos",
            modifier = Modifier.size(180.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "AUSTRALOPITECOS",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Segurança: nosso instinto mais antigo",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        EmergencyDashboardScreen(
            monitorActive = monitorActive,
            alertSent = emergencyMessage != "Nenhuma mensagem de emergência gerada"
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Monitoramento ativo de palavra-gatilho",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text("Digite uma palavra ou frase") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { analyzeText(inputText) }) {
            Text("Testar texto")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                if (monitorActive) {
                    stopMonitoring()
                } else {
                    startMonitoring()
                }
            }
        ) {
            Text(
                if (monitorActive) {
                    "⏸ Pausar monitoramento"
                } else {
                    "🟢 Ativar monitoramento"
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(resultText, style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Última fala reconhecida:", style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(8.dp))

        Text(lastRecognizedText, style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Última localização capturada:",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(lastLocationLink, style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(24.dp))

        EmergencyContactScreen()

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Última mensagem de emergência:",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = emergencyMessage,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = onOpenSettings) {
            Text("⚙ Configurações")
        }

        Spacer(modifier = Modifier.height(24.dp))

        EmergencyHistoryScreen()
    }
}


@Composable
fun ProfessionalSplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF101418)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_mobile_australopitecos),
                contentDescription = "Logo Australopitecos",
                modifier = Modifier.size(220.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "AUSTRALOPITECOS",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFFF5F7FA)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Segurança: nosso instinto mais antigo",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFFB8C4BE)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Cybersecurity • Emergency Protocol",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF1F8A70)
            )
        }
    }
}

@Composable
fun StealthScreen(
    onExitStealth: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {
                while (true) {
                    awaitPointerEventScope {
                        awaitFirstDown(requireUnconsumed = false)

                        val releasedBeforeSevenSeconds = withTimeoutOrNull(7000L) {
                            while (true) {
                                val event = awaitPointerEvent()
                                val stillPressed = event.changes.any { it.pressed }

                                if (!stillPressed) {
                                    return@withTimeoutOrNull true
                                }
                            }
                        }

                        if (releasedBeforeSevenSeconds == null) {
                            onExitStealth()
                        }
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Text(text = "", color = Color.Black)
    }
}