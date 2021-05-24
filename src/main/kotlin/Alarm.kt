package org.sddn.plugin.hibiki

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.message.data.MessageChain
import java.sql.Time
import kotlin.math.abs

object Alarm {
    fun addAlarmToSendMessage(timeInMillis: Long, notice: MessageChain, contact: Contact){
        GlobalScope.launch {
            while (true) {
                if (abs(System.currentTimeMillis() - timeInMillis) < 2000){
                    contact.sendMessage(notice)
                }
            }
        }
    }
}