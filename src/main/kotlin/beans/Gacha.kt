package org.sddn.plugin.hibiki.beans

import kotlinx.serialization.Serializable

@Serializable
data class Gacha(
    var id: Int = 0,
    val name: String = "",
    var rarity1Rate: Double = 0.0,
    var rarity2Rate: Double = 0.0,
    var rarity3Rate: Double = 0.0,
    var rarity4Rate: Double = 0.0,
    var startTime : Long = 0,
    var endTime : Long = 0,
    var contents : List<Int> = listOf(),
    var pickups : List<Int> = listOf(),
)
