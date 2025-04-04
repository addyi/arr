package com.example.route.greeting

import io.ktor.server.application.Application
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun Application.configureGreetingRouting() {
    routing {
        get("/") {
            call.respond(mapOf("message" to "Arr!"))
        }

        get("/{name}") {
            val name = call.parameters["name"] ?: "World"
            call.respond(mapOf("message" to "Hello $name!"))
        }
    }
}
