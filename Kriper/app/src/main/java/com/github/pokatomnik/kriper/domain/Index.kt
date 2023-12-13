package com.github.pokatomnik.kriper.domain

import java.time.Instant

class Index(
    /**
     * Map of stories.
     * Key is the identifier of the story
     * Value is the metainfo of the story.
     */
    val pageMeta: Map<String, PageMeta>,

    /**
     * Complex map that looks like a tree of tags (max 2 levels):
     * - TAG_GROUP1
     *   - Tag1
     *      - tagName: Tag1
     *      - pages: ['id_of_story_1', 'id_of_story_2']
     *   - Tag2
     *      - tagName: Tag2
     *      - pages: ['id_of_story_3', 'id_of_story4']
     * - TAG_GROUP2
     *   - Tag3
     *     - tagName: Tag3
     *     - pages: ['id_of_story5', 'id_of_story6']
     */
    val tagsMap: Map<String, Map<String, Tag>>,

    /**
     * Dictionary of top story identifiers
     */
    val top: Top,

    /**
     * New Year 2024 story ids. New year stories.
     * TODO: remove after holidays
     */
    val ny2024: Set<String>,

    /**
     * When the dump created
     */
    val dateCreatedISO: Instant,
)