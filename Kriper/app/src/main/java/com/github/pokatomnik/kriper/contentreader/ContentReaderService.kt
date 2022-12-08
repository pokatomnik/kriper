package com.github.pokatomnik.kriper.contentreader

import android.app.Application
import java.io.BufferedReader

class ContentReaderService(private val application: Application) {
    fun readContents(path: String): String {
        return application.assets
            .open("content/index.json")
            .bufferedReader(Charsets.UTF_8)
            .use(BufferedReader::readText)
    }
}