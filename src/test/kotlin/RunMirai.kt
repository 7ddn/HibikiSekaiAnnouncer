package org.sddn.plugin.hibiki
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.enable
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.load
import net.mamoe.mirai.console.terminal.MiraiConsoleTerminalLoader

suspend fun main() {
    MiraiConsoleTerminalLoader.startAsDaemon()

    PluginMain.load()
    PluginMain.enable()

    /*val bot = MiraiConsole.addBot(123456, "") {
        fileBasedDeviceInfo()
    }.alsoLogin()

    MiraiConsole.job.join()*/
    GlobalScope.launch{
        //val ifOk =  Crawler.cardCrawler(12)
        //Crawler.cardPicCrawler(ifOk)
        Crawler.eventCrawler(3)
        // println(PluginData.cards[0].ifNormalCached)
    }

}