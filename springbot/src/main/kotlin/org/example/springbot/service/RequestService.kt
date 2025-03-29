package org.example.springbot.service

import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Service
class RequestService(private val logger: LogService) {
    val client: HttpClient = HttpClient.newHttpClient()

    fun request(url: String, params: Map<String, Any?> = mapOf()): String? {
        return retry { makeRequest(url, params) }
    }

    private fun <T> retry(retries: Int = 3, delayMs: Long = 1000, function: () -> T): T? {
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
        logger.error { "Failed all $retries attempts" }
        return null
    }

    private fun makeRequest(url: String, params: Map<String, Any?> = mapOf()): String {
        val fullUrl = URI(url + params
            .map { it.key + "=" + it.value.toString() }
            .joinToString(prefix = "?", separator = "&")
        )
        logger.info { "for URL: $fullUrl" }
        val request = HttpRequest.newBuilder().uri(fullUrl).build()
        val response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).join()
        return response.body()
    }
}

