package com.github.pokatomnik.kriper.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GroupWork
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import com.github.pokatomnik.kriper.navigation.rememberNavigation

@Composable
fun KriperNavBottomBar() {
    val navigation = rememberNavigation()
    NavigationBar() {
        NavigationBarItem(
            selected = navigation.tagGroupsRoute.on(),
            onClick = { navigation.tagGroupsRoute.navigate() },
            icon = {
                Icon(
                    imageVector = Icons.Filled.GroupWork,
                    contentDescription = "Группы тегов"
                )
            }
        )
        NavigationBarItem(
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