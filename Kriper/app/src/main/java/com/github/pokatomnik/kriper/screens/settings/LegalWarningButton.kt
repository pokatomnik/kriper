package com.github.pokatomnik.kriper.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.pokatomnik.kriper.services.KRIPER_DOMAIN
import com.github.pokatomnik.kriper.ui.components.LARGE_PADDING

@Composable
fun LegalWarningButton() {
    val dialogOpen = remember { mutableStateOf(false) }
    val showDialog = { dialogOpen.value = true }
    val hideDialog = { dialogOpen.value = false }

    TextButton(onClick = showDialog) {
        Text("О приложении")
    }
    if (dialogOpen.value) {
        AlertDialog(
            onDismissRequest = hideDialog,
            title = { Text("О приложении") },
            text = {
                Text(
                    text = "Kriper - приложение с открытым исходным кодом для чтения историй с сайта https://$KRIPER_DOMAIN. " +
                            "Авторство всех историй, имеющихся в данном приложении принадлежит только " +
                            "их создателям. Все совпадения с реальными личностями, местами, верованиями и " +
                            "исповеданиями случайны. Ни вебсайт https://$KRIPER_DOMAIN, ни данное приложение " +
                            "не несет ответственности за возможный ущерб, нанесенный любым образом, " +
                            "будь то материальный или моральный. Приложение не отображает иного контента, " +
                            "кроме того который есть в публичном доступе на сайте https://$KRIPER_DOMAIN.",
                    textAlign = TextAlign.Justify
                )
            },
            buttons = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 0.dp,
                            start = LARGE_PADDING.dp,
                            end = LARGE_PADDING.dp,
                            bottom = LARGE_PADDING.dp
                        ),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(onClick = hideDialog) {
                        Text("Понятно")
                    }
                }
            },
        )
    }
}