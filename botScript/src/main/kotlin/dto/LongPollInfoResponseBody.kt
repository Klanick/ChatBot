package org.example.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LongPollInfoResponseBody(
    val response: LongPollInfo
)

@Serializable
data class LongPollInfo(
    val server: String,
    val key: String,
    val ts: Long
)

@Serializable
data class MessageInfo(
    @SerialName("from_id") val fromId: Long,
    val text: String
)