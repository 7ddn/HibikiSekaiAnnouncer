package org.sddn.plugin.hibiki.beans

import kotlinx.serialization.Serializable

@Serializable
data class Card(
    var id: Int = 0, //卡片id
    var cardName: String = "", //卡片名称
    var cardSkillName: String = "", //卡片技能名称
    var characterID: Int = 0, //所属角色id
    var relativeID: Int = 0, //相对id,即在此角色分类下的id
    var rarity: Int = 0, //稀有度
    var attr: String = "", //颜色
    var cardSkillID: Int = 0, //技能编号
    var ifNormalCached: Boolean = false,
    var ifTrainedCached: Boolean = false,

    //TODO:
    //var gatha: MutableSet<Int> = mutableSetOf()
)
