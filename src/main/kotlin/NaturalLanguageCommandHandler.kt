package org.sddn.plugin.hibiki

import net.mamoe.mirai.console.command.CommandSender.Companion.toCommandSender
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.utils.ExternalResource.Companion.uploadAsImage
import java.io.File

suspend fun GroupMessageEvent.messageEventHandler(message: Message) {
    val messageText = message.contentToString()
    //println("I'm called = ${group.botAsMember.nick}")

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
        val patternTeachNickname = Regex("其实(.+)就是(.+)$")
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
        }
        return
    }
}