package org.sddn.plugin.hibiki

import com.alibaba.fastjson.JSON
import kotlinx.coroutines.delay

import org.sddn.plugin.hibiki.beans.Card
import org.sddn.plugin.hibiki.beans.Event
import org.sddn.plugin.hibiki.beans.Gacha
import java.io.File


object Crawler {

    suspend fun externalResourcesCrawler(): Int {

        OtherUtils.checkAndCreateWorkingDir("${PluginConfig.WorkingDir}pic\\rsc\\")

        // 抽卡模板
        try {
            val tFile = File("${PluginConfig.WorkingDir}pic\\rsc\\gachaTemplate.png")
            if (!tFile.exists()) {
                val tUrl = PluginConfig.ExternalResourceUrls["gachaTemplate"]
                if (tUrl == null) {
                    PluginMain.logger.info("没有设置抽卡模板地址!")
                    throw(Exception("unavailable template url"))
                }
                HttpUtils.getImageFromUrlOrSave(tFile, tUrl)
                if (tFile.canRead()) {
                    PluginMain.logger.info("缓存抽卡模板成功")
                }
            }
        } catch (e:Exception) {
            PluginMain.logger.info(e.message)
        }

        // 星星
        try {
            val starFile = File("${PluginConfig.WorkingDir}pic\\rsc\\rarityStar.png")
            if (!starFile.exists()) {
                val starUrl = PluginConfig.ExternalResourceUrls["rarityStar"]
                if (starUrl == null) {
                    PluginMain.logger.info("没有设置稀有度星星地址!")
                    throw(Exception("unavailable template url"))
                }
                HttpUtils.getImageFromUrlOrSave(starFile, starUrl)
                if (starFile.canRead()) {
                    PluginMain.logger.info("缓存稀有度星星成功")
                }
            }
        } catch (e:Exception) {
            PluginMain.logger.info(e.message)
        }

        // 卡片框架
        try {
            for (i: Int in 1..4) {
                val cardFrameFile = File("${PluginConfig.WorkingDir}pic\\rsc\\cardFrame_${i}.png")
                if (!cardFrameFile.exists()) {
                    val cardFrameUrl = PluginConfig.ExternalResourceUrls["cardFrame"]
                    if (cardFrameUrl == null) {
                        PluginMain.logger.info("没有设置卡片框架地址!")
                        throw(Exception("unavailable card frame url"))
                    }
                    HttpUtils.getImageFromUrlOrSave(cardFrameFile, "$cardFrameUrl$i.png")
                    if (cardFrameFile.canRead()) {
                        PluginMain.logger.info("缓存卡片框架成功")
                    }
                }
            }

        } catch (e:Exception) {
            PluginMain.logger.info(e.message)
        }

        // 类别图标
        val attributes = setOf("cool", "mysterious", "happy", "cute", "pure")

        try {
            for (attribute in attributes) {
                val attributeIconFile = File("${PluginConfig.WorkingDir}pic\\rsc\\attributeIcon_${attribute}.png")
                if (!attributeIconFile.exists()) {
                    val attributeIconUrl = PluginConfig.ExternalResourceUrls["attributeIcon"]
                    if (attributeIconUrl == null) {
                        PluginMain.logger.info("没有设置类别图标地址!")
                        throw(Exception("unavailable card frame url"))
                    }
                    HttpUtils.getImageFromUrlOrSave(attributeIconFile, "$attributeIconUrl$attribute.png")
                    if (attributeIconFile.canRead()) {
                        PluginMain.logger.info("缓存类别图标成功")
                    }
                }
            }

        } catch (e:Exception) {
            PluginMain.logger.info(e.message)
        }

        return 1

    }

    suspend fun cardCrawler(maxID : Int): Int {
        var cardID = PluginData.maxCardId
        while (true) {
            if (cardID > maxID) {
                PluginData.maxCardId = cardID
                return 0
            }
            cardID++
            try {
                val cardUrl = PluginConfig.APIs["card"] + OtherUtils.intTo3Word(cardID)
                val response = HttpUtils.httpGet(cardUrl)
                if (response == null || !response.isSuccessful) {
                    // cardID++
                    continue
                }
                val card = JSON.parseObject(response.body!!.string())
                val count = card.getString("total").toInt()
                if (count == 0) {
                    /* 由于不明原因(废案?) 初期卡各自留了1个四星卡的空位，因此108号之前的卡片并不是连号的
                    * 故此处对编号进行特判，如果在108号之前可以认为并未加载完全部新卡
                     */
                    if (cardID <= 108) {
                        PluginData.cards.add(Card(id = cardID, cardName = "unavailable"))
                        continue
                    } else {
                        PluginData.maxCardId = cardID - 1
                        PluginMain.logger.info("读取到最新的卡牌ID为${cardID - 1}")
                        break
                    }

                } else {
                    val data = card.getJSONArray("data").getJSONObject(0)
                    // PluginMain.logger.info(data.toString())
                    val id = data.getIntValue("id")
                    val characterID = data.getIntValue("characterId")
                    val cardName = data.getString("prefix")
                    val cardSkillName = data.getString("cardSkillName")
                    val attr = data.getString("attr")
                    val rarity = data.getIntValue("rarity")
                    val cardSkillID = data.getIntValue("skillId")
                    val relativeID = data.getString("assetbundleName").substring(9).toInt()

                    val newCard = Card(
                        id = id,
                        characterID = characterID,
                        cardName = cardName,
                        cardSkillName = cardSkillName,
                        attr = attr,
                        rarity = rarity,
                        cardSkillID = cardSkillID,
                        relativeID = relativeID
                    )

                    PluginData.cards.add(newCard)
                    if (PluginData.chara2card[characterID] == null) PluginData.chara2card[characterID] = mutableListOf()
                    PluginData.chara2card[characterID]!!.add(newCard.id)
                    PluginMain.logger.info("添加新卡片 id = $id")
                    response.close()
                    delay(50L)
                }


                // PluginData.cards.

            } catch (e: Exception) {
                PluginMain.logger.info(e.message)
                return -1
            }
        }
        return 0
    }

    suspend fun cardPicCrawler(ifCardOk: Int) {
        OtherUtils.checkAndCreateWorkingDir("${PluginConfig.WorkingDir}pic\\normal\\")
        OtherUtils.checkAndCreateWorkingDir("${PluginConfig.WorkingDir}pic\\trained\\")

        val cards = PluginData.cards

        /*测试用
        cards.clear()
        cards.add(Card(
            characterID = 20,
            relativeID = 8,
            rarity = 4
        ))
        测试用结束*/

        cards.forEach {
            if (it.cardName == "unavailable") return@forEach // 同上 前108号中有缺失的部分
            if (it.ifNormalCached && (it.rarity <= 2 || it.ifTrainedCached)) return@forEach
            val characterID = OtherUtils.intTo3Word(it.characterID)
            val relativeID = OtherUtils.intTo3Word(it.relativeID)

            try {
                val urlNormal = HttpUtils.cardNormalUrlGenerate(characterID, relativeID)
                val fileNormal = File("${PluginConfig.WorkingDir}pic\\normal\\${it.id}_normal.png")
                HttpUtils.getImageFromUrlOrSave(fileNormal, urlNormal)
                if (fileNormal.canRead()) {
                    it.ifNormalCached = true
                    PluginMain.logger.info("添加新特训前卡图 id = ${it.id}")
                } else {
                    PluginMain.logger.info("读取新特训前卡图 id = ${it.id}失败")
                }
                if (it.rarity > 2) {
                    val urlAfterTraining = HttpUtils.cardAfterTrainingUrlGenerate(characterID, relativeID)
                    val fileTraining = File("${PluginConfig.WorkingDir}pic\\trained\\${it.id}_trained.png")
                    HttpUtils.getImageFromUrlOrSave(fileTraining, urlAfterTraining)
                    if (fileTraining.canRead()) {
                        it.ifTrainedCached = true
                        PluginMain.logger.info("添加新特训后卡图 id = ${it.id}")
                    } else {
                        PluginMain.logger.info("读取新特训后卡图 id = ${it.id}失败")
                    }
                }
            } catch (e: Exception) {
                PluginMain.logger.info(e.message)
            }
        }

    }

    suspend fun cardIconCrawler(ifCardOk: Int): Int {
        OtherUtils.checkAndCreateWorkingDir("${PluginConfig.WorkingDir}pic\\normal\\icon\\")
        OtherUtils.checkAndCreateWorkingDir("${PluginConfig.WorkingDir}pic\\trained\\icon\\")

        var cards = PluginData.cards

        cards.forEach {
            if (it.cardName == "unavailable") return@forEach
            val characterID = OtherUtils.intTo3Word(it.characterID)
            val relativeID = OtherUtils.intTo3Word(it.relativeID)

            try {
                val fileNormal = File("${PluginConfig.WorkingDir}pic\\normal\\icon\\${it.id}_normal.png")
                if (!fileNormal.exists()) {
                    val urlNormal = HttpUtils.iconNormalUrlGenerate(characterID, relativeID)
                    HttpUtils.getImageFromUrlOrSave(fileNormal, urlNormal)
                    if (fileNormal.canRead()) {
                        PluginMain.logger.info("添加新特训前图标 id = ${it.id}")
                    } else {
                        PluginMain.logger.info("读取新特训前图标 id = ${it.id}失败")
                    }
                }

                if (it.rarity > 2){
                    val fileTraining = File("${PluginConfig.WorkingDir}pic\\trained\\icon\\${it.id}_trained.png")
                    if (!fileTraining.exists()) {
                        val urlAfterTraining = HttpUtils.iconAfterTrainingUrlGenerate(characterID, relativeID)
                        HttpUtils.getImageFromUrlOrSave(fileTraining, urlAfterTraining)
                        if (fileTraining.canRead()) {
                            PluginMain.logger.info("添加新特训后图标 id = ${it.id}")
                        } else {
                            PluginMain.logger.info("读取新特训后图标 id = ${it.id}失败")
                        }
                    }
                }
            } catch (e: Exception) {
                PluginMain.logger.info(e.message)
            }
        }

        return 1
    }

    suspend fun eventCrawler(maxID: Int) : Int{
        var eventID = PluginData.maxEventId
        while (true) {
            if (eventID > maxID) return 0
            eventID++
            try {
                val eventUrl = PluginConfig.APIs["event"] + OtherUtils.intTo3Word(eventID)
                val response = HttpUtils.httpGet(eventUrl)
                if (response == null || !response.isSuccessful) {

                    continue
                }
                val event = JSON.parseObject(response.body!!.string())
                val count = event.getString("total").toInt()
                if (count == 0) {

                    PluginData.maxEventId = eventID - 1
                    PluginMain.logger.info("读取到最新的活动ID为${eventID - 1}")
                    break


                } else {
                    val data = event.getJSONArray("data").getJSONObject(0)
                    val id = data.getIntValue("id")
                    val name = data.getString("name")
                    val startTime = data.getLong("startAt")
                    val endTime = data.getLong("closedAt")
                    val scoreCloseTime = data.getLong("aggregateAt")

                    val newEvent = Event(
                        id = id,
                        name = name,
                        startTime = startTime,
                        endTime = endTime,
                        scoreStopTime = scoreCloseTime,
                        alarmApplied = false
                    )

                    PluginData.events.add(newEvent)
                    PluginMain.logger.info("添加新活动 id = $id")
                    response.close()
                    delay(500L)
                }


            } catch (e: Exception) {
                PluginMain.logger.info(e.message)
                return -1
            }
        }
        return 0
    }

    suspend fun gachaCrawler(maxId: Int) : Int{
        var gachaID = PluginData.maxGachaId
        while (true) {
            if (gachaID > maxId) return 0
            gachaID++
            try {
                val gachaUrl = PluginConfig.APIs["gacha"] + OtherUtils.intTo3Word(gachaID)
                val response = HttpUtils.httpGet(gachaUrl)
                if (response == null || !response.isSuccessful) {
                    continue
                }
                val gacha = JSON.parseObject(response.body!!.string())
                val count = gacha.getString("total").toInt()
                if (count == 0){
                    PluginData.maxGachaId = gachaID - 1
                    PluginMain.logger.info("读取到最新的抽卡ID为${gachaID - 1}")
                    break
                } else {
                    val data = gacha.getJSONArray("data").getJSONObject(0)
                    val id = data.getIntValue("id")
                    val name = data.getString("name")
                    val rarity1Rate = data.getDouble("rarity1Rate")/100.0
                    val rarity2Rate = data.getDouble("rarity2Rate")/100.0
                    val rarity3Rate = data.getDouble("rarity3Rate")/100.0
                    val rarity4Rate = data.getDouble("rarity4Rate")/100.0
                    val startTime = data.getLong("startAt")
                    val endTime = data.getLong("endAt")

                    val detail = data.getJSONArray("gachaDetails")
                    val contents = mutableListOf<Int>()


                    for (i: Int in 0 until detail.size){
                        val gachaItem = detail.getJSONObject(i)
                        val cardId = gachaItem.getIntValue("cardId")
                        contents.add(cardId)
                    }

                    val gachaPickups = data.getJSONArray("gachaPickups")
                    val pickups = mutableListOf<Int>()

                    for (i: Int in 0 until gachaPickups.size){
                        val pickupItem = gachaPickups.getJSONObject(i)
                        val pickupId = pickupItem.getIntValue("cardId")
                        pickups.add(pickupId)
                    }


                    val newGacha = Gacha(
                        id = id,
                        name = name,
                        rarity1Rate = rarity1Rate,
                        rarity2Rate = rarity2Rate,
                        rarity3Rate = rarity3Rate,
                        rarity4Rate = rarity4Rate,
                        startTime = startTime,
                        endTime = endTime,
                        contents = contents,
                        pickups = pickups,
                    )

                    PluginData.gachas.add(newGacha)
                    PluginMain.logger.info("添加新抽卡 id = $id")
                    response.close()
                    delay(500L)


                }

            } catch (e: Exception) {
                PluginMain.logger.info(e.message)
                return -1
            }
        }
        return 0
    }



}