package com.github.pokatomnik.kriper.services.index

import com.github.pokatomnik.kriper.domain.Index
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

private const val AVG_RATING = 25
private const val SHORT_STORY_READING_TIME_MAX = 5
private const val LONG_STORY_READING_TIME_MIN = 25
private const val MONTH_SECONDS = 60 * 60 * 24 * 31


data class Selections(private val index: Index) {
    val allStoryTitles: Collection<String> by lazy {
        index.pageMeta.keys.sortedWith { a, b -> a.compareTo(b) }
    }

    val shortAndMostUpVoted: Collection<String> by lazy {
        index.pageMeta.values
            .filter {
                it.rating >= AVG_RATING && it.readingTimeMinutes <= SHORT_STORY_READING_TIME_MAX
            }
            .map { it.title }
            .sortedWith { a, b -> a.compareTo(b) }
    }

    val longAndMostUpVoted: Collection<String> by lazy {
        index.pageMeta.values
            .filter {
                it.rating >= AVG_RATING && it.readingTimeMinutes >= LONG_STORY_READING_TIME_MIN
            }
            .map { it.title }
            .sortedWith { a, b -> a.compareTo(b) }
    }

    val new: Collection<String> by lazy {
        index.pageMeta.values
            .filter {
                val dateCreatedSecondsTimestamp = LocalDate.of(
                    it.dateCreated.year,
                    it.dateCreated.month,
                    it.dateCreated.day
                ).atStartOfDay(ZoneId.systemDefault()).toInstant().epochSecond
                val now = Instant.now().epochSecond
                now - dateCreatedSecondsTimestamp <= MONTH_SECONDS
            }
            .map { it.title }
            .sortedWith { a, b -> a.compareTo(b) }
    }
}