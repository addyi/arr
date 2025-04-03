package com.example

import com.example.dto.User
import com.example.dto.Email
import com.example.dto.Age
import com.example.dto.Users
import com.example.users.UserRepository
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

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

fun Application.configureUserRouting(userRepository: UserRepository) {
    routing {

        route("/user") {
            post {
                try {
                    val user = call.receive<User>()
                    log.info("Create $user requested!")

                    if (userRepository.createUser(user)) {
                        call.respond(HttpStatusCode.Created)
                    } else {
                        call.respond(HttpStatusCode.InternalServerError)
                    }
                } catch (ex: IllegalStateException) {
                    call.respond(HttpStatusCode.BadRequest)
                } catch (ex: JsonConvertException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }

            get("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid user ID")
                    return@get
                }
                val user = userRepository.getUserById(id)
                if (user != null) {
                    call.respond(user)
                } else {
                    call.respond(HttpStatusCode.NotFound, "User not found")
                }
            }

            get("/email/{email}") {
                val emailParameter = call.parameters["email"]
                if (emailParameter == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid email format")
                    return@get
                }

                try {
                    val email = Email(emailParameter)
                    val user = userRepository.getUserByEmail(email)
                    if (user != null) {
                        call.respond(user)
                    } else {
                        call.respond(HttpStatusCode.NotFound, "User not found")
                    }
                } catch (error: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid email format")
                }
            }

            put("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid user ID")
                    return@put
                }
                try {
                    val user = call.receive<User>()
                    if (userRepository.updateUser(id, user)) {
                        call.respond(HttpStatusCode.OK)
                    } else {
                        call.respond(HttpStatusCode.NotFound, "User not found")
                    }
                } catch (ex: IllegalStateException) {
                    call.respond(HttpStatusCode.BadRequest)
                } catch (ex: JsonConvertException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }

            delete("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid user ID")
                    return@delete
                }
                if (userRepository.deleteUser(id)) {
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.NotFound, "User not found")
                }
            }

            delete("/email/{email}") {
                val emailParameter = call.parameters["email"]
                if (emailParameter == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid email format")
                    return@delete
                }

                try {
                    val email = Email(emailParameter)
                    if (userRepository.deleteUser(email)) {
                        call.respond(HttpStatusCode.OK)
                    } else {
                        call.respond(HttpStatusCode.NotFound, "User not found")
                    }
                } catch (error: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid email format")
                }
            }
        }

        route("/users") {

            get {
                call.respond(Users(userRepository.getAllUsers()))

            }

            get("/age/{age}") {
                val ageParameter = call.parameters["age"]?.toIntOrNull()
                if (ageParameter == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid age")
                    return@get
                }

                try {
                    val age = Age(ageParameter)
                    call.respond(Users(userRepository.getUsersByAge(age)))
                } catch (error: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid age")
                    return@get
                }
            }

            delete {
                userRepository.deleteAllUsers()
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}

