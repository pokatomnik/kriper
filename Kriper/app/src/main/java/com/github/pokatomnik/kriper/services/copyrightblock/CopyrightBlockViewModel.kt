package com.github.pokatomnik.kriper.services.copyrightblock

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CopyrightBlockViewModel @Inject constructor(
    val copyrightBlock: CopyrightBlock
) : ViewModel()
