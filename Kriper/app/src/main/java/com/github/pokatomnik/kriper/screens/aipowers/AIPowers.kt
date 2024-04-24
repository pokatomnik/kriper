package com.github.pokatomnik.kriper.screens.aipowers

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.pokatomnik.kriper.ui.components.LARGE_PADDING
import com.github.pokatomnik.kriper.ui.components.PageContainer
import com.github.pokatomnik.kriper.ui.components.PageTitle

@Composable
fun AIPowers(
    onNavigateBack: () -> Unit
) {
    PageContainer(
        priorButton = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Назад"
                )
            }
        },
        header = {
            PageTitle(title = "AI Powers")
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = LARGE_PADDING.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.fillMaxWidth().height(LARGE_PADDING.dp))
            Text(text = AI_POWERS)
            Spacer(modifier = Modifier.fillMaxWidth().height(LARGE_PADDING.dp))
        }
    }
}

const val AI_POWERS = "При создании приложения использованы AI инструменты, " +
        "в частности, короткие описания историй сгенерированы с помощью " +
        "нейросети YandexGPT. Разработчики и авторы настоящего приложения " +
        "не имеют никакого отношения к контенту, который сгенерирован " +
        "этой нейросетью. В случае если материал, сгенерированный нейросетью " +
        "кажется оскорбительным, вводящим в заблуждения или просто не нравится " +
        "по любой причине, просим Вас воздержаться от использования данного " +
        "приложения и просим прощения за предоставленные неудобства. " +
        "Так же будем признательны за любую обратную связь касаемо " +
        "настоящего приложения или отдельно этой функциональности."