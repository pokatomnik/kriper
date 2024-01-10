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

    val gold: Collection<PageMeta> by lazy {
        index.pageMeta.values
            .filter { it.gold }
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

    val weekTop: Collection<PageMeta> by lazy {
        index.top.weekTop.fold(mutableListOf()) { acc, currentStoryId ->
            acc.apply {
                index.pageMeta[currentStoryId]?.let(::add)
            }
        }
    }

    val monthTop: Collection<PageMeta> by lazy {
        index.top.monthTop.fold(mutableListOf()) { acc, currentStoryId ->
            acc.apply {
                index.pageMeta[currentStoryId]?.let(::add)
            }

        }
    }

    val yearTop: Collection<PageMeta> by lazy {
        index.top.yearTop.fold(mutableListOf()) { acc, currentStoryId ->
            acc.apply {
                index.pageMeta[currentStoryId]?.let(::add)
            }
        }
    }

    val allTheTimeTop: Collection<PageMeta> by lazy {
        index.top.allTheTimeTop.fold(mutableListOf()) { acc, currentStoryId ->
            acc.apply {
                index.pageMeta[currentStoryId]?.let(::add)
            }
        }
    }

    /**
     * year - month - pagemeta list
     */
    val yearToMonths: Map<Int, Map<Int, Collection<PageMeta>>> by lazy {
        val result: MutableMap<Int, MutableMap<Int, MutableCollection<PageMeta>>> = mutableMapOf()

        index.pageMeta.values.forEach { pageMeta ->
            val year = pageMeta.dateCreated.year
            val month = pageMeta.dateCreated.month

            val currentMonths = result[year] ?: mutableMapOf()

            val currentPageMetaList = currentMonths[month] ?: mutableListOf()

            currentPageMetaList.add(pageMeta)

            if (currentMonths[month] == null) {
                currentMonths[month] = currentPageMetaList
            }

            if (result[year] == null) {
                result[year] = currentMonths
            }
        }

        result
    }
}