package com.github.pokatomnik.kriper.ui.widgets

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.github.pokatomnik.kriper.navigation.Navigation

@Composable
fun KriperBottomNavigation(navigation: Navigation) {
    BottomNavigation(
        elevation = 1.dp
    ) {
        BottomNavigationItem(
            selected = navigation.homeRoute.on(),
            onClick = { navigation.homeRoute.navigate() },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = "Главная"
                )
            }
        )
        BottomNavigationItem(
            selected = navigation.searchRoute.on(),
            onClick = { navigation.searchRoute.navigate() },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Поиск"
                )
            }
        )
        BottomNavigationItem(
            selected = navigation.historyRoute.on(),
            onClick = { navigation.historyRoute.navigate() },
            icon = {
                Icon(
                    imageVector = Icons.Filled.History,
                    contentDescription = "Хронология"
                )
            }
        )
        BottomNavigationItem(
            selected = navigation.settingsRoute.on(),
            onClick = { navigation.settingsRoute.navigate() },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Настройки"
                )
            }
        )
    }
}