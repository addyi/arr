package com.example.config

import com.example.route.users.model.UserTable
import io.ktor.server.application.Application
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabases() {
    val dbConfig = environment.config.config("ktor.database")
    val dbDriver = dbConfig.property("driver").getString()
    val dbHost = dbConfig.property("host").getString()
    val dbName = dbConfig.property("name").getString()
    val dbUser = dbConfig.property("user").getString()
    val dbPassword = dbConfig.property("password").getString()
    val dbUrl = "jdbc:postgresql://$dbHost:5432/$dbName"

    val db = Database.connect(
        url = dbUrl,
        driver = dbDriver,
        user = dbUser,
        password = dbPassword,
    )

    transaction(db) {
        // Create the user table if it doesn't exist
        SchemaUtils.create(UserTable)
    }
//    // Insert some test data into the user table
//    createUser(User(Name("Anton"), Email("anton@example.com"), Age(8)))
//    createUser(User(Name("Arne"), Email("arne@example.com"), Age(12)))
//    createUser(User(Name("Danil"), Email("danil@example.com"), Age(14)))
//    createUser(User(Name("Friederike"), Email("friederike@example.com"), Age(16)))
//    createUser(User(Name("Iwo"), Email("iwo@example.com"), Age(18)))
//    createUser(User(Name("James"), Email("james@example.com"), Age(20)))
//    createUser(User(Name("Johannes"), Email("johannes@example.com"), Age(10)))
//    createUser(User(Name("Taras"), Email("taras@example.com"), Age(22)))
//    createUser(User(Name("Vladimir"), Email("vladimir@examle.com"), Age(24)))
//
//    transaction {
//
//        log.info("Fetching all route.users from the database")
//        exec("SELECT * FROM \"${UserTable.tableName}\"") { rs ->
//            while (rs.next()) {
//                val id = rs.getString("id")
//                val name = rs.getString("name")
//                val email = rs.getString("email")
//                val age = rs.getInt("age")
//                log.info("User: $id, $name, $email, $age")
//            }
//        }
//
//        exec("SELECT count(*) FROM \"${UserTable.tableName}\"") { rs ->
//            while (rs.next()) {
//                val count = rs.getInt(1)
//                log.info("User count: $count")
//            }
//        }
//
//    }
}
