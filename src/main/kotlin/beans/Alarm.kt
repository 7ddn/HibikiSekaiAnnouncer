package org.sddn.plugin.hibiki.beans

import kotlinx.serialization.Serializable

@Serializable
data class Alarm(
    var time: Long = 0, // millisecond
    var message: String, //mirai code
    var contactType: String, //group, user
    var contactId: Long, //groupId, userId
)
