package org.example.springbot.contorller

import org.example.springbot.dto.Event
import org.example.springbot.dto.MessageInfo
import org.example.springbot.service.ConnectionService
import org.example.springbot.service.LogService
import org.example.springbot.service.SendService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@RestController
@RequestMapping("/vk/callback")
class CallBackController(
    private val connectionService: ConnectionService,
    private val sendService: SendService,
    private val logger: LogService,
) {
    @PostMapping
    fun processCallBack(@RequestBody event: Event): ResponseEntity<String> {
        logger.info { event }
        val connection = connectionService.getConnection(event.groupId, event.secret)
            ?: return ResponseEntity(
                "Bad Request: connection failed ${event.type}",
                HttpStatus.BAD_REQUEST
            )

        when (event.type) {
            "confirmation" -> {
                return ResponseEntity.ok(connection.confirmToken)
            }

            "message_new" -> {
                val message: MessageInfo = event.obj?.message
                    ?: return ResponseEntity(
                        "Bad Request: object.message is required for event type ${event.type}",
                        HttpStatus.BAD_REQUEST
                    )
                val encodedMessageText = URLEncoder.encode(
                    "Вы сказали: ${message.text}",
                    StandardCharsets.UTF_8
                )
                return if (sendService.sendMessage(
                        accessToken = connection.accessToken,
                        peerId = message.fromId,
                        message = encodedMessageText,
                    )
                ) ResponseEntity.ok("ok")
                else ResponseEntity(
                    "Message successfully received, but reply wasn't delivered",
                    HttpStatus.INTERNAL_SERVER_ERROR
                )
            }
        }
        return ResponseEntity.ok("ok")
    }
}