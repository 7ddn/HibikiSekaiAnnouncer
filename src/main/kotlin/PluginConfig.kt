package org.sddn.plugin.hibiki

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.value

object PluginConfig : AutoSavePluginConfig("config") {
    val UserAgent: String by value(
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
            "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.212 Safari/537.36"
    )

    val WorkingDir: String by value(
        "\\"
    )

    val ExternalResourceUrls: MutableMap<String, String> by value(
        mutableMapOf(
            "gachaTemplate" to "https://z3.ax1x.com/2021/07/12/WisHaT.png",
            "cardFrame" to "https://pjsek.ai/images/member/cardFrame_S_",
            "rarityStar" to "https://pjsek.ai/images/member/rarity_star_normal.png",
            "attributeIcon" to "https://pjsek.ai/images/member/icon_attribute_",
        )
    )

    val APIs: MutableMap<String, String> by value(
        mutableMapOf(
            "song" to "https://api.pjsek.ai/database/master/musics?id=",
            "card" to "https://api.pjsek.ai/database/master/cards?id=",
            "event" to "https://api.pjsek.ai/database/master/events?id=",
            "gacha" to "https://api.pjsek.ai/database/master/gachas?id=",
            //TODO: "vocal" to "https://api.pjsek.ai/database/master/musicVocals?musicId=",
            "difficulties" to "https://api.pjsek.ai/database/master/musicDifficulties?musicId=",
        )

    )

    val iconCoordinate: List<Pair<Int, Int>> by value(
        listOf(
            Pair(320, 494),
            Pair(608, 494),
            Pair(896, 494),
            Pair(1184, 494),
            Pair(1472, 494),

            Pair(320, 782),
            Pair(608, 782),
            Pair(896, 782),
            Pair(1184, 782),
            Pair(1472, 782),
        )
    )

}