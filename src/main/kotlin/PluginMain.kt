package org.sddn.plugin.hibiki

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.mamoe.mirai.Bot
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
import java.util.*
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
    lateinit var bot: Bot


    override fun onEnable() {
        logger.info { "Plugin loaded" }

        PluginConfig.reload()

        PluginData.reload()

        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"))

        launch {
            while (true) {
                try {
                    bot = Bot.instances[0]
                    bot
                    break
                } catch (e: Exception) {
                    logger.info("error : ${e.message}")
                    delay(1000)
                }
            }
        }

        GlobalScope.launch{
            refresh()
        }



        globalEventChannel().subscribeAlways<GroupMessageEvent> {

            messageEventHandler(message)

        }

    }

    suspend fun refresh() {
        println("getting card list")
        var ifOk = Crawler.cardCrawler(Int.MAX_VALUE)
        println("getting card pics")
        Crawler.cardPicCrawler(ifOk)
        println("getting event list")
        ifOk = Crawler.eventCrawler(Int.MAX_VALUE)
        if (ifOk != 666) { // just to make sure this will work after get newest event
            // add event alarm
            val event = PluginData.events.last() //latest event
            if (!event.alarmApplied) {
                // alarm before start
                val toSayBefore = PlainText("活动${event.name}还有1小时就要开始了哦").toMessageChain()

                // alarm before score end
                val toSayBeforeScoreEndDay = PlainText("活动${event.name}今天就要截止了，要注意自己的排名呢")
                    .toMessageChain()
                val toSayBeforeScoreEndHour = PlainText(
                    "活动${event.name}还有1小时就要截止了，" +
                        "可以考虑冲刺排名了哦\n" +
                        "活动结束后的live也别忘了呢"
                ).toMessageChain()

                // alarm before end
                val toSayBeforeEnd = PlainText("活动${event.name}今天就要结束了，注意活动剧情回收了吗？")
                    .toMessageChain()

                PluginData.autoEventAlarmGroupList.forEach {
                    println("now adding alarms for group $it")
                    val group = bot.getGroup(it)
                    Alarm.addAlarmToSendMessage(
                        event.startTime - 3600000,
                        toSayBefore,
                        group!!
                    )
                    Alarm.addAlarmToSendMessage(
                        event.scoreStopTime - 43200000,
                        toSayBeforeScoreEndDay,
                        group
                    )
                    Alarm.addAlarmToSendMessage(
                        event.scoreStopTime - 3600000,
                        toSayBeforeScoreEndHour,
                        group
                    )
                    Alarm.addAlarmToSendMessage(
                        event.endTime - 3600000,
                        toSayBeforeEnd,
                        group
                    )
                    group.sendMessage("已经为活动${event.name}添加自动提醒")
                }
                PluginData.events.last().alarmApplied = true
            }
        }

        // 检查alarm列表
        val nowTime : Long = Calendar.getInstance(Locale.CHINA).timeInMillis
        PluginData.alarms.forEach {
            if (it.time - nowTime < -20000) {
                PluginData.alarms.remove(it)
            } else {
                Alarm.addAlarmToSendMessage(
                    timeInMillis = it.time,
                    notice = it.message,
                    contactId = it.contactId,
                    bot
                )
                println("从缓存中回收了${it.time}的闹钟")
            }
        }

    }


}