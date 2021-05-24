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

    val APIs: MutableMap<String, String> by value(
        mutableMapOf(
            "song" to "https://api.pjsek.ai/database/master/musics?id=",
            "card" to "https://api.pjsek.ai/database/master/cards?id=",
            "event" to "https://api.pjsek.ai/database/master/events?id=",
            //TODO: "vocal" to "https://api.pjsek.ai/database/master/musicVocals?musicId=",
            "difficulties" to "https://api.pjsek.ai/database/master/musicDifficulties?musicId=",
        )

    )

}