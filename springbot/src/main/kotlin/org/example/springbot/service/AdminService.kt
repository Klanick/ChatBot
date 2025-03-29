package org.example.springbot.service

import org.example.springbot.config.AdminConfig
import org.springframework.stereotype.Service

@Service
class AdminService(
    private val adminConfig: AdminConfig,
    private val logger: LogService,
) {
    fun checkAccess(adminToken: String): Boolean {
        return adminConfig.token == adminToken
    }
}