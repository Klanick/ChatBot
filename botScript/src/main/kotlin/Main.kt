package org.example

import java.net.URI
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.*
import kotlinx.serialization.json.Json
import org.example.dto.LongPollInfoResponseBody
import org.example.dto.LongPollUpdatesResponseBody
import org.example.dto.MessageInfo
import java.net.URLEncoder
import java.net.http.HttpClient
import java.nio.charset.StandardCharsets
import mu.KotlinLogging
import kotlin.system.exitProcess

private val logger = KotlinLogging.logger {}
private fun Scanner.getVariable(name: String): String =
    System.getenv(name) ?: run {
        println("Input $name:")
        this.nextLine().trim()
    }

private const val VK_API_ENDPOINT = "https://api.vk.com/method/"
private const val VK_API_VERSION = "5.199"

private fun <T> retry(retries: Int = 3, delayMs: Long = 1000, function: () -> T): T {
    for (attempt in 1..retries) {
        try {
            return function.invoke()!!
        } catch (e: Exception) {
            logger.warn { "Failed $attempt attempt: ${e.message}" }
        }
        if (attempt < retries) {
            Thread.sleep(delayMs * attempt)
        }
    }
    exitWithFail("Failed all $retries attempts")
}

private fun exitWithFail(errorMessage: String): Nothing {
    logger.error { errorMessage }
    exitProcess(1)
}

private fun makeRequest(client: HttpClient, url: String, params: Map<String, Any?> = mapOf()): String {
    val fullUrl = URI(url + params
        .map { it.key + "=" + it.value.toString() }
        .joinToString(prefix = "?", separator = "&")
    )
    val request = HttpRequest.newBuilder().uri(fullUrl).build()
    val response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).join()
    return response.body()
}


fun main() {
    val scanner = Scanner(System.`in`)
    val accessToken: String = scanner.getVariable("VK_ACCESS_TOKEN")
    val groupId: String = scanner.getVariable("VK_GROUP_ID")

    val client = HttpClient.newBuilder().build()
    val json = Json { ignoreUnknownKeys = true }

    logger.info { "Requesting data for connecting to LongPollServer" }
    val longPollInfo = retry {
        val longPollInfoResponseBody = makeRequest(
            client,
            VK_API_ENDPOINT + "groups.getLongPollServer",
            mapOf(
                "access_token" to accessToken,
                "group_id" to groupId,
                "v" to VK_API_VERSION
            )
        )
        json.decodeFromString<LongPollInfoResponseBody>(longPollInfoResponseBody).response
    }
    logger.info { "Successfully received data for connecting to LongPollServer" }

    val serverAddress = longPollInfo.server
    val serverKey: String = longPollInfo.key
    var ts = longPollInfo.ts

    while (true) {
        logger.info { "Requesting updates from LongPollServer" }
        val longPollUpdates = retry {
            val longPollUpdatesResponseBody = makeRequest(
                client,
                serverAddress,
                mapOf(
                    "act" to "a_check",
                    "key" to serverKey,
                    "ts" to ts,
                    "wait" to 25
                )
            )
            json.decodeFromString<LongPollUpdatesResponseBody>(longPollUpdatesResponseBody)
        }
        logger.info { "Successfully received updates from LongPollServer" }

        ts = longPollUpdates.ts
        longPollUpdates.updates
            .filter { it.type == "message_new" }
            .forEach {
                val message: MessageInfo = it.obj.message
                    ?: exitWithFail("Failed to find message")
                val encodedMessageText = URLEncoder.encode(
                    "Вы сказали: ${message.text}",
                    StandardCharsets.UTF_8
                )

                logger.info { "Request send message" }
                retry {
                    makeRequest(
                        client,
                        VK_API_ENDPOINT + "messages.send",
                        mapOf(
                            "access_token" to accessToken,
                            "peer_id" to message.fromId,
                            "message" to encodedMessageText,
                            "random_id" to 0,
                            "v" to VK_API_VERSION,
                        )
                    )
                }
                logger.info { "Successfully send message" }
            }
    }
}