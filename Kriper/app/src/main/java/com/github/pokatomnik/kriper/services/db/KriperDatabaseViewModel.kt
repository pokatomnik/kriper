package com.github.pokatomnik.kriper.services.db

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class KriperDatabaseViewModel @Inject constructor(
    val database: KriperDatabase
) : ViewModel()