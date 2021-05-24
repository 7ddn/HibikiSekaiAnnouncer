package org.sddn.plugin.hibiki.beans

import kotlinx.serialization.Serializable

@Serializable
data class Event(
    var id: Int = 0,
    //TODO: var eventType: String ="",
    var name: String = "",
    var startTime : Long = 0, //millisecond
    var scoreStopTime : Long = 0,
    var endTime : Long = 0,
)
