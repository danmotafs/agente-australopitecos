# 🛡️ Australopitecos

### Segurança: nosso instinto mais antigo

Australopitecos é um aplicativo Android de segurança pessoal desenvolvido para auxiliar usuários em situações de risco através de monitoramento por voz, palavras-gatilho, geolocalização e protocolos de emergência.

O projeto foi construído como um MVP funcional utilizando Kotlin e Jetpack Compose, com foco em acionamento rápido, discreto e eficiente de contatos de emergência.

---

## 🚀 Funcionalidades

### 🎙️ Monitoramento por voz

Monitora continuamente frases e palavras previamente definidas pelo usuário.

### 🚨 Palavras-gatilho

Ao detectar uma palavra ou frase de emergência, o aplicativo executa automaticamente o protocolo de segurança.

### 📍 Geolocalização

Captura a localização atual do dispositivo e gera um link compartilhável do Google Maps.

### 💬 Integração WhatsApp

Gera automaticamente mensagens de emergência contendo:

* alerta de risco
* horário do incidente
* localização atual

### 👥 Múltiplos contatos

Permite cadastrar diversos contatos de emergência.

### 🕶️ Modo Stealth

Oculta a interface principal após o acionamento de emergência.

### 📚 Histórico local

Armazena localmente os incidentes registrados.

### 📄 Exportação CSV

Permite exportar registros para análise posterior.

### 🔒 Backup protegido

Geração de cópias de segurança dos dados locais.

### 📊 Dashboard Executivo

Exibe indicadores operacionais do sistema:

* incidentes registrados
* contatos cadastrados
* histórico
* backup
* status operacional

### ⚙️ Configurações

Centralização de recursos administrativos do aplicativo.

---

## 🏗️ Arquitetura

```text
Monitoramento de Voz
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
 Dashboard Executivo
```

---

## 🛠️ Tecnologias

* Kotlin
* Jetpack Compose
* Android SDK
* Material Design 3
* Android Location Services
* SharedPreferences
* FileProvider
* WhatsApp Intent API

---

## 📂 Estrutura

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

---

## 📱 Como executar

### Requisitos

* Android Studio Hedgehog ou superior
* Android SDK 34+
* Dispositivo Android físico ou emulador

### Instalação

```bash
git clone https://github.com/danmotafs/agente-australopitecos.git
```

Abra o projeto no Android Studio e execute:

```txt
Run ▶
```

---

## 🔐 Permissões necessárias

O aplicativo utiliza:

* Microfone
* Localização precisa
* Localização aproximada

---

## 🗺️ Roadmap

### v0.6.0

* Refinamento visual
* Home reorganizada
* Melhorias de UX

### v0.7.0

* Integração Telegram
* Grupos de emergência

### v0.8.0

* Sincronização em nuvem

### v1.0.0

* Backend dedicado
* Painel administrativo
* Gestão remota

---

## 📌 Status

Versão atual:

```text
v0.5.0
```

Status:

```text
MVP navegável
```

---

## 👨‍💻 Autor

Daniel Mota Ferreira Silva

Data Intelligence • Process Management • Artificial Intelligence • Public Sector Innovation

---

## ⚠️ Aviso

Australopitecos é um projeto em desenvolvimento e não substitui serviços oficiais de emergência ou segurança pública.
