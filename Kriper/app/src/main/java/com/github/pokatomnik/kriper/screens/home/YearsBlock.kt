package com.github.pokatomnik.kriper.screens.home

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.pokatomnik.kriper.services.index.IndexServiceReadiness
import com.github.pokatomnik.kriper.ui.components.IconicCardSmall
import com.github.pokatomnik.kriper.ui.components.LARGE_PADDING
import com.github.pokatomnik.kriper.ui.components.SMALL_PADDING

@Composable
fun YearsBlock(
    onNavigateToYear: (year: Int) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = SMALL_PADDING.dp)
        ) {
            Spacer(modifier = Modifier.width(LARGE_PADDING.dp))
            Text(
                text = "ПО ГОДАМ",
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(
            modifier = Modifier.height(SMALL_PADDING.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {
            IndexServiceReadiness { indexService ->
                indexService.content.selections.yearToMonths.entries
                    .sortedWith { (yearA), (yearB) -> yearB - yearA}
                    .forEachIndexed { index, (year) ->
                        if (index == 0) {
                            Spacer(modifier = Modifier.width(SMALL_PADDING.dp))
                        }
                        IconicCardSmall(
                            title = year.toString(),
                            onClick = { onNavigateToYear(year) }
                        )
                        Spacer(modifier = Modifier.width(SMALL_PADDING.dp))
                    }
            }
        }
    }
}