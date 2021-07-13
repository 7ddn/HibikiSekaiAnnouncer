package org.sddn.plugin.hibiki

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

    fun generateNonce(size: Int): String {
        val nonceScope = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val scopeSize = nonceScope.length
        val nonceItem: (Int) -> Char = { nonceScope[(scopeSize * Math.random()).toInt()] }
        return Array(size, nonceItem).joinToString("")
    }

}