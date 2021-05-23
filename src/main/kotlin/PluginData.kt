package org.sddn.plugin.hibiki

import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.value
import org.sddn.plugin.hibiki.beans.Card
import org.sddn.plugin.hibiki.beans.Song

object PluginData : AutoSavePluginData("data"){
    val cards : MutableList<Card> by value()
    val songs : MutableList<Song> by value()

    val chara2card : MutableMap<Int, MutableSet<Int>> by value()
    // TODO: val chara2song : MutableMap<Int, Int> by value()

    var maxCardId : Int by value(0)
    val songIDList : MutableSet<Int> by value()
}