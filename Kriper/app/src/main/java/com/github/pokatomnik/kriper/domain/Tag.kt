package com.github.pokatomnik.kriper.domain

/**
 * This class has a tag name and the set of identifiers of stories
 */
data class Tag(val tagName: String, val storyIds: Collection<String>)
