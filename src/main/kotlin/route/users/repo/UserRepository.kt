package com.example.route.users.repo


import com.example.route.users.model.Age
import com.example.route.users.model.Email
import com.example.route.users.model.RequestUser
import com.example.route.users.model.ResponseUser
import java.util.UUID

interface UserRepository {
    suspend fun createUser(user: RequestUser): ResponseUser

    suspend fun getUserById(id: UUID): ResponseUser?
    suspend fun getAllUsers(): List<ResponseUser>

    suspend fun getUserByEmail(email: Email): ResponseUser?
    suspend fun getUsersByAge(age: Age): List<ResponseUser>

    suspend fun updateUser(id: UUID, user: RequestUser): Boolean

    suspend fun deleteUser(id: UUID): Boolean
    suspend fun deleteUsersAboveAge(age: Age): Int
    suspend fun deleteAllUsers()
}
