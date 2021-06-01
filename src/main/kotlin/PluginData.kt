package org.sddn.plugin.hibiki

import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.value
import org.sddn.plugin.hibiki.beans.Alarm
import org.sddn.plugin.hibiki.beans.Card
import org.sddn.plugin.hibiki.beans.Event
import org.sddn.plugin.hibiki.beans.Song

object PluginData : AutoSavePluginData("data"){
    val cards : MutableList<Card> by value()
    val songs : MutableList<Song> by value()
    val events : MutableList<Event> by value()

    val chara2card : MutableMap<Int, MutableList<Int>> by value()
    // TODO: val chara2song : MutableMap<Int, Int> by value()
    val chara2nickname : MutableMap<Int, MutableSet<String>> by value(mutableMapOf(
        1 to mutableSetOf("星乃一歌", "一歌", "ick"),
        2 to mutableSetOf("天马咲希", "咲希", "saki"),
        3 to mutableSetOf("望月穂波", "honami"),
        4 to mutableSetOf("日野森志步", "志步", "shiho"),
        5 to mutableSetOf("花里实乃里", "minori", "xcw", "实乃里"),
        6 to mutableSetOf("桐谷遥", "遥", "hrk"),
        7 to mutableSetOf("桃井爱莉", "airi", "爱莉"),
        8 to mutableSetOf("日夜森雫", "szk", "雫"),
        9 to mutableSetOf("小豆泽心羽", "khn", "仓鼠"),
        10 to mutableSetOf("白石杏", "an", "an酱", "杏"),
        11 to mutableSetOf("东云彰人", "东云弟", "akito"),
        12 to mutableSetOf("青柳冬弥", "toya", "冬弥"),
        13 to mutableSetOf("天马司", "司"),
        14 to mutableSetOf("凤绘梦", "emu"),
        15 to mutableSetOf("草雉宁宁", "nene"),
        16 to mutableSetOf("神代类", "rui"),
        17 to mutableSetOf("宵崎奏", "奏", "knd"),
        18 to mutableSetOf("朝比奈真冬", "mfy", "马忽悠"),
        19 to mutableSetOf("东云绘名", "enna", "enana", "ena"),
        20 to mutableSetOf("晓山瑞希", "mzk", "瑞希"),
        21 to mutableSetOf("初音未来", "miku"),
        22 to mutableSetOf("镜音铃", "rin", "lin"),
        23 to mutableSetOf("镜音连", "ren", "len"),
        24 to mutableSetOf("巡音流歌", "luka"),
        25 to mutableSetOf("MEIKO", "大姐"),
        26 to mutableSetOf("KAITO", "大哥"),
    ))

    var maxCardId : Int by value(0)
    var maxEventId : Int by value(0)
    val songIDList : MutableSet<Int> by value()

    val alarms : MutableSet<Alarm> by value(mutableSetOf())
    val autoEventAlarmGroupList : MutableSet<Long> by value(mutableSetOf())
}