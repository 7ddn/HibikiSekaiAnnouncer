package org.sddn.plugin.hibiki
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.enable
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.load
import net.mamoe.mirai.console.terminal.MiraiConsoleTerminalLoader
import org.sddn.plugin.hibiki.beans.Gacha

suspend fun main() {
    MiraiConsoleTerminalLoader.startAsDaemon()

    PluginMain.load()
    // PluginMain.enable()

    /*val bot = MiraiConsole.addBot(123456, "") {
        fileBasedDeviceInfo()
    }.alsoLogin()

    MiraiConsole.job.join()*/
    GlobalScope.launch{
        val ifOk =  Crawler.cardCrawler(Int.MAX_VALUE)
        //Crawler.cardPicCrawler(ifOk)
        //Crawler.eventCrawler(3)
        Crawler.gachaCrawler(1)
        //Crawler.externalResourcesCrawler()
        Crawler.cardIconCrawler(ifOk)
        /* val testGacha = Gacha(
            rarity2Rate = 0.885,
            rarity3Rate = 0.0885,
            rarity4Rate = 0.03,
            contents = listOf(
                2,3,4,6,7,10,11,14,15,18,19,
            ),
            pickups = listOf(
                4,
            )
        )*/

        GachaSimulation.buildGachaImage(
            GachaSimulation.getGachaResult(PluginData.gachas[0])
        )
        //println(PluginData.gachas[0].contents.size)
        // println(PluginData.cards[0].ifNormalCached)
    }

}