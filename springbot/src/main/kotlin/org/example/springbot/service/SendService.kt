package org.example.springbot.service

import mu.KotlinLogging
import org.example.springbot.config.VkApiConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class SendService(
    private val requestService: RequestService,
    private val vkApiConfig: VkApiConfig,
    private val logger: LogService
) {

    fun sendMessage(accessToken: String, peerId: Long, message: String): Boolean {
        logger.info { "Request send message" }
        if (requestService.request(
            vkApiConfig.endpoint + "messages.send",
            mapOf(
                "access_token" to accessToken,
                "peer_id" to peerId,
                "message" to message,
                "random_id" to 0,
                "v" to vkApiConfig.version,
            )
        ) != null) {
            logger.info { "Successfully send message" }
            return true
        }
        return false
    }

}