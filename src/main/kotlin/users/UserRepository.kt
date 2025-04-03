package com.example.users

import com.example.dto.Age
import com.example.dto.Email
import com.example.dto.User

interface UserRepository {
    fun createUser(user: User): Boolean

    fun getUserById(id: Int): User?
    fun getAllUsers(): List<User>

    fun getUserByEmail(email: Email): User?
    fun getUsersByAge(age: Age): List<User>

    fun updateUser(id: Int, user: User): Boolean

    fun deleteUser(id: Int): Boolean
    fun deleteUser(email: Email): Boolean
    fun deleteAllUsers(): Boolean
}
