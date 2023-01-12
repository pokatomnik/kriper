package com.github.pokatomnik.kriper.services.preferences.sorting

import com.github.pokatomnik.kriper.domain.PageMeta

private val valuableChars = "abcdefghijklmnopqrstuvwxyzабвгдеёжзиклмнопрстуфхцчшщъыьэюя"
    .let { it + it.uppercase() } + "0123456789".split("").toSet()

private fun String.valuableChars(): String = this.filter { char -> valuableChars.contains(char) }

class AlphaASC : PageMetaSorter {
    override val id = "ALPHA_ASC"
    override val title = "Алфавит: по возрастанию"
    override fun compare(a: PageMeta, b: PageMeta) = a.title
        .valuableChars()
        .compareTo(b.title.valuableChars())
}

class AlphaDESC : PageMetaSorter {
    override val id = "ALPHA_DESC"
    override val title = "Алфавит: по убыванию"
    override fun compare(a: PageMeta, b: PageMeta) = b.title
        .valuableChars()
        .compareTo(a.title.valuableChars())
}

class DateASC : PageMetaSorter {
    override val id = "DATE_ASC"
    override val title = "Дата: по возрастанию"
    override fun compare(a: PageMeta, b: PageMeta) = a.dateCreated.compareTo(b.dateCreated)
}

class DateDESC : PageMetaSorter {
    override val id = "DATE_DESC"
    override val title = "Дата: по убыванию"
    override fun compare(a: PageMeta, b: PageMeta) = b.dateCreated.compareTo(a.dateCreated)
}

class NumberOfViewsASC : PageMetaSorter {
    override val id = "NUMBER_OF_VIEWS_ASC"
    override val title = "Просмотры: по возрастанию"
    override fun compare(a: PageMeta, b: PageMeta) = a.numberOfViews - b.numberOfViews
}

class NumberOfViewsDESC : PageMetaSorter {
    override val id = "NUMBER_OF_VIEWS_DESC"
    override val title = "Просмотры: по убыванию"
    override fun compare(a: PageMeta, b: PageMeta) = b.numberOfViews - a.numberOfViews
}

class ReadingTimeASC : PageMetaSorter {
    override val id = "READING_TIME_ASC"
    override val title = "Время чтения: по возрастанию"
    override fun compare(a: PageMeta, b: PageMeta) =
        (a.readingTimeMinutes * 100 - b.readingTimeMinutes * 100).toInt()
}

class ReadingTimeDESC : PageMetaSorter {
    override val id = "READING_TIME_DESC"
    override val title = "Время чтения: по убыванию"
    override fun compare(a: PageMeta, b: PageMeta) =
        (b.readingTimeMinutes * 100 - a.readingTimeMinutes * 100).toInt()
}

class RatingASC : PageMetaSorter {
    override val id = "RATING_ASC"
    override val title = "Рейтинг: по возрастанию"
    override fun compare(a: PageMeta, b: PageMeta) = a.rating - b.rating
}

class RatingDESC : PageMetaSorter {
    override val id = "RATING_DESC"
    override val title = "Рейтинг: по убыванию"
    override fun compare(a: PageMeta, b: PageMeta) = b.rating - a.rating
}