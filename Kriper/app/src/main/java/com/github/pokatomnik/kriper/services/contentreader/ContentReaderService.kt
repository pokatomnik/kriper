package com.github.pokatomnik.kriper.services.contentreader

import android.app.Application
import android.graphics.drawable.Drawable
import java.io.BufferedReader
import java.io.IOException

class ContentReaderService(private val application: Application) {
    fun getTextContent(path: String): String {
        return application.assets
            .open(path)
            .bufferedReader(Charsets.UTF_8)
            .use(BufferedReader::readText)
    }

    fun getDrawable(path: String): Drawable? {
        return try {
            Drawable.createFromStream(
                application.applicationContext.assets.open(path),
                null
            )
        } catch (e: IOException) { null }
    }
}