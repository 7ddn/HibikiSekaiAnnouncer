package org.sddn.plugin.hibiki

import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.utils.ExternalResource.Companion.uploadAsImage
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

suspend fun GroupMessageEvent.messageEventHandler(message: Message) {
    val messageText = message.contentToString()
    //println("I'm called = ${group.botAsMember.nick}")


    // 随机卡片
    val patternRandomCard = Regex("^随一个(.+)$")
    if (patternRandomCard.matches(messageText)) {
        PluginMain.logger.info("随点啥")
        val charName = patternRandomCard.findAll(messageText).map { it.groupValues[1] }.joinToString()
        val charId = OtherUtils.charNameToID(charName)
        if (charId == -1) {
            group.sendMessage("呜呜 我好像不认识${charName}是谁")
        }
        println("charId = $charId\n")
        println(PluginData.chara2card.toString() + "\n")
        val relativeId = (0 until PluginData.chara2card[charId]!!.size).random()
        println("relativeId = $relativeId\n")
        val card = PluginData.cards[PluginData.chara2card[charId]!![relativeId] - 1]
        print("cardId = ${card.id}\n")
        var toSay = PlainText("${card.cardName} 种类:${card.attr} 稀有度:${card.rarity}\n\r").toMessageChain()
        val picNormal = File("""${PluginConfig.WorkingDir}pic\normal\${card.id}_normal.png""").inputStream()
        toSay += Image(
            picNormal.uploadAsImage(group).imageId
        )
        if (card.rarity > 2) {
            val picTrained = File("${PluginConfig.WorkingDir}pic\\trained\\${card.id}_trained.png").inputStream()
            toSay += Image(
                picTrained.uploadAsImage(group).imageId
            )
        }
        group.sendMessage(toSay)
        return
    }

    // @相关处理
    //println(messageText.contains("@${bot.id}"))
    //println("text:$messageText\nid:@${bot.id}")
    if (messageText.contains("@${bot.id}")) {

        // 别名控制
        val patternTeachNickname = Regex("其实(.+)就是(.+)$")
        val patternForgetNickname = Regex("其实(.+)不是(.+)$")
        //println("${patternTeachNickname.containsMatchIn(messageText)}\n")
        if (patternTeachNickname.containsMatchIn(messageText)) {
            //println("matched")
            val matches = patternTeachNickname.findAll(messageText)
            val name1 = matches.map { it.groupValues[1] }.joinToString()
            val name2 = matches.map { it.groupValues[2] }.joinToString()
            val id1 = OtherUtils.charNameToID(name1)
            val id2 = OtherUtils.charNameToID(name2)
            if (id1 != -1) {
                if (id2 != -1) {
                    // 2是1 都已知
                    group.sendMessage(At(sender) + PlainText("哼，我本来就知道${name1}是${name2}呢"))
                    return
                } else {
                    // 2是1 2未知
                    PluginData.chara2nickname[id1]!!.add(name2)
                    group.sendMessage(At(sender) + PlainText("以后我就知道${name1}就是${name2}了哦"))
                }
            } else {
                if (id2 != -1) {
                    // 1是2 1未知
                    PluginData.chara2nickname[id2]!!.add(name1)
                    group.sendMessage(At(sender) + PlainText("以后我就知道${name2}就是${name1}了哦"))
                } else {
                    //都未知
                    group.sendMessage(At(sender) + PlainText("呜呜,${name1}和${name2}我好像一个都不认识呢"))
                }
            }
            return
        } else if (patternForgetNickname.containsMatchIn(messageText)) {

            val matches = patternForgetNickname.findAll(messageText)
            val name1 = matches.map { it.groupValues[1] }.joinToString()
            val name2 = matches.map { it.groupValues[2] }.joinToString()
            val id1 = OtherUtils.charNameToID(name1)
            val id2 = OtherUtils.charNameToID(name2)
            if (id1 != 1) {
                if (id2 == id1) {
                    // 都已知且有关
                    PluginData.chara2nickname[id1]!!.remove(name2)
                    group.sendMessage(At(sender) + PlainText("以后我就不把${name1}当成${name2}了喔"))
                } else {
                    if (id2 != -1) {
                        // 都已知但无关
                        group.sendMessage(At(sender) + PlainText("${name1}和${name2}好像本来就不是同一个人呢"))
                    } else {
                        //2未知
                        group.sendMessage(At(sender) + PlainText("哎呀，我好像不认识${name2}呢"))
                    }
                }
            } else {
                if (id2 != -1) {
                    group.sendMessage(At(sender) + PlainText("あれ？${name1}是谁呀"))
                } else {
                    group.sendMessage(At(sender) + PlainText("呜呜,${name1}和${name2}我好像一个都不认识呢"))
                }
            }
            return
        }


        // 计时器控制(试制)
        val patternSetAlarm = Regex(
            "(((\\d{1,2}月)?(\\d{1,2}号))|(今天|明天|后天))?(\\d{2}:\\d{2}:?(\\d{2})?)叫我(.+)\$")
        if (patternSetAlarm.containsMatchIn(messageText)){
            val matches = patternSetAlarm.findAll(messageText)
            val day = matches.map{it.groupValues[1]}.joinToString()
            val time1 = matches.map{it.groupValues[6]}.joinToString()
            val hour = time1.substring(0,2).toInt()
            val minute = time1.substring(3,5).toInt()
            var second = matches.map{it.groupValues[7]}.joinToString()
            val notice = matches.map{it.groupValues[8]}.joinToString()
            // val formatDate = SimpleDateFormat("yyyy-MM-dd")
            // val nowDate = formatDate.format(Date())
            val date = Calendar.getInstance(Locale.CHINA)
            date.set(Calendar.HOUR_OF_DAY, hour)
            date.set(Calendar.MINUTE, minute)

            if (second == "") second = "0"
            date.set(Calendar.SECOND, second.toInt())

            // val formatTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            // val time = formatTime.parse("$date $time1:$time2")
            when (day) {
                "明天" -> date.add(Calendar.DATE, 1)
                "后天" -> date.add(Calendar.DATE, 2)
                "", "今天" -> Unit
                else -> {
                    val dayString = matches.map{it.groupValues[4]}.joinToString()
                    val dayOfMonth = dayString.substring(0,dayString.length-1).toInt()
                    println(dayOfMonth)
                    date.set(Calendar.DATE, dayOfMonth)
                    val monthString = matches.map{it.groupValues[3]}.joinToString()
                    if (monthString != ""){
                        val month = monthString.substring(0,monthString.length-1).toInt()
                        date.set(Calendar.MONTH, month - 1)
                    }
                }
            }
            Alarm.addAlarmToSendMessage(
                date.timeInMillis,
                At(sender).toMessageChain()+PlainText(" 快去$notice"),
                group
            )
            group.sendMessage("添加${date.time}的闹钟成功")
            return
        }

    }
}