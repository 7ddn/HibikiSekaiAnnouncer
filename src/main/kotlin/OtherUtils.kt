package org.sddn.plugin.hibiki

import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.toMessageChain
import net.mamoe.mirai.message.data.toPlainText
import java.io.File

object OtherUtils {
    fun intTo3Word(id : Int):String{
        return when {
            id < 10 -> {
                "00$id"
            }
            id < 100 -> {
                "0$id"
            }
            id <= 999 -> {
                id.toString()
            }
            else -> throw Exception("Too Large Input")
        }
    }

    private fun mkDir(dirPath: String) {

        val dirArray = dirPath.split("""\\""".toRegex())
        var pathTemp = ""
        for (i in 1 until dirArray.size) {
            pathTemp = "$pathTemp/${dirArray[i]}"
            val newF = File("${dirArray[0]}$pathTemp")
            if (!newF.exists()) {
                val cheatDir: Boolean = newF.mkdir()
                println("MkDir" + if (cheatDir) "successfully at ${newF.absolutePath} " else "failed")
            }
        }

    }

    fun checkAndCreateWorkingDir(dirPath: String) {
        val workingDir = File(dirPath)
        if (!workingDir.exists()) mkDir(dirPath) //else println("${workingDir.absoluteFile} exists")
    }

    fun charNameToID(name: String):Int{
        for (i in 1 .. PluginData.chara2nickname.size) {
            if (name in PluginData.chara2nickname[i]!!) return i
        }
        return -1
    }

    suspend fun sendAndSplitToUnder100 (message : PlainText, target: Contact) : MessageChain { //将要发送的PlainText拆分成最长99的字节并发送
        PluginMain.logger.info("正在分割长句$message")
        var messageText = message.content
        while (messageText.length > 99){
            val sub100 =  messageText.substring(0, 99)
            target.sendMessage(sub100.toPlainText())
            messageText = messageText.substring(99, messageText.length)
        }
        return messageText.toPlainText().toMessageChain()
    }
}