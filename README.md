# 🛡️ Australopitecos

### Segurança: nosso instinto mais antigo.

Australopitecos é um aplicativo Android de segurança pessoal que combina monitoramento por voz, palavras-gatilho, geolocalização e protocolos de emergência para auxiliar usuários em situações de risco.

O projeto foi desenvolvido como um MVP funcional focado em acionamento rápido e discreto de contatos de emergência.

---

# Funcionalidades

## 🎙️ Monitoramento por voz

Monitora continuamente frases e palavras previamente definidas pelo usuário.

---

## 🚨 Palavra-gatilho

Ao detectar um gatilho de emergência:

* interrompe o monitoramento
* captura a localização atual
* gera uma mensagem de emergência
* aciona o protocolo de segurança

---

## 📍 Localização GPS

Captura automaticamente a localização do usuário.

Exemplo:

https://maps.google.com/?q=latitude,longitude

---

## 💬 Integração WhatsApp

Gera automaticamente uma mensagem contendo:

* alerta de emergência
* horário do incidente
* link da localização

---

## 👥 Múltiplos contatos

Permite cadastrar diversos contatos de emergência.

---

## 🕶️ Modo Stealth

Após o acionamento:

* a interface é ocultada
* a tela torna-se discreta
* reduz a percepção de uso do aplicativo

---

## 📚 Histórico Local

Armazena registros de incidentes diretamente no dispositivo.

---

## 📄 Exportação CSV

Permite exportar registros para análise posterior.

---

## 🔒 Backup Protegido

Geração de cópias de segurança dos registros locais.

---

## 📊 Dashboard Executivo

Exibe:

* incidentes registrados
* contatos cadastrados
* status operacional
* histórico
* backup

---

## ⚙️ Configurações

Área centralizada para:

* contatos
* histórico
* exportação
* backup
* informações do aplicativo

---

# Arquitetura

```text
Voz
 ↓
TriggerMatcher
 ↓
LocationProvider
 ↓
EmergencyMessageBuilder
 ↓
WhatsAppAlertSender
 ↓
EmergencyIncidentStorage
 ↓
Dashboard
```

# Tecnologias

* Kotlin
* Jetpack Compose
* Android SDK
* Location Services
* WhatsApp Intent
* Material Design 3

# Estrutura do Projeto

```text
app
 ├── emergency
 │   ├── EmergencyContact
 │   ├── EmergencyContactScreen
 │   ├── EmergencyDashboardScreen
 │   ├── EmergencyHistoryScreen
 │   ├── EmergencyMessageBuilder
 │   └── WhatsAppAlertSender
 │
 ├── location
 │   └── LocationProvider
 │
 ├── trigger
 │   └── TriggerMatcher
 │
 └── MainActivity
```

# Roadmap

## v0.6.0

* melhorias visuais
* organização da Home
* refinamento da UX

## v0.7.0

* integração Telegram
* grupos de emergência

## v0.8.0

* sincronização em nuvem

## v1.0.0

* backend completo
* painel administrativo
* gerenciamento remoto

# Status

Versão atual:

v0.5.0

Maturidade estimada:

75%

# Autor

Daniel Mota Ferreira Silva

Data & Process Manager | AI | Data Intelligence | Public Sector Innovation
