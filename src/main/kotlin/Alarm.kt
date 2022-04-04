package org.sddn.plugin.hibiki

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.mamoe.mirai.Bot
import net.mamoe.mirai.console.plugin.info
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.message.code.MiraiCode.deserializeMiraiCode
import net.mamoe.mirai.message.data.MessageChain
import org.sddn.plugin.hibiki.beans.Alarm
import java.sql.Time
import kotlin.math.abs

object Alarm {
    fun addAlarmToSendMessage(timeInMillis: Long, notice: MessageChain, contact: Contact){
        val alarm = Alarm(
            timeInMillis,
            notice.serializeToMiraiCode(),
            "group", //TODO: implement user alarm
            contact.id
        )
        PluginData.alarms.add(alarm)
        println("added alarm at $timeInMillis")
        GlobalScope.launch {
            while (true) {
                if (timeInMillis - System.currentTimeMillis() < -20000){
                    PluginData.alarms.remove(alarm)
                    PluginMain.logger.info("Alarm \"$notice\" have been outdated and is now removed ")
                } else if (timeInMillis - System.currentTimeMillis() < 2000){
                    contact.sendMessage(notice)
                    PluginData.alarms.remove(alarm)
                    break
                } else {
                    delay((timeInMillis - System.currentTimeMillis())/2)
                }
            }
        }
    }

    fun addAlarmToSendMessage(timeInMillis: Long, notice: String, contactId: Long, bot: Bot){
        //默认是群id

        val message = notice.deserializeMiraiCode()
        val contact = bot.getGroup(contactId)
        if (contact != null) {
            addAlarmToSendMessage(timeInMillis, message, contact)
        }
    }
}