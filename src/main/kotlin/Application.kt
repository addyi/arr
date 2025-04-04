package com.example


import com.example.config.configureCallLogging
import com.example.config.configureContentNegotiation
import com.example.config.configureDatabases
import com.example.route.greeting.configureGreetingRouting
import com.example.route.users.configureUserRouting
import com.example.route.users.repo.UserRepositoryImpl
import io.ktor.server.application.Application
import io.ktor.server.application.log

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    configureCallLogging()
    configureContentNegotiation()
    configureDatabases()
    configureGreetingRouting()
    configureUserRouting(userRepository = UserRepositoryImpl())
}
