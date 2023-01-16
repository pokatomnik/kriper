package com.github.pokatomnik.kriper.services.preferences.global

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.github.pokatomnik.kriper.services.preferences.PreferencesStringValue
import com.google.gson.Gson
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface Actions {
    @Composable
    fun Once(key: String, action: () -> Unit)
}

class OncePerRunActions : Actions {
    private val actionKeys = mutableSetOf<String>()

    @Composable
    override fun Once(key: String, action: () -> Unit) {
        LaunchedEffect(Unit) {
            if (!actionKeys.contains(key)) {
                action()
                actionKeys.add(key)
            }
        }
    }
}

class OncePerInstallActions(
    private val preferencesStringValue: PreferencesStringValue
) : Actions {
    val serializer = Gson()

    private fun serializeActionKeys(actionKeysSet: Set<String>): String {
        return serializer.toJson(actionKeysSet.toList())
    }

    private fun parseActionKeys(source: String): Set<String> {
        return JsonParser.parseString(source).asJsonArray.map { it.asString }.toSet()
    }

    @Composable
    override fun Once(key: String, action: () -> Unit) {
        LaunchedEffect(Unit) {
            launch {
                val job = SupervisorJob()
                val serializedActionKeys = withContext(Dispatchers.IO + job) {
                    preferencesStringValue.read("[]")
                }
                val existingActionKeys = withContext(Dispatchers.Default + job) {
                    parseActionKeys(serializedActionKeys)
                }

                if (!existingActionKeys.contains(key)) {
                    action()

                    val newExistingActionKeys = existingActionKeys.toMutableSet().apply { add(key) }
                    val newSerializedActionKeys = withContext(Dispatchers.Default + job) {
                        serializeActionKeys(newExistingActionKeys)
                    }
                    withContext(Dispatchers.IO + job) {
                        preferencesStringValue.write(newSerializedActionKeys)
                    }
                }
            }
        }
    }
}

class OneTimeRunners(preferencesStringValue: PreferencesStringValue) {
    val oncePerRunActions = OncePerRunActions()
    val oncePerInstallActions = OncePerInstallActions(preferencesStringValue)
}