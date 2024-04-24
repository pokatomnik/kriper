package com.github.pokatomnik.kriper.screens.about

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
import com.github.pokatomnik.kriper.services.KRIPER_DOMAIN
import com.github.pokatomnik.kriper.ui.components.LARGE_PADDING
import com.github.pokatomnik.kriper.ui.components.PageContainer
import com.github.pokatomnik.kriper.ui.components.PageTitle

@Composable
fun About(
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
            PageTitle(title = "О приложении")
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = LARGE_PADDING.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.fillMaxWidth().height(LARGE_PADDING.dp))
            Text(text = ABOUT_TEXT)
            Spacer(modifier = Modifier.fillMaxWidth().height(LARGE_PADDING.dp))
        }
    }
}

const val ABOUT_TEXT = "Kriper - приложение с открытым исходным кодом для чтения историй с сайта https://$KRIPER_DOMAIN. " +
        "Авторство всех историй, имеющихся в данном приложении принадлежит только " +
        "их создателям. Все совпадения с реальными личностями, местами, верованиями и " +
        "исповеданиями случайны. Ни вебсайт https://$KRIPER_DOMAIN, ни данное приложение " +
        "не несет ответственности за возможный ущерб, нанесенный любым образом, " +
        "будь то материальный или моральный. Приложение не отображает иного контента, " +
        "кроме того который есть в публичном доступе на сайте https://$KRIPER_DOMAIN."