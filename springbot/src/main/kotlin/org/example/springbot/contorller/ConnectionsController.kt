package org.example.springbot.contorller

import org.example.springbot.dto.Connection
import org.example.springbot.service.AdminService
import org.example.springbot.service.ConnectionService
import org.example.springbot.service.LogService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admin")
class ConnectionsController(
    private val connectionService: ConnectionService,
    private val adminService: AdminService,
    private val logger: LogService,
    ) {

    @PostMapping("connects")
    fun addConnect(
        @RequestParam("admin_token") adminToken: String,
        @RequestBody request: Connection
    ): Boolean {
        if (adminService.checkAccess(adminToken)) {
            connectionService.addConnection(request)
            return true
        }
        return false
    }

    @GetMapping("connects")
    fun getConnections(@RequestParam adminToken: String): List<Connection>? {
        if (adminService.checkAccess(adminToken)) {
            return connectionService.getConnections().values.toList()
        }
        return null
    }
}