package com.example

import com.example.dto.User
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respond(mapOf("message" to "Arr!"))
        }

        get("/{name}") {
            val name = call.parameters["name"] ?: "World"
            call.respond(mapOf("message" to "Hello $name!"))
        }

        post("/user") {
            try {
                val user = call.receive<User>()
                log.info("User ${user.name} with ${user.email} created!")
                call.respond(HttpStatusCode.Created)
            } catch (ex: IllegalStateException) {
                call.respond(HttpStatusCode.BadRequest)
            } catch (ex: JsonConvertException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

    }
}
