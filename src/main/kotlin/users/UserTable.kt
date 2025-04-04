package com.example.users

import com.example.dto.Age
import com.example.dto.Email
import com.example.dto.Name
import com.example.dto.User
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

object UserTable : UUIDTable("user") {
    val name = varchar("name", 255)
    val email = varchar("email", 255).uniqueIndex()
    val age = integer("age")
}

class UserDAO(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<UserDAO>(UserTable)

    var name: String by UserTable.name
    var email by UserTable.email
    var age by UserTable.age

    fun toUser() = User(
        name = Name(name),
        email = Email(email),
        age = Age(age)
    )
}

fun createUser(user: User): UserDAO = transaction {
    val userRes = UserDAO.new {
        name = user.name.value
        email = user.email.value
        age = user.age.value
    }
    commit()
    userRes
}

fun getUser(id: UUID): User? = transaction {
    UserDAO.findById(id)?.toUser()
}

fun updateUser(id: UUID, user: User): Boolean = transaction {
    val userDAO = UserDAO.findById(id) ?: return@transaction false
    userDAO.name = user.name.value
    userDAO.email = user.email.value
    userDAO.age = user.age.value

    commit()
    true
}

fun deleteUser(id: UUID): Boolean = transaction {
    val userDAO = UserDAO.findById(id) ?: return@transaction false
    userDAO.delete()
    commit()
    true
}
