package com.example

import com.example.users.FakeUserRepository
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import org.jetbrains.exposed.sql.Database
import org.slf4j.event.Level

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    configureContentNegotiation()
    configureCallLogging()
    configureGreetingRouting()
    configureUserRouting(userRepository = FakeUserRepository())
}

fun Application.configureCallLogging() {
    install(CallLogging) {
        level = Level.INFO
        // filter { call -> call.request.path().startsWith("/api") }
        format { call ->
            val status = call.response.status()
            val httpMethod = call.request.httpMethod.value
            val userAgent = call.request.headers["User-Agent"]
            val requestPath = call.request.path()
            "Status: $status, HTTP method: $httpMethod, Request path: $requestPath, User agent: $userAgent"
        }
    }
}

fun Application.configureContentNegotiation() {
    install(ContentNegotiation) {
        json()
    }
}

fun Application.configureDatabases() {
    Database.connect(
        "jdbc:postgresql://localhost:5432/ktor_tutorial_db",
        user = "postgresql",
        password = "password"
    )
}