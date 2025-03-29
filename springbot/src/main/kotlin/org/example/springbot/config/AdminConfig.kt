package org.example.springbot.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "admin")
data class AdminConfig (
    var token: String = "",
)