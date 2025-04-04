package com.example.route.users.repo

import com.example.route.users.model.Age
import com.example.route.users.model.Email
import com.example.route.users.model.RequestUser
import com.example.route.users.model.ResponseUser
import com.example.route.users.model.UserDAO
import com.example.route.users.model.UserTable
import com.example.util.suspendTransaction
import java.util.UUID
import org.jetbrains.exposed.sql.SqlExpressionBuilder.greater
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.deleteWhere

class UserRepositoryImpl : UserRepository {

    override suspend fun createUser(user: RequestUser): ResponseUser = suspendTransaction {
        UserDAO
            .new {
                name = user.name.value
                email = user.email.value
                age = user.age.value
            }
            .toUser()
    }

    override suspend fun getUserById(id: UUID): ResponseUser? = suspendTransaction {
        UserDAO.findById(id)?.toUser()
    }

    override suspend fun getAllUsers(): List<ResponseUser> = suspendTransaction {
        UserDAO
            .all()
            .map(UserDAO::toUser)
    }

    override suspend fun getUserByEmail(email: Email): ResponseUser? = suspendTransaction {
        UserDAO
            .find { UserTable.email eq email.value }
            .firstOrNull()
            ?.toUser()
    }

    override suspend fun getUsersByAge(age: Age): List<ResponseUser> = suspendTransaction {
        UserDAO
            .find { UserTable.age eq age.value }
            .map(UserDAO::toUser)
    }

    override suspend fun updateUser(id: UUID, user: RequestUser): Boolean = suspendTransaction {
        UserDAO
            .findByIdAndUpdate(id) {
                it.name = user.name.value
                it.email = user.email.value
                it.age = user.age.value
            } != null
    }

    override suspend fun deleteUser(id: UUID): Boolean = suspendTransaction {
        UserDAO.findById(id)?.let {
            it.delete()
            true
        } ?: false
    }

    override suspend fun deleteUsersAboveAge(age: Age): Int = suspendTransaction {
        UserTable.deleteWhere {
            UserTable.age greater age.value
        }
    }

    override suspend fun deleteAllUsers() = suspendTransaction {
        UserTable.deleteAll()
        Unit
    }
}
