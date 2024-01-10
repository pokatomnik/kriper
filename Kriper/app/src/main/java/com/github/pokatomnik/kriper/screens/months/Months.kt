package com.github.pokatomnik.kriper.screens.months

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.pokatomnik.kriper.services.index.IndexServiceReadiness
import com.github.pokatomnik.kriper.ui.components.LARGE_PADDING
import com.github.pokatomnik.kriper.ui.components.LazyList
import com.github.pokatomnik.kriper.ui.components.PageContainer
import com.github.pokatomnik.kriper.ui.components.PageTitle
import com.github.pokatomnik.kriper.ui.components.SMALL_PADDING

@Composable
fun Months(
    year: Int,
    onNavigateToStoriesOfMonth: (month: Int) -> Unit,
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
            PageTitle(title = "Истории за $year год")
        }
    ) {
        IndexServiceReadiness { indexService ->
            indexService.content.selections.yearToMonths[year]?.let { currentYearMonths ->
                val monthsSorted = currentYearMonths.keys.toList().sortedWith { monthA, monthB ->
                    monthA - monthB
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = SMALL_PADDING.dp)
                ) {
                    LazyList(list = monthsSorted) { index, monthIndex ->
                        val isFirst = 0 == index
                        if (isFirst) {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(SMALL_PADDING.dp)
                            )
                        }
                        MonthListItem(
                            month = monthIndex,
                            year = year,
                            onPress = { onNavigateToStoriesOfMonth(monthIndex) }
                        )
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(LARGE_PADDING.dp)
                        )
                    }
                }
            }
        }
    }
}
