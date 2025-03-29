package org.example.springbot.dto

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.annotation.Nonnull

data class Event(
    @Nonnull val type: String,
    @Nonnull @JsonProperty("group_id") val groupId: Long,
    val secret: String? = null,
    @JsonProperty("object") val obj: EventObject? = null,
)

data class EventObject(
    val message: MessageInfo? = null,
)

data class MessageInfo(
    @JsonProperty("from_id") val fromId: Long,
    val text: String,
)