package org.example.springbot.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "connection")
data class ConnectionConfig(
    var groupId: Long? = null,
    var secret: String? = null,
    var confirmToken: String = "",
    var accessToken: String = "",
)