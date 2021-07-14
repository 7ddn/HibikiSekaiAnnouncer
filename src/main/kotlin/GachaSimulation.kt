package org.sddn.plugin.hibiki

import org.sddn.plugin.hibiki.beans.Gacha
import java.awt.RenderingHints
import java.io.File
import javax.imageio.ImageIO

object GachaSimulation {
    suspend fun buildGachaImage(
        cards: List<Int>,
        gachaID: Int,
        ifBonus: Boolean = false,
    ) :String{
        val bgFile = File("${PluginConfig.WorkingDir}pic\\rsc\\gachaTemplate.png")
        if (!bgFile.canRead()){
            val ifSuccess = Crawler.externalResourcesCrawler()
        }
        val bg = ImageIO.read(bgFile)

        val g2 = bg.createGraphics()
        g2.drawImage(bg, 0, 0, null)
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        //抽卡logo
        val gachaLogoFile = File("${PluginConfig.WorkingDir}pic\\gacha\\logo\\${gachaID}.png")
        if (!gachaLogoFile.canRead()){
            val ifSuccess = Crawler.gachaLogoCrawler(1)
        }
        val gachaLogo = ImageIO.read(gachaLogoFile)
        g2.drawImage(gachaLogo, 4, 156,560, 256, null)
        //这里是硬编码，需要改动

        for (i in cards.indices) {
            val card = PluginData.cards[cards[i] - 1]
            println(card.cardName)

            // 卡面
            val cardIconFile = File("${PluginConfig.WorkingDir}pic\\normal\\icon\\${cards[i]}_normal.png")
            if (!cardIconFile.canRead()) {
                val ifSuccess = Crawler.cardIconCrawler(Int.MAX_VALUE)
            }
            val cardIcon = ImageIO.read(cardIconFile)
            g2.drawImage(cardIcon, PluginConfig.iconCoordinate[i].first + 10, PluginConfig.iconCoordinate[i].second + 10, 230, 230, null)

            // 框架
            val cardFrameFile = File("${PluginConfig.WorkingDir}pic\\rsc\\cardFrame_${card.rarity}.png")
            // println(cardFrameFile.absoluteFile)
            if (!cardFrameFile.canRead()) {
                val ifSuccess = Crawler.externalResourcesCrawler()
            }
            val cardFrame = ImageIO.read(cardFrameFile)
            g2.drawImage(cardFrame, PluginConfig.iconCoordinate[i].first, PluginConfig.iconCoordinate[i].second, 250, 249, null)

            // 种类图标
            val attributeIconFile = File("${PluginConfig.WorkingDir}pic\\rsc\\attributeIcon_${card.attr}.png")
            if (!attributeIconFile.canRead()) {
                val ifSuccess = Crawler.externalResourcesCrawler()
            }
            val attributeIcon = ImageIO.read(attributeIconFile)
            g2.drawImage(attributeIcon, PluginConfig.iconCoordinate[i].first + 1, PluginConfig.iconCoordinate[i].second + 1, 55, 55,null)

            // 稀有度星星
            val starFile = File("${PluginConfig.WorkingDir}pic\\rsc\\rarityStar.png")
            if (!attributeIconFile.canRead()) {
                val ifSuccess = Crawler.externalResourcesCrawler()
            }
            val star = ImageIO.read(starFile)
            for (j in 0 until card.rarity) {
                g2.drawImage(star, PluginConfig.iconCoordinate[i].first + 13 + 35 * j , PluginConfig.iconCoordinate[i].second + 200, 35, 33, null)
            }

            //bonus缎带
            if (ifBonus) {
                // TODO: 上传缎带到图床 现在错了
                val bonusRibbonFile = File("${PluginConfig.WorkingDir}pic\\rsc\\rarityStar.png")
                if (!bonusRibbonFile.canRead()) {
                    val ifSuccess = Crawler.externalResourcesCrawler()
                }
                val bonusRibbon = ImageIO.read(bonusRibbonFile)
                g2.drawImage(bonusRibbon, 291, 716,309,57, null)
                //此处为硬编码，需要改动
            }




        }

        g2.dispose()
        OtherUtils.checkAndCreateWorkingDir("${PluginConfig.WorkingDir}pic\\tempPics\\")
        val resultFile = File("${PluginConfig.WorkingDir}pic\\tempPics\\${OtherUtils.generateNonce(8)}.png")
        println(resultFile.absolutePath)
        ImageIO.write(bg, "PNG", resultFile)

        return resultFile.absolutePath

    }

    fun getGachaResult(gacha: Gacha) :List<Int>{
        val contents = gacha.contents
        val pickups = gacha.pickups
        val r4content = mutableListOf<Int>()
        val r3content = mutableListOf<Int>()
        val r2content = mutableListOf<Int>()
        for (content in contents) {
            when (PluginData.cards[content - 1].rarity){
                2 -> {
                    r2content.add(content)
                }
                3 -> {
                    r3content.add(content)
                }
                4 -> {
                    r4content.add(content)
                }
            }
        }
        val results = mutableListOf<Int>()
        val pickupsNum = (200*0.004*pickups.size).toInt()
        val r4Num = (200.0*gacha.rarity4Rate).toInt() - pickupsNum
        val r3Num = (200.0*gacha.rarity3Rate).toInt()

        for (i in 1..10){
            // 先确认抽到的星级

            val rarity = when ((1..200).random()){
                in 1..pickupsNum -> {
                    5 // pickups
                }
                in pickupsNum + 1..pickupsNum + r4Num -> {
                    4
                }
                in pickupsNum + r4Num + 1..pickupsNum + r4Num + r3Num -> {
                    3
                }
                else -> {
                    if (i == 10) 3 else 2
                }
            }
            when (rarity){
                5 -> {
                    // pickups
                    results.add(pickups[(pickups.indices).random()])
                }
                4 -> {
                    results.add(r4content[(r4content.indices).random()])
                }
                3 -> {
                    results.add(r3content[(r3content.indices).random()])
                }
                2 -> {
                    results.add(r2content[(r2content.indices).random()])
                }
            }
        }

        return results

    }

}