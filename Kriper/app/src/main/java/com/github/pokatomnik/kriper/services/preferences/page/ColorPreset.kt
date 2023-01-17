package com.github.pokatomnik.kriper.services.preferences.page

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.github.pokatomnik.kriper.services.preferences.PreferencesStringValue

data class ColorsInfo(
    val id: String,
    val title: String,
    val backgroundColor: Color?,
    val contentColor: Color?,
)

class ColorPreset(private val preferencesValue: PreferencesStringValue) {
    private val defaultColorsInfo = ColorsInfo(
        id = "DEFAULT",
        title = "Из темы",
        backgroundColor = null,
        contentColor = null,
    )

    val availableColorPresets: Map<String, ColorsInfo> = mutableMapOf<String, ColorsInfo>()
        .apply {
            defaultColorsInfo.apply { set(id, this) }
            ColorsInfo(
                id = "CREME",
                title = "Кремовый",
                backgroundColor = Color(0xfffff3e0),
                contentColor = Color(0xff000000),
            ).apply { set(id, this) }
            ColorsInfo(
                id = "LIGHT_CYAN",
                title = "Светлый циан",
                backgroundColor = Color(0xffe0f7fa),
                contentColor = Color(0xff000000),
            ).apply { set(id, this) }
            ColorsInfo(
                id = "BLACK_WHITE",
                title = "Черно-белый",
                backgroundColor = Color(0xff000000),
                contentColor = Color(0xffffffff),
            ).apply { set(id, this) }
            ColorsInfo(
                id = "WHITE_BLACK",
                title = "Бело-черный",
                backgroundColor = Color(0xffffffff),
                contentColor = Color(0xff000000),
            ).apply { set(id, this) }
            ColorsInfo(
                id = "DARK_DEEP_BLUE",
                title = "Темный синевато-черный",
                backgroundColor = Color(0xff424242),
                contentColor = Color(0xffffffff),
            ).apply { set(id, this) }
            ColorsInfo(
                id = "TERRACOTTA",
                title = "Темный терракотовый",
                backgroundColor = Color(0xff4e342e),
                contentColor = Color(0xffffffff),
            ).apply { set(id, this) }
        }

    @Composable
    fun collectAsState(): MutableState<ColorsInfo> {
        val (colorPresetId, setColorPresetId) = remember {
            val savedColorPresetId = preferencesValue.read(defaultColorsInfo.id)
            mutableStateOf(savedColorPresetId)
        }
        val colorPreset = availableColorPresets[colorPresetId] ?: defaultColorsInfo
        val setColorPreset: (ColorsInfo) -> Unit = { setColorPresetId(it.id) }

        return object : MutableState<ColorsInfo> {
            override var value: ColorsInfo
                get() = colorPreset
                set(value) {
                    preferencesValue.write(value.id)
                    setColorPreset(value)
                }

            override fun component1() = colorPreset

            override fun component2(): (ColorsInfo) -> Unit {
                return {
                    preferencesValue.write(it.id)
                    setColorPreset(it)
                }
            }

        }
    }
}