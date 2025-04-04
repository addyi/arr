package com.example.route.users.model

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.UUID

object UserTable : UUIDTable(name = "user") {
    val name = varchar("name", 255)
    val email = varchar("email", 255).uniqueIndex()
    val age = integer("age")
}

class UserDAO(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<UserDAO>(UserTable)

    var name: String by UserTable.name
    var email by UserTable.email
    var age by UserTable.age

    fun toUser() = ResponseUser(
        id = id.value.toString(),
        name = Name(name),
        email = Email(email),
        age = Age(age)
    )
}