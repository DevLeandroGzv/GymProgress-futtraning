package com.leandro.gymprogress_futtraing.core

import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.tasks.await


object TranslationManager {

    // Configuración: De Inglés a Español
    private val options = TranslatorOptions.Builder()
        .setSourceLanguage(TranslateLanguage.ENGLISH)
        .setTargetLanguage(TranslateLanguage.SPANISH)
        .build()

    private val translator = Translation.getClient(options)

    suspend fun translateText(text: String): String {
        return try {
            translator.downloadModelIfNeeded().await()
            translator.translate(text).await()
        } catch (e: Exception) {
            text
        }
    }
}