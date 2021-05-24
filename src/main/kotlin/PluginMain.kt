package org.sddn.plugin.hibiki

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.toMessageChain
import net.mamoe.mirai.utils.ExternalResource.Companion.uploadAsImage
import net.mamoe.mirai.utils.info
import java.io.File
import kotlin.random.Random

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

        PluginConfig.reload()

        PluginData.reload()

        GlobalScope.launch{
            println("working on card")
            var ifOk =  Crawler.cardCrawler(Int.MAX_VALUE)
            println("working on card pics")
            Crawler.cardPicCrawler(ifOk)
            println("working on event")
            // ifOk = Crawler.eventCrawler(Int.MAX_VALUE)
            if (ifOk == 0){
                // TODO: add a event alarm
            }
        }



        globalEventChannel().subscribeAlways<GroupMessageEvent> {

            messageEventHandler(message)

        }

    }


}