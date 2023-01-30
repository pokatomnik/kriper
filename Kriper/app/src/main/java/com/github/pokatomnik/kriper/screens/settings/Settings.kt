package com.github.pokatomnik.kriper.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.pokatomnik.kriper.ui.components.LARGE_PADDING
import com.github.pokatomnik.kriper.ui.components.PageContainer
import com.github.pokatomnik.kriper.ui.components.PageTitle
import com.github.pokatomnik.kriper.ui.components.SMALL_PADDING
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment

@Composable
fun Settings(
    onNavigateBack: () -> Unit,
) {
    PageContainer(
        priorButton = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Назад"
                )
            }
        },
        header = {
            PageTitle(title = "Настройки")
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxSize()
                    .height(SMALL_PADDING.dp)
            )
            ThemeSection()
            SectionDivider()
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(SMALL_PADDING.dp)
            )
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = LARGE_PADDING.dp),
                mainAxisAlignment = MainAxisAlignment.Center
            ) {
                LegalWarningButton()
                Spacer(modifier = Modifier.width(LARGE_PADDING.dp))
                WebsiteButton()
                Spacer(modifier = Modifier.width(LARGE_PADDING.dp))
                SourcesButton()
                Spacer(modifier = Modifier.width(LARGE_PADDING.dp))
                ContactViaEmailButton()
                Spacer(modifier = Modifier.width(LARGE_PADDING.dp))
                OpenWebmasterContactsButton()
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(SMALL_PADDING.dp)
            )
            SectionDivider()
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(SMALL_PADDING.dp)
            )
            BuildInfoSection()
        }
    }
}