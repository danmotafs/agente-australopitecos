package com.danmotafs.australopitecos.trigger

object TriggerMatcher {

    private val emergencyTriggerWords = listOf(
        "australopitecos",
        "australoptecos",
        "australopiticos",
        "australopitcus",
        "australopithecus"
    )

    private val exitStealthTriggerWords = listOf(
        "homo habilis",
        "homohabilis",
        "homo abilis",
        "omo habilis",
        "homo habilies",
        "homo hábilis",
        "homo abiles"
    )

    fun isEmergencyTrigger(text: String): Boolean {
        val normalizedText = normalize(text)

        return emergencyTriggerWords.any {
            normalizedText.contains(it)
        }
    }

    fun isExitStealthTrigger(text: String): Boolean {
        val normalizedText = normalize(text)

        return exitStealthTriggerWords.any {
            normalizedText.contains(it)
        }
    }

    private fun normalize(text: String): String {
        return text
            .lowercase()
            .trim()
            .replace("á", "a")
            .replace("à", "a")
            .replace("â", "a")
            .replace("ã", "a")
            .replace("é", "e")
            .replace("ê", "e")
            .replace("í", "i")
            .replace("ó", "o")
            .replace("ô", "o")
            .replace("õ", "o")
            .replace("ú", "u")
            .replace("ç", "c")
    }
}