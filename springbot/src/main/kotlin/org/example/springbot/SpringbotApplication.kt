package org.example.springbot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringbotApplication

fun main(args: Array<String>) {
    runApplication<SpringbotApplication>(*args)
}
