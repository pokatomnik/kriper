package com.github.pokatomnik.kriper.services.preferences.page

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.github.pokatomnik.kriper.R
import com.github.pokatomnik.kriper.services.preferences.PreferencesStringValue

data class FontInfo(
    val id: String,
    val title: String,
    val fontResourceId: Int?
)

class FontFamily(private val preferencesValue: PreferencesStringValue) {
    private val defaultFontInfo = FontInfo(
        id = "DEFAULT",
        title = "По умолчанию",
        fontResourceId = null
    )

    val availableFontInfos: Map<String, FontInfo> = mutableMapOf<String ,FontInfo>()
        .apply {
            defaultFontInfo.apply { set(id, this) }
            FontInfo(
                id = "ALEGREYA",
                title = "Alegreya",
                fontResourceId = R.font.alegreyascregular
            ).apply { set(id, this) }
            FontInfo(
                id = "LORA",
                title = "Lora",
                fontResourceId = R.font.loraregular
            ).apply { set(id, this) }
            FontInfo(
                id = "PHILOSOPHER",
                title = "Philosopher",
                fontResourceId = R.font.philosopherregular
            ).apply { set(id, this) }
        }

    @Composable
    fun collectAsState(): MutableState<FontInfo> {
        val (fontId, setFontId) = remember {
            val savedFontId = preferencesValue.read(defaultFontInfo.id)
            mutableStateOf(savedFontId)
        }
        val fontInfo = availableFontInfos[fontId] ?: defaultFontInfo
        val setFontInfo: (FontInfo) -> Unit = { setFontId(it.id) }

        return object : MutableState<FontInfo> {
            override var value: FontInfo
                get() = fontInfo
                set(value) {
                    preferencesValue.write(value.id)
                    setFontInfo(value)
                }

            override fun component1() = fontInfo

            override fun component2(): (FontInfo) -> Unit {
                return {
                    preferencesValue.write(it.id)
                    setFontInfo(it)
                }
            }

        }
    }
}