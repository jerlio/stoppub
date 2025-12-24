package com.jelio.stoppub

import android.telecom.Call
import android.telecom.CallScreeningService

class PrefixCallScreeningService : CallScreeningService() {

    // 1️⃣ Liste simple des préfixes bloqués
    private val blockedPrefixes = setOf(
        "0162", "0163",
        "0270", "0271",
        "0377", "0378",
        "0424", "0425",
        "0568", "0569",
        "0948", "0949"
    )

    override fun onScreenCall(callDetails: Call.Details) {

        // 2️⃣ Récupération du numéro entrant
        val number = callDetails.handle?.schemeSpecificPart

        // 3️⃣ Extraction des 4 premiers chiffres
        val prefix = extractPrefix(number)

        // 4️⃣ Décision
        val shouldBlock = prefix != null && blockedPrefixes.contains(prefix)

        // Sauvegarde de l'appel
        CallLogStore.addLog(this, number, shouldBlock)

        // 5️⃣ Réponse à Android
        val response = CallResponse.Builder()
            .setDisallowCall(shouldBlock)   // empêche l'appel
            .setRejectCall(shouldBlock)     // raccroche
            .setSkipCallLog(shouldBlock)    // pas dans l'historique
            .setSkipNotification(shouldBlock) // pas de notification
            .build()

        respondToCall(callDetails, response)
    }

    // Fonction simple pour extraire le préfixe
    private fun extractPrefix(number: String?): String? {
        if (number.isNullOrBlank()) return null

        // Nettoyage basique
        val clean = number
            .replace("+33", "0")
            .replace(" ", "")

        return if (clean.length >= 4) {
            clean.take(4)
        } else {
            null
        }
    }
}
