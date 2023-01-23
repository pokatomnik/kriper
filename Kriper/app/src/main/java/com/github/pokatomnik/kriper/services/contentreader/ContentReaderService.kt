package com.github.pokatomnik.kriper.services.contentreader

import android.app.Application
import java.io.BufferedReader

class ContentReaderService(private val application: Application) {
    fun readContents(path: String): String {
        return application.assets
            .open(path)
            .bufferedReader(Charsets.UTF_8)
            .use(BufferedReader::readText)
    }
}