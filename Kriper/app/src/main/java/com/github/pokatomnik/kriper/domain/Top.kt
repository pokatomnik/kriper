package com.github.pokatomnik.kriper.domain

data class Top(
    val weekTop: Set<String>,
    val monthTop: Set<String>,
    val yearTop: Set<String>,
    val allTheTimeTop: Set<String>,
)