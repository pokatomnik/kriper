package com.github.pokatomnik.kriper.services.preferences.page

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.github.pokatomnik.kriper.services.preferences.PreferencesIntValue

class FontSize(private val preferencesValue: PreferencesIntValue) {
    private fun coerce(desiredFontSize: Int): Int {
        return desiredFontSize.coerceIn(min..max)
    }

    @Composable
    fun collectAsState(): MutableState<Int> {
        val (fontSize, setFontSize) = remember {
            mutableStateOf(
                preferencesValue.read(defaultFontSize)
            )
        }

        return object : MutableState<Int> {
            override var value: Int
                get() = fontSize
                set(value) {
                    val coercedValue = coerce(value)
                    setFontSize(coercedValue)
                    preferencesValue.write(coercedValue)
                }
            override fun component1(): Int {
                return fontSize
            }
            override fun component2(): (Int) -> Unit {
                return { fontSize ->
                    val coercedValue = coerce(fontSize)
                    setFontSize(coercedValue)
                    preferencesValue.write(coercedValue)
                }
            }
        }
    }

    companion object {
        const val min = 16
        const val max = 60
        const val defaultFontSize = min
    }
}