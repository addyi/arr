package com.example

import com.example.dto.Age
import com.example.dto.Email
import com.example.dto.Name
import com.example.dto.User
import com.example.users.FakeUserRepository
import com.example.users.UserTable
import com.example.users.createUser
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.event.Level

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    configureContentNegotiation()
    configureCallLogging()
    configureDatabases()
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
    val dbConfig = environment.config.config("ktor.database")
    val dbDriver = dbConfig.property("driver").getString()
    val dbUrl = dbConfig.property("url").getString()
    val dbUser = dbConfig.property("user").getString()
    val dbPassword = dbConfig.property("password").getString()

    val db = Database.connect(
        url = dbUrl,
        driver = dbDriver,
        user = dbUser,
        password = dbPassword,
    )

    transaction(db) {
        // Delete the database if it exists
        SchemaUtils.drop(UserTable)
        // Create the user table
        SchemaUtils.create(UserTable)

        commit()
    }
    // Insert some test data into the user table
    createUser(User(Name("Anton"), Email("anton@example.com"), Age(8)))
    createUser(User(Name("Arne"), Email("arne@example.com"), Age(12)))
    createUser(User(Name("Danil"), Email("danil@example.com"), Age(14)))
    createUser(User(Name("Friederike"), Email("friederike@example.com"), Age(16)))
    createUser(User(Name("Iwo"), Email("iwo@example.com"), Age(18)))
    createUser(User(Name("James"), Email("james@example.com"), Age(20)))
    createUser(User(Name("Johannes"), Email("johannes@example.com"), Age(10)))
    createUser(User(Name("Taras"), Email("taras@example.com"), Age(22)))
    createUser(User(Name("Vladimir"), Email("vladimir@examle.com"), Age(24)))

    transaction {

        log.info("Fetching all users from the database")
        exec("SELECT * FROM \"${UserTable.tableName}\"") { rs ->
            while (rs.next()) {
                val id = rs.getString("id")
                val name = rs.getString("name")
                val email = rs.getString("email")
                val age = rs.getInt("age")
                log.info("User: $id, $name, $email, $age")
            }
        }

        exec("SELECT count(*) FROM \"${UserTable.tableName}\"") { rs ->
            while (rs.next()) {
                val count = rs.getInt(1)
                log.info("User count: $count")
            }
        }

    }
}
