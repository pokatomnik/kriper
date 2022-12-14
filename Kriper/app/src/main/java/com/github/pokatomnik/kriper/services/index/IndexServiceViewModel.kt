package com.github.pokatomnik.kriper.services.index

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class IndexServiceViewModel @Inject constructor(
    val indexService: IndexService
) : ViewModel()