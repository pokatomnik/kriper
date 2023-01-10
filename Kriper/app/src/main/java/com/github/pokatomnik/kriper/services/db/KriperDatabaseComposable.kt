package com.github.pokatomnik.kriper.services.db

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun rememberKriperDatabase(): KriperDatabase {
    return hiltViewModel<KriperDatabaseViewModel>().database
}