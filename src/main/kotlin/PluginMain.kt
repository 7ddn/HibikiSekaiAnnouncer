package org.sddn.plugin.hibiki

import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.utils.info

object PluginMain : KotlinPlugin(
    JvmPluginDescription(
        id = "org.sddn.plugin.hibiki",
        name = "HibikiSekaiAnnouncer",
        version = "0.1.0"
    ) {
        author("七度")

        info("""
            音游プロジェクトセカイ feat.初音ミク的辅助插件
        """.trimIndent())

        // author 和 info 可以删除.
    }
) {
    override fun onEnable() {
        logger.info { "Plugin loaded" }
    }
}