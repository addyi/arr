package com.example.route.users

import com.example.model.ErrorMessage
import com.example.route.users.model.Age
import com.example.route.users.model.Email
import com.example.route.users.model.RequestUser
import com.example.route.users.model.Users
import com.example.route.users.repo.UserRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import java.util.UUID

fun Application.configureUserRouting(userRepository: UserRepository) {
    routing {
        route("/user") {
            post {
                val newUser = call.getNewUser()
                    ?: return@post call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorMessage("Invalid user format"),
                    )

                val user = runCatching { userRepository.createUser(newUser) }.getOrNull()
                    ?: return@post call.respond(
                        HttpStatusCode.Conflict,
                        ErrorMessage("Email already exists"),
                    )
                call.respond(HttpStatusCode.Created, user)
            }

            get("/{id}") {
                val id = call.getUserUUID()
                    ?: return@get call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorMessage("Invalid user ID"),
                    )

                val user = userRepository.getUserById(id)
                if (user != null) {
                    call.respond(HttpStatusCode.OK, user)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }

            get("/email/{email}") {
                val email = call.getUserEmail()
                    ?: return@get call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorMessage("Invalid email format"),
                    )

                val user = userRepository.getUserByEmail(email)
                if (user != null) {
                    call.respond(HttpStatusCode.OK, user)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }

            put("/{id}") {
                val id = call.getUserUUID()
                    ?: return@put call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorMessage("Invalid user ID"),
                    )

                val newUser = call.getNewUser()
                    ?: return@put call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorMessage("Invalid user format"),
                    )

                if (userRepository.updateUser(id, newUser)) {
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(
                        HttpStatusCode.NotFound,
                        ErrorMessage("User not found"),
                    )
                }
            }

            delete("/{id}") {
                val id = call.getUserUUID()
                    ?: return@delete call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorMessage("Invalid user ID"),
                    )

                if (userRepository.deleteUser(id)) {
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(
                        HttpStatusCode.NotFound,
                        ErrorMessage("User not found"),
                    )
                }
            }

            delete {
                val age = call.pathParameters["maxAge"]?.toIntOrNull()
                    ?.let { runCatching { Age(it) } }?.getOrNull()
                    ?: return@delete call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorMessage("Invalid age format"),
                    )

                val numberOfDeletedUsers = userRepository.deleteUsersAboveAge(age)
                call.respond(HttpStatusCode.OK, mapOf("numberOfDeletedUsers" to numberOfDeletedUsers))
            }
        }

        route("/users") {
            get {
                call.respond(HttpStatusCode.OK, Users(userRepository.getAllUsers()))
            }

            get("/age/{age}") {
                val age = call.parameters["age"]?.toIntOrNull()
                    ?.let { runCatching { Age(it) } }?.getOrNull()
                    ?: return@get call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorMessage("Invalid age format"),
                    )

                call.respond(HttpStatusCode.OK, Users(userRepository.getUsersByAge(age)))
            }

            delete {
                userRepository.deleteAllUsers()
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}

fun RoutingCall.getUserUUID(): UUID? = parameters["id"]
    ?.let { runCatching { UUID.fromString(it) }.getOrNull() }

fun RoutingCall.getUserEmail(): Email? = parameters["email"]
    ?.let { runCatching { Email(it) }.getOrNull() }

suspend fun RoutingCall.getNewUser(): RequestUser? = runCatching { receive<RequestUser>() }.getOrNull()
