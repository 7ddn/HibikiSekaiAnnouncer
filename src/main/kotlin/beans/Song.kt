package org.sddn.plugin.hibiki.beans

import kotlinx.serialization.Serializable

@Serializable
data class Song (
        var id: String = "",
        var title: String = "",
        var lyricist: String = "",
        var composer: String = "",
        var easy: Int = 0,
        var normal: Int = 0,
        var hard: Int = 0,
        var expert: Int = 0,
        var master: Int = 0,
)