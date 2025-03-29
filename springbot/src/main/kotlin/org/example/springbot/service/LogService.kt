package org.example.springbot.service

import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class LogService {
    private val logger = KotlinLogging.logger {}
    fun info(msg: () -> Any?) = logger.info(msg)
    fun warn(msg: () -> Any?) = logger.warn(msg)
    fun debug(msg: () -> Any?) = logger.debug(msg)
    fun error(msg: () -> Any?) = logger.error(msg)
}