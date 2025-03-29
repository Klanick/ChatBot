package org.example.springbot.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "vk.api")
data class VkApiConfig(
    var endpoint: String = "",
    var version: String = "",
)