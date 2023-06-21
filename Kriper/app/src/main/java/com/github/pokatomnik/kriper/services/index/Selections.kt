package com.github.pokatomnik.kriper.services.index

import com.github.pokatomnik.kriper.domain.Index
import com.github.pokatomnik.kriper.domain.PageMeta
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

private const val AVG_RATING = 25
private const val SHORT_STORY_READING_TIME_MAX = 5
private const val LONG_STORY_READING_TIME_MIN = 25
private const val MONTH_SECONDS = 60 * 60 * 24 * 31


data class Selections(private val index: Index) {
    val allStoryTitles: Collection<PageMeta> by lazy {
        index.pageMeta.values.sortedWith { a, b -> a.title.compareTo(b.title) }
    }

    val shortAndMostUpVoted: Collection<PageMeta> by lazy {
        index.pageMeta.values
            .filter {
                it.rating >= AVG_RATING && it.readingTimeMinutes <= SHORT_STORY_READING_TIME_MAX
            }
            .sortedWith { a, b -> a.title.compareTo(b.title) }
    }

    val longAndMostUpVoted: Collection<PageMeta> by lazy {
        index.pageMeta.values
            .filter {
                it.rating >= AVG_RATING && it.readingTimeMinutes >= LONG_STORY_READING_TIME_MIN
            }
            .sortedWith { a, b -> a.title.compareTo(b.title) }
    }

    val new: Collection<PageMeta> by lazy {
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
            .sortedWith { a, b -> a.title.compareTo(b.title) }
    }
}