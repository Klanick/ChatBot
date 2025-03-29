package org.example.springbot.service

import org.example.springbot.config.ConnectionConfig
import org.example.springbot.dto.Connection
import org.springframework.stereotype.Service

@Service
class ConnectionService(
    private val connectionConfig: ConnectionConfig,
    private val logger: LogService) {
    private val connections: MutableMap<Long, Connection> = mutableMapOf()

    init {
        connectionConfig.groupId?.let {
            addConnection(Connection(
                groupId = it,
                secret = connectionConfig.secret,
                confirmToken = connectionConfig.confirmToken,
                accessToken = connectionConfig.accessToken,
            ))
        }
    }

    fun addConnection(connection: Connection) {
        connections[connection.groupId] = connection
    }

    fun getConnections(): Map<Long, Connection> {
        return connections.toMap()
    }

    fun getConnection(groupId: Long, secret: String? = null): Connection? {
        connections[groupId]?.let {
            if (secret == null || it.secret == secret) {
                return it
            }
        }
        return null
    }
}