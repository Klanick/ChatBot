package org.example.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LongPollUpdatesResponseBody(
    val ts: Long,
    val updates: List<LongPollUpdate>
)

@Serializable
data class LongPollUpdate(
    val type: String,
    @SerialName("object") val obj: LongPollUpdateObject
)

@Serializable
data class LongPollUpdateObject(
    val message: MessageInfo? = null
)

