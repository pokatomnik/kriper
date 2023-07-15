package com.github.pokatomnik.kriper.services.copyrightblock

import android.content.Context
import com.github.pokatomnik.kriper.services.api.KriperService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CopyrightBlock(context: Context, private val kriperService: KriperService) {
    private val sharedPreferences = context.getSharedPreferences(
        "BLOCKED_CACHE",
        Context.MODE_PRIVATE
    )

    private var blocked: Set<String>? = null

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private fun readBlockedFromSharedPreferences(): Set<String> {
        return sharedPreferences.getStringSet(CACHE_KEY, setOf())?.toSet() ?: setOf()
    }

    private fun writeBlockedToSharedPreferences(blocked: Set<String>) {
        sharedPreferences.edit().putStringSet(CACHE_KEY, blocked).apply()
    }

    @Synchronized
    fun tryInit() {
        if (blocked != null) {
            return
        }
        blocked = readBlockedFromSharedPreferences()
        coroutineScope.launch {
            try {
                val fetchedBlocked = kriperService
                    .getCopyrightHolderBlocks()
                    .map { it.storyId }
                    .toSet()
                blocked = fetchedBlocked
                writeBlockedToSharedPreferences(fetchedBlocked)
            } catch (_: Exception) {}
        }
    }

    fun isBlocked(storyId: String): Boolean {
        return blocked?.contains(storyId) ?: false
    }

    companion object {
        private const val CACHE_KEY = "BLOCKED_CACHE"
    }
}