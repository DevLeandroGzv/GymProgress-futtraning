package com.leandro.gymprogress_futtraing.core

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.UUID

fun saveImageToInternalStorage(context: Context, uri: Uri): String? {
    val contentResolver = context.contentResolver

    val fileName = "exercise_${UUID.randomUUID()}.jpg"
    val outputFile = File(context.filesDir, fileName)

    return try {
        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(outputFile)

        inputStream?.copyTo(outputStream)

        inputStream?.close()
        outputStream.close()

        outputFile.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}