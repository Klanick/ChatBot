package org.example.springbot.dto

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.annotation.Nonnull

data class Connection (
    @Nonnull @JsonProperty("group_id") val groupId: Long,
    val secret: String? = null,
    @Nonnull @JsonProperty("confirm_token") val confirmToken: String,
    @Nonnull @JsonProperty("access_token") val accessToken: String,
)